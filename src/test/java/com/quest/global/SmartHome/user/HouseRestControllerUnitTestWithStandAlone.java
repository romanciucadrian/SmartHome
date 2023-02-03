package com.quest.global.SmartHome.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.global.SmartHome.controllers.HouseController;
import com.quest.global.SmartHome.dtos.HouseDTO;
import com.quest.global.SmartHome.exceptions.HouseNotFoundException;
import com.quest.global.SmartHome.mapstruct.MapStructMapperImpl;
import com.quest.global.SmartHome.models.House;
import com.quest.global.SmartHome.repositories.HouseRepository;
import com.quest.global.SmartHome.services.HouseService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class HouseRestControllerUnitTestWithStandAlone {

    @Mock
    private HouseService houseService;


    private MapStructMapperImpl mapper = new MapStructMapperImpl();

    @InjectMocks
    private HouseController houseController;

    private JacksonTester<HouseDTO> jsonHouse;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(houseController)
                .build();
    }

//    @Test
//    public void testUpdateHouseName() throws Exception {
//
//        // Given
//        String houseName = "AdrianROM4";
//        String houseNewName = "AdrianROM5";
//
//        House updatedHouse = new House();
//        updatedHouse.setName(houseNewName);
//        updatedHouse.setRooms(new ArrayList<>());
//        updatedHouse.setDevicesList(new ArrayList<>());
//
//        // When
//        when(houseService.updateHouseName(houseName, houseNewName)).thenReturn(updatedHouse);
//
//        mockMvc.perform(put("/api/houses/{houseName}", houseName)
//                        .param("houseNewName", houseNewName)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        // Then
//        verify(houseService, times(1)).updateHouseName(houseName, houseNewName);
//
//    }

//    @Test
//    public void testRetrieveHouseByID() throws Exception {
//
//        // Given
//        ObjectId id = new ObjectId("637cb085be36767eae89c267");
//        HouseRepository mockRepository = mock(HouseRepository.class);
//        House mockHouse = new House();
//
//        mockHouse.setId(id);
//        mockHouse.setName("CasaMEA");
//        mockHouse.setRooms(new ArrayList<>());
//        mockHouse.setDevicesList(new ArrayList<>());
//
//        // When
//        MockHttpServletResponse response = mockMvc.perform(
//                get("/api/houses/{id}", id)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Then
//        assertThat(response.getStatus())
//                .isEqualTo(HttpStatus.OK.value());
//
//    }

    @Test
    public void testRetrieveHouseByNameWhenExists() throws Exception {

        // Given
        ObjectId id = new ObjectId();
        String houseName = "MyHouse";

        HouseDTO expectedHouse =
                mapper.houseToHouseDTO(new House(id, houseName, new ArrayList<>(), new ArrayList<>()));

        given(houseService.findHouseByName(houseName))
                .willReturn(expectedHouse);

        // When
        String responseJson = mockMvc.perform(get("/api/houses/" + houseName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then
        assertThat(responseJson).isEqualTo(expectedHouse.toString());
    }


    @Test
    public void getHouseByName_ReturnsErrorMessage_WhenHouseNotFound() throws Exception {
        String houseName = "Test House";
        String expectedErrorMessage = "House not found";

        when(houseService.findHouseByName(houseName))
                .thenThrow(new HouseNotFoundException(expectedErrorMessage));

        mockMvc = MockMvcBuilders.standaloneSetup(houseController).build();

        String responseJson = mockMvc.perform(get("/api/houses/" + houseName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertThat(responseJson).isEqualTo(expectedErrorMessage);
    }

}
