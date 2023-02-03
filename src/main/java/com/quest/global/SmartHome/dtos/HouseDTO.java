package com.quest.global.SmartHome.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseDTO {

    private ObjectId id;

    private String name;

    private List<ObjectId> rooms;

}
