package com.quest.global.SmartHome.user;

import com.quest.global.SmartHome.controllers.HouseController;
import com.quest.global.SmartHome.models.House;
import com.quest.global.SmartHome.services.HouseService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HouseRestControllerGETAll {

    @InjectMocks
    HouseController houseController;

    @Mock
    HouseService houseService;

//    @Test
//    public void getAllHousesAPI() throws Exception {
//
//        // Given
//        House house1 =
//                new House(new ObjectId("637cb085be36767eae89c26a"), "CASA1", new ArrayList<>(), new ArrayList<>());
//        House house2 =
//                new House(new ObjectId("637cb085be36767eae89c26b"), "CASA2", new ArrayList<>(), new ArrayList<>());
//
//        List<House> houseList = new ArrayList<>();
//        houseList.add(house1);
//        houseList.add(house2);
//
//        // When
//        when(houseService.listAll()).thenReturn(houseList);
//
//     //  ResponseEntity<List<House>> result =  houseController.getAll();
//
//        // Then
//        assertThat(Objects.requireNonNull(result.getBody()).size()).isEqualTo(2);
//        assertThat(result.getBody().get(0).getName()).isEqualTo(house1.getName());
//        assertThat(result.getBody().get(1).getName()).isEqualTo(house2.getName());
//    }
}
