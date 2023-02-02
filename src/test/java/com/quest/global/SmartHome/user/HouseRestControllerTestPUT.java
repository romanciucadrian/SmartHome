package com.quest.global.SmartHome.user;


import com.quest.global.SmartHome.controllers.HouseController;
import com.quest.global.SmartHome.dtos.HouseDTO;
import com.quest.global.SmartHome.exceptions.HouseNotFoundException;
import com.quest.global.SmartHome.mapstruct.MapStructMapperImpl;
import com.quest.global.SmartHome.models.House;
import com.quest.global.SmartHome.repositories.HouseRepository;
import com.quest.global.SmartHome.services.HouseService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class HouseRestControllerTestPUT {

    @Mock
    HouseService houseService;

    @MockBean
    HouseRepository houseRepository;

    @InjectMocks
    HouseController houseController;

    @Mock
    MapStructMapperImpl mapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(houseController)
                .build();
    }

    @Test
    public void testUpdateHouseName() throws Exception {

        // Given
        String houseName = "AdrianROM4";
        String houseNewName = "AdrianROM5";

        House updatedHouse = new House();
        updatedHouse.setName(houseNewName);
        updatedHouse.setRooms(new ArrayList<>());
        updatedHouse.setDevicesList(new ArrayList<>());

        // When
        when(houseService.updateHouseName(houseName, houseNewName)).thenReturn(updatedHouse);

        mockMvc.perform(put("/api/houses/{houseName}", houseName)
                        .param("houseNewName", houseNewName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        verify(houseService, times(1)).updateHouseName(houseName, houseNewName);

    }

    @Test
    public void testGetHouseById() throws Exception {

        // Given
        ObjectId id = new ObjectId("637cb085be36767eae89c267");
        List<ObjectId> roomList = new ArrayList<>();
        roomList.add(new ObjectId("637cb085be36767eae89c268"));
        roomList.add(new ObjectId("637cb085be36767eae89c269"));
        roomList.add(new ObjectId("637cb085be36767eae89c26a"));

        House expectedHouse = new House(id, "AdrianROM4", roomList, new ArrayList<>());

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setId(new ObjectId("637cb085be36767eae89c267"));
        houseDTO.setName("AdrianROM4");
        houseDTO.setRooms(roomList);


        // When
        when(mapper.houseToHouseDTO(expectedHouse)).thenReturn(houseDTO);
        when(houseService.findHouseByID(id)).thenReturn(houseDTO);


        mockMvc.perform(get("/api/houses/{id}", id.toHexString()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(id)))
                .andExpect((ResultMatcher) jsonPath("$.name", is("AdrianROM4")));
    }

//    @Test
//    public void testRetrieveHouseByID() throws Exception {
//
//        // Given
//        given(houseRepository.findById(new ObjectId("637cb085be36767eae89c267")).orElse(null)
//                .thenReturn(new House()))
//    }
}
