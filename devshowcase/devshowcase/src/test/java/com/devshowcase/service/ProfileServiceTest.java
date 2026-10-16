package com.devshowcase.service;

import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.entity.Profile;
import com.devshowcase.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Nested
    class cadastrar {

        @Test
        @DisplayName("Deve criar um Profile com sucesso.")
        void deveCadastrar(){

            //Arrange
            var profile = new Profile(
                    "João",
                    "Dev",
                    "github.com/joao",
                    "linkedin.com/in/joao");


            doReturn(profile).when(profileRepository).save(any(Profile.class));; // não é uma boa pratica  usar o any.

            var input = new ProfileRequestDTO(
                    "username",
                    "biografia",
                    "github.com.br",
                    "www.linkedin.com");

            //ACT
           var output =  profileService.cadastrar(input);


            //Assert
            assertNotNull(output);
        }

    }

}