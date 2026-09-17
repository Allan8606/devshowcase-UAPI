package com.devshowcase.service;

import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.dto.response.ProfileResponseDTO;
import com.devshowcase.dto.response.ProjectResponseDTO;
import com.devshowcase.entity.Profile;
import com.devshowcase.entity.Project;
import com.devshowcase.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    //Editar Profile
    public ProfileResponseDTO editar(Long id, ProfileRequestDTO request){
        Profile profile = profileRepository.findById(id)
                .orElseThrow();

        profile.setName(request.name());
        profile.setBio(request.bio());
        profile.setGithubUrl(request.githubUrl());
        profile.setLinkedinUrl(request.linkedinUrl());

        Profile save = profileRepository.save(profile);

        return conversoEntity(save);

    }

    //Deletar Profile
    public void deletar (Long id){
        Profile profile = profileRepository.findById(id)
                .orElseThrow();
        profileRepository.delete(profile);
    }




    //============================MÉTODOS AUXILIARES, PARA CONVERTER================================
    private Profile conversoRequest(ProfileRequestDTO request){
        return new Profile(
                request.name(),
                request.bio(),
                request.githubUrl(),
                request.linkedinUrl()
        );
    }

    private ProfileResponseDTO conversoEntity(Profile profile) {

        Set<Project> projects = profile.getProjects();

        Set<ProjectResponseDTO> projectResponses = projects
                .stream()
                .map(project -> new ProjectResponseDTO(
                        project.getId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getGithubUrl(),
                        project.getProjectUrl(),
                        project.getProfile().getId(),
                        project.getTechnologies()
                                .stream()
                                .map(technology -> technology.getId())
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toSet());

        return new ProfileResponseDTO(
                profile.getId(),
                profile.getName(),
                profile.getBio(),
                profile.getGithubUrl(),
                profile.getLinkedinUrl(),
                projectResponses
        );
    }
}
