package com.quest.global.SmartHome.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.global.SmartHome.exceptions.DeviceNotFoundException;
import com.quest.global.SmartHome.exceptions.DeviceTypeNotFoundException;
import com.quest.global.SmartHome.mapstruct.MapStructMapperImpl;
import com.quest.global.SmartHome.models.Device;
import com.quest.global.SmartHome.models.DeviceType;
import com.quest.global.SmartHome.repositories.DeviceRepository;
import com.quest.global.SmartHome.repositories.DeviceTypeRepository;
import com.quest.global.SmartHome.services.impl.IDeviceService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeviceService implements IDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final MqttService mqttService;
    private final DeviceTypeRepository deviceTypeRepository;

    public DeviceService(DeviceRepository deviceRepository, MqttService mqttService,
                         DeviceTypeRepository deviceTypeRepository) {

        this.deviceRepository = deviceRepository;
        this.mqttService = mqttService;
        this.deviceTypeRepository = deviceTypeRepository;
    }


    public List<Device> findAll() {
        return new ArrayList<>(deviceRepository.findAll());
    }

    @Transactional
    public Device saveDevice(Device device) {

        return deviceRepository.save(device);
    }

    @Transactional
    public Device findDeviceByName(String deviceName) throws DeviceNotFoundException {
        Device device =
                deviceRepository.findDeviceByDeviceName(deviceName);

        try {
           return device;
        } catch (NoSuchElementException exception) {
            throw new DeviceNotFoundException("Can't find a device with this name!");
        }
    }

    @Transactional
    public Device updateDeviceByName(String deviceName, String deviceNewName) throws DeviceNotFoundException {

        try {
            Device device = deviceRepository.findDeviceByDeviceName(deviceName);
            device.setDeviceName(deviceNewName);

            return deviceRepository.save(device);
        } catch (NoSuchElementException e) {
            throw new DeviceNotFoundException("This device name doesn't exist!");
        }
    }

    @Transactional
    public void deleteDeviceByName(String deviceName) throws DeviceNotFoundException {
        Device device
                = findDeviceByName(deviceName);

        try {
            deviceRepository.delete(device);
        } catch (NoSuchElementException e) {
            throw new DeviceNotFoundException("This device doesn't exist!");
        }
    }

    @Transactional
    public Device createDeviceByDeviceTypeHexName(String deviceHexName) throws
            DeviceTypeNotFoundException, DeviceNotFoundException, InterruptedException {

        DeviceType deviceType =
               deviceTypeRepository.findDeviceTypeByDeviceHexName(deviceHexName).orElse(null);

        if (deviceType == null) {
            throw new DeviceTypeNotFoundException("DeviceType not found!");
        } else {
            Device device =
                   deviceRepository.findDeviceByHexId(deviceHexName).orElse(null);
            if (device != null) {
                throw new DeviceNotFoundException("This device exists!");
            } else {

                mqttService.doPublish(deviceType.getDeviceTypeName());

                Thread.sleep(3000);

                return deviceRepository.findDeviceByHexId(deviceHexName).orElse(null);
            }
        }
    }




}