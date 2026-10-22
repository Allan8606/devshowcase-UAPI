package com.devshowcase.controller;

import com.devshowcase.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private String json;

    private String url;


    @BeforeEach
    void setup(){
        //mockMvc = MockMvcBuilders.standaloneSetup(profileController).alwaysDo(print()).build();
        url = "/api";

    }



}