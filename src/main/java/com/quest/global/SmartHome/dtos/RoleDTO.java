package com.quest.global.SmartHome.dtos;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class RoleDTO {

    private ObjectId id;

    private String name;
}
