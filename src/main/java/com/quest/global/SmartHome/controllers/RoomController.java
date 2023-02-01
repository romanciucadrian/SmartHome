package com.quest.global.SmartHome.controllers;

import com.quest.global.SmartHome.exceptions.HouseNotFoundException;
import com.quest.global.SmartHome.models.Room;
import com.quest.global.SmartHome.services.RoomService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("{houseID}")
    public ResponseEntity<?> saveRoom(@PathVariable ObjectId houseID,
                                   @RequestBody Room room) throws HouseNotFoundException {

        try {
            return new ResponseEntity<>(roomService.createRoomForAHouse(houseID, room),

                    HttpStatus.OK
                    );
        } catch (HouseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }
}
