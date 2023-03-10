package com.app.smarthome.controllers;

import com.app.smarthome.configuration.MqttConfig;
import com.app.smarthome.dtos.mqtt.MqttPublish;
import com.app.smarthome.exceptions.DeviceNotFoundException;
import com.app.smarthome.exceptions.DeviceTypeNotFoundException;
import com.app.smarthome.services.DeviceService;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mqtt")
public class MqttController {


    private final DeviceService deviceService;
    private final MqttConfig mqttConfig;

    public MqttController(DeviceService deviceService, MqttConfig mqttConfig) {
        this.deviceService = deviceService;
        this.mqttConfig = mqttConfig;
    }

    @PostMapping
    public void publishMessage(@RequestParam String topic,
                                @RequestParam String payload)
            throws org.eclipse.paho.client.mqttv3.MqttException {

        MqttPublish mqttPublish =
                new MqttPublish();

        mqttPublish.setTopic(topic);
        mqttPublish.setPayload(payload);

        MqttMessage mqttMessage =
                new MqttMessage(mqttPublish.getPayload().getBytes());
        mqttMessage.setQos(2);

        mqttConfig.getClient().publish(mqttPublish.getTopic(), mqttMessage);

    }

    @GetMapping("{deviceHexName}")
    public ResponseEntity<?> saveDeviceInSystem(@PathVariable String deviceHexName) throws DeviceNotFoundException,
            InterruptedException, DeviceTypeNotFoundException {

        try {
            return new ResponseEntity<>(deviceService.createDeviceByDeviceTypeHexName(deviceHexName),
                    HttpStatus.OK
            );
        } catch (DeviceTypeNotFoundException | DeviceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }


    }






}
