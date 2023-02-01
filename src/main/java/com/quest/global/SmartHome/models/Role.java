package com.quest.global.SmartHome.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("Roles")
public class Role {

    @Id
    @Field(name = "_id")
    private ObjectId id;

    @Field(name = "Name")
    private String name;

    @Field(name = "NormalizedName")
    private String normalizedName;

    public Role(String name) {
        this.name = name;
    }
}
