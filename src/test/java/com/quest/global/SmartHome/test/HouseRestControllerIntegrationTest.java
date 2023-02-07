package com.quest.global.SmartHome.test;


import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class HouseRestControllerIntegrationTest {

    @Mock
    private HouseService houseService;

    private MapStructMapperImpl mapper =
            new MapStructMapperImpl();

    private final ObjectMapper objectMapper =
            new ObjectMapper();

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
    public void testRetrieveHouseByID() throws Exception {

        // Given
        ObjectId id = new ObjectId("637cb085be36767eae89c267");
        HouseRepository mockRepository = mock(HouseRepository.class);
        House mockHouse = new House();

        mockHouse.setId(id);
        mockHouse.setName("CasaMEA");
        mockHouse.setRooms(new ArrayList<>());
        mockHouse.setDevicesList(new ArrayList<>());

        // When
        MockHttpServletResponse response = mockMvc.perform(
                get("/api/houses/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void testRetrieveHouseByNameWhenExists() throws Exception {

        // Given
        ObjectId id = ObjectId.get();
        String houseName = "MyHouse";

        HouseDTO expectedHouse =
                mapper.houseToHouseDTO(new House(id, houseName, new ArrayList<>(), new ArrayList<>()));

        given(houseService.findHouseByName(houseName))
                .willReturn(expectedHouse);

        // When
        String responseJson = mockMvc.perform(get("/api/houses/")
                        .param("houseName", houseName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then
        HouseDTO actualHouse = objectMapper.readValue(responseJson, HouseDTO.class);
        assertThat(actualHouse).isEqualTo(expectedHouse);
    }

    @Test
    public void testGetHouseByID_Success() throws Exception {

        // Given
        ObjectId id =
                ObjectId.get();

        House house =
                new House();

        house.setId(id);
        house.setName("AdrianHouse");
        house.setRooms(new ArrayList<>());
        house.setDevicesList(new ArrayList<>());

        HouseDTO expectedHouse = mapper.houseToHouseDTO(house);

        given(houseService.findHouseByID(id)).willReturn(expectedHouse);

        // When
        String responseJson =
                mockMvc.perform(get("/api/houses/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then
        HouseDTO actualHouse = objectMapper.readValue(responseJson, HouseDTO.class);
        assertThat(actualHouse).isEqualTo(expectedHouse);

    }

    @Test
    public void testGetHouseByID_WhenNotFound() throws Exception {

        // Given
        ObjectId id = ObjectId.get();
        String errorMsg = "House ID not found!";

        given(houseService.findHouseByID(id)).willThrow(new HouseNotFoundException(errorMsg));

        // When
        String responseJson = mockMvc.perform(get("/api/houses/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        // Then
        assertThat(responseJson).isEqualTo(errorMsg);

    }


    @Test
    public void getHouseByName_ReturnsErrorMessage_WhenHouseNotFound() throws Exception {

        // Given
        String houseName = "Test House";
        String expectedErrorMessage = "House not found";

        // When
        when(houseService.findHouseByName(houseName))
                .thenThrow(new HouseNotFoundException(expectedErrorMessage));

        String responseJson = mockMvc.perform(get("/api/houses/")
                        .param("houseName", houseName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        // Then
        assertThat(responseJson).isEqualTo(expectedErrorMessage);
    }

    @Test
    public void testGetAllHouses() throws Exception {

        ObjectId id1 = new ObjectId("637cb085be36767eae89c267");
        ObjectId id2 = new ObjectId("637cb085be36767eae89c268");

        // Given
        List<House> expectedList = new ArrayList<>();
        expectedList.add(new House(id1, "House1", new ArrayList<>(), new ArrayList<>()));
        expectedList.add(new House(id2, "House2", new ArrayList<>(), new ArrayList<>()));

        given(houseService.listAll()).willReturn(expectedList);

        // When
        String responseJson =
                mockMvc.perform(get("/api/houses/")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        // Then
        TypeReference<List<House>> typeReference =
                new TypeReference<>() {};

        List<House> actualList =
                new ObjectMapper().readValue(responseJson, typeReference);

        assertThat(actualList).isEqualTo(expectedList);
    }

}
