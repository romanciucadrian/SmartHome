package com.quest.global.SmartHome.mapstruct;

import com.quest.global.SmartHome.dtos.*;
import com.quest.global.SmartHome.dtos.mqtt.MqttPublish;
import com.quest.global.SmartHome.mapstruct.mappers.MapStructMapper;
import com.quest.global.SmartHome.models.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MapStructMapperImpl implements MapStructMapper {

    @Override
    public HouseDTO houseToHouseDTO(House house) {
        if (house == null) {
            return null;
        }
        HouseDTO houseDTO = new HouseDTO();


        houseDTO.setName(house.getName());
        houseDTO.setRooms(roomListToRoomSlimDTOList(house.getRooms()));

        return houseDTO;
    }

    @Override
    public House houseDtoToHouse(HouseDTO houseDTO) {
        if (houseDTO == null) {
            return null;
        }
        House house = new House();

        house.setName(houseDTO.getName());
        house.setRooms(houseDTO.getRooms());

        return house;
    }

    @Override
    public List<HouseDTO> housesToHouseDTOs(List<House> houses) {
        if (houses == null) {
            return null;
        }
        List<HouseDTO> list = new ArrayList<>();
        for (House house : houses) {
            list.add(houseToHouseDTO(house));
        }
        return list;
    }

    @Override
    public RoomDTO roomToRoomDTO(Room room) {
        if (room == null) {
            return null;
        }
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setHouseId(room.getHouseId());

        return roomDTO;
    }

    @Override
    public Room roomDtoToRoom(RoomDTO roomDTO) {
        if (roomDTO == null) {
            return null;
        }
        Room room = new Room();

        room.setName(roomDTO.getName());
        room.setHouseId(roomDTO.getHouseId());

        return room;
    }

    @Override
    public UserViewDTO userToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserViewDTO userViewDTO = new UserViewDTO();

        userViewDTO.setEmail(user.getEmail());
        userViewDTO.setFirstName(user.getFirstName());
        userViewDTO.setLastName(user.getLastName());
        userViewDTO.setHousesList(houseListToHouseDTOList(user.getHousesList()));

        return userViewDTO;
    }

    @Override
    public List<UserViewDTO> usersToUsersDTOs(List<User> users) {
        if (users == null) {
            return null;
        }
        List<UserViewDTO> list = new ArrayList<>();
        for (User user : users) {
            list.add(userToUserDTO(user));
        }
        return list;
    }

    @Override
    public MqttPublish commandToMqttPublish(Command command) {
        if (command == null) {
            return null;
        }
        MqttPublish mqttPublish = new MqttPublish();

        mqttPublish.setTopic(command.getTopic());
        mqttPublish.setPayload(command.getPayload());

        return mqttPublish;

    }

    @Override
    public DeviceTypeDTO deviceTypeToDeviceTypeDTO(DeviceType deviceType) {
        if (deviceType == null) {
            return null;
        }
        DeviceTypeDTO deviceTypeDTO = new DeviceTypeDTO();

        deviceTypeDTO.setDeviceTypeName(deviceType.getDeviceTypeName());
        deviceTypeDTO.setDeviceHexName(deviceType.getDeviceHexName());

        return deviceTypeDTO;
    }

    @Override
    public DeviceDTO deviceToDeviceDTO(Device device) {
        if (device == null) {
            return null;
        }
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setHexId(device.getHexId());

        return deviceDTO;
    }

    @Override
    public CommandDTO commandToCommandDTO(Command command) {
        if (command == null){
            return null;
        }
        CommandDTO commandDTO = new CommandDTO();
        commandDTO.setDeviceNameCommand(command.getDeviceNameCommand());

        return commandDTO;
    }

    protected List<ObjectId> houseListToHouseDTOList(List<ObjectId> list) {
        if (list == null) {
            return null;
        }
        List<ObjectId> idList = new ArrayList<>();
        for (ObjectId houseID : list) {
            idList.add(houseID);
        }
        return idList;
    }

    protected List<ObjectId> roomListToRoomSlimDTOList(List<ObjectId> list) {
        if (list == null) {
            return null;
        }
        List<ObjectId> list1 = new ArrayList<>();
        for (ObjectId room : list) {
            list1.add(room);
        }
        return list1;
    }


}
