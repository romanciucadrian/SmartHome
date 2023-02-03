package com.quest.global.SmartHome.controllers;

import com.quest.global.SmartHome.dtos.HouseDTO;
import com.quest.global.SmartHome.exceptions.HouseNotFoundException;
import com.quest.global.SmartHome.exceptions.HouseNotFoundRestException;
import com.quest.global.SmartHome.mapstruct.mappers.MapStructMapper;
import com.quest.global.SmartHome.models.House;
import com.quest.global.SmartHome.repositories.HouseRepository;
import com.quest.global.SmartHome.services.HouseService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping
    public House saveHouse(@RequestBody House house) {
        return houseService.createHouse(house);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getHouseById(@PathVariable ObjectId id) {

        try {
          return ResponseEntity.ok(houseService.findHouseByID(id));

        } catch (HouseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping(params = "houseName")
    public ResponseEntity<?> getHouseByName(@RequestParam String houseName) {

        try {
            return new ResponseEntity<>(houseService.findHouseByName(houseName),
                    HttpStatus.OK);
        } catch (HouseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("{houseName}")
    public ResponseEntity<?> updateHouseByName(@PathVariable("houseName") String houseName,
                                               @RequestParam("houseNewName") String houseNewName) {

        try {
            return new
                    ResponseEntity<>(houseService.updateHouseName
                    (
                            houseName, houseNewName
                    ),
            HttpStatus.OK
            );

        } catch (HouseNotFoundException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<House>> getAll() {
        return new ResponseEntity<>(houseService.listAll(),
                HttpStatus.OK
        );
    }

}
