package com.devshowcase.service;

import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.dto.response.ProfileResponseDTO;
import com.devshowcase.entity.Profile;
import com.devshowcase.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    //Cadastrar
    public ProfileResponseDTO cadastrar(ProfileRequestDTO request){

        Profile profile = conversoRequest(request);

        Profile save = profileRepository.save(profile);

        return conversoEntity(save);
    }

    //Listar Todos os Profiles
    public List<ProfileResponseDTO> listarTodos(){
        return profileRepository.findAll().stream().map(profile -> conversoEntity(profile)).toList();
    }

    //Listar por ID
    public ProfileResponseDTO buscarPorId(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow();

        return conversoEntity(profile);

    }




    //============================CONVERSORES================================
    private Profile conversoRequest(ProfileRequestDTO request){
        return new Profile(
                request.name(),
                request.bio(),
                request.githubUrl(),
                request.linkedinUrl()
        );
    }

    private ProfileResponseDTO conversoEntity(Profile profile){
        return new ProfileResponseDTO(
                profile.getId(),
                profile.getName(),
                profile.getBio(),
                profile.getGithubUrl(),
                profile.getLinkedinUrl(),
                null
        );
    }
}
