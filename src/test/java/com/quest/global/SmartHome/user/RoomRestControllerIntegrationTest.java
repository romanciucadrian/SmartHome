package com.quest.global.SmartHome.user;

import com.quest.global.SmartHome.controllers.RoomController;
import com.quest.global.SmartHome.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoomRestControllerIntegrationTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    @BeforeEach
    public void setup() {

    }


}
