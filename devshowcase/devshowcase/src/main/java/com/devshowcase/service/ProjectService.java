package com.devshowcase.service;

import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.dto.request.ProjectRequestDTO;
import com.devshowcase.dto.response.ProfileResponseDTO;
import com.devshowcase.dto.response.ProjectResponseDTO;
import com.devshowcase.entity.Profile;
import com.devshowcase.entity.Project;
import com.devshowcase.entity.Technology;
import com.devshowcase.repository.ProfileRepository;
import com.devshowcase.repository.ProjectRepository;
import com.devshowcase.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final TechnologyRepository technologyRepository;

    //Cadastrar
    public ProjectResponseDTO cadastrar(ProjectRequestDTO request){
        Project project = converterParaEntity(request);
        Project save = projectRepository.save(project);

        return converterParaResponse(save);
    }

    //Listar Todos
    public List<ProjectResponseDTO> listarTodos(){
        return projectRepository.findAll()
                .stream()
                .map(project -> converterParaResponse(project))
                .toList();
    }

    //Listar por ID
    public ProjectResponseDTO buscarPorId(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow();

        return converterParaResponse(project);
    }

    //Editar Project
    public ProjectResponseDTO editar(Long id, ProjectRequestDTO request){

        Project project = projectRepository.findById(id)
                .orElseThrow();

        Profile profile = profileRepository.findById(request.profileId())
                .orElseThrow();

        Set<Technology> technologies = technologyRepository
                .findAllById(request.technologyIds())
                .stream()
                .collect(Collectors.toSet());


        project.setTitle(request.title());
        project.setDescription(request.description());
        project.setGithubUrl(request.githubUrl());
        project.setProjectUrl(request.projectUrl());
        project.setProfile(profile);
        project.setTechnologies(technologies);


        Project save = projectRepository.save(project);

        return converterParaResponse(save);


    }








    //===================MÉTODOS AUXILIARES, PARA CONVERTER================
    private Project converterParaEntity(ProjectRequestDTO request){
        Profile profile = profileRepository.findById(request.profileId()).orElseThrow();
        return new Project(
                request.title(),
                request.description(),
                request.githubUrl(),
                request.projectUrl(),
                profile
        );
    }

    private ProjectResponseDTO converterParaResponse(Project project) {

        Set<Long> technologyIds = project.getTechnologies()
                .stream()
                .map(technology -> technology.getId())
                .collect(Collectors.toSet());

        return new ProjectResponseDTO(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getGithubUrl(),
                project.getProjectUrl(),
                project.getProfile().getId(),
                technologyIds
        );
    }
}
