package com.quest.global.SmartHome.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.global.SmartHome.configuration.MqttConfig;
import com.quest.global.SmartHome.dtos.mqtt.MqttPublish;
import com.quest.global.SmartHome.mapstruct.mappers.MapStructMapper;
import com.quest.global.SmartHome.models.Device;
import com.quest.global.SmartHome.repositories.CommandRepository;
import com.quest.global.SmartHome.repositories.DeviceRepository;
import com.quest.global.SmartHome.repositories.DeviceTypeRepository;
import com.quest.global.SmartHome.services.impl.IMqttService;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MqttService implements IMqttService {

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);
    private final ObjectMapper objectMapper;
    private final DeviceRepository deviceRepository;
    private final MqttConfig mqttConfig;
    private final MapStructMapper mapStructMapper;
    private final CommandRepository commandRepository;
    private final CommandService commandService;
    private final ExecutorService executorService
            = Executors.newFixedThreadPool(10);

    @Value("${mqtt.subscribe.topic}")
    private String subscribeTopic;

    public MqttService(ObjectMapper objectMapper, DeviceRepository deviceRepository, MqttConfig mqttConfig,
                       MapStructMapper mapStructMapper, CommandRepository commandRepository, CommandService commandService) {

        this.objectMapper = objectMapper;
        this.deviceRepository = deviceRepository;
        this.mqttConfig = mqttConfig;
        this.mapStructMapper = mapStructMapper;
        this.commandRepository = commandRepository;
        this.commandService = commandService;
    }

    @Async
    public void doPublish(String deviceNameCommand) {

        MqttPublish mqttPublish
                = mapStructMapper.commandToMqttPublish(commandRepository.findCommandByDeviceNameCommand(deviceNameCommand).orElse(null));

        MqttMessage mqttMessage
                = new MqttMessage();

        mqttMessage.setPayload(mqttPublish.getPayload().getBytes());
        mqttMessage.setQos(2);

        try {
            mqttConfig.getClient()
                    .publish(mqttPublish.getTopic(), mqttMessage).waitForCompletion();
        } catch (MqttException e) {
            logger.error("Error while publishing to topic: {}", mqttPublish.getTopic(), e);
        }

    }

    @Async
    public void doSubscribe() {

        executorService.submit(() -> {
            try {
                mqttConfig.getClient()
                            .subscribe(subscribeTopic, 2, null, new IMqttActionListener() {

                            @Override
                            public void onSuccess(IMqttToken iMqttToken) {
                                logger.info("Successfully subscribed to topic: {}", subscribeTopic);
                            }

                            @Override
                            public void onFailure(IMqttToken iMqttToken, Throwable exception) {
                                logger.error("Error occurred while subscribing to topic: {}", subscribeTopic, exception);
                            }
                        }, new IMqttMessageListener() {

                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                String arrivedMessage = new String(message.getPayload());
                                logger.info("The received message is: {}" + arrivedMessage);

                                if (commandService.getAllTopics().contains(topic)) {
                                    handleReceivedMessage(arrivedMessage);
                                }
                            }
                        });

            } catch (MqttException e) {
                logger.error("Error occurred while subscribing to topic {}", subscribeTopic, e);
            }
        });
    }

    private void handleReceivedMessage(String arrivedMessage) {
        try {
            Device device = objectMapper.readValue(arrivedMessage, Device.class);


            JSONObject jsonObject = new JSONObject(arrivedMessage);

            JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("ZbStatus3"));

            device.setHexId(((JSONObject)jsonArray.get(0)).getString("Device"));

            deviceRepository.save(device);

        } catch (IOException e) {
            throw new RuntimeException("Entity doesn't saved!");
        }
    }

}

// TODO: Incarca proiectul pe git
// Creaza branch separat pentru fiecare task
// Fa pull request pentru a face merge din branch -> master

// Foloseste o librarie pentru code coverage. ex: jacoco
// Scrie teste pentru Controllere, servicii.
    // Testeaza si functii care arunca exceptii