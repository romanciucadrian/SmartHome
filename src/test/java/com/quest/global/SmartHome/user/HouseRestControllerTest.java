package com.quest.global.SmartHome.user;


import com.quest.global.SmartHome.controllers.HouseController;
import com.quest.global.SmartHome.dtos.HouseDTO;
import com.quest.global.SmartHome.mapstruct.MapStructMapperImpl;
import com.quest.global.SmartHome.models.House;
import com.quest.global.SmartHome.repositories.HouseRepository;
import com.quest.global.SmartHome.services.HouseService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;;


import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class HouseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    HouseController controller;

    @Mock
    HouseService houseService;

    @Autowired
    private MapStructMapperImpl mapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/houses/{id}")
    public void getHouseByIdTest() throws Exception {

        // Given
        House house1 =
                new House(new ObjectId("637ce74e7f00342303b19e43"), "CasaMea1",
                        new ArrayList<>(), new ArrayList<>());

        House house2 =
                new House(new ObjectId("63bfdd024872887ba23fbc3e"), "CasaMea2",
                        new ArrayList<>(), new ArrayList<>());

        HouseDTO houseDTO1 =
                mapper.houseToHouseDTO(house1);

        HouseDTO houseDTO2 =
                mapper.houseToHouseDTO(house2);

        // When
        doReturn(houseDTO1).when(houseService).findHouseByID(new ObjectId("637ce74e7f00342303b19e43"));
        HouseDTO foundHouseDTO1 =
                houseService.findHouseByID(new ObjectId("637ce74e7f00342303b19e43"));

        doReturn(houseDTO2).when(houseService).findHouseByID(new ObjectId("63bfdd024872887ba23fbc3e"));
        HouseDTO foundHouseDTO2 =
                houseService.findHouseByID(new ObjectId("63bfdd024872887ba23fbc3e"));


        // Then
        assertNotNull(foundHouseDTO1);
        assertEquals(houseDTO1.getName(), foundHouseDTO1.getName());

        assertNotNull(foundHouseDTO2);
        assertEquals(houseDTO2.getName(), foundHouseDTO2.getName());

    }




}
