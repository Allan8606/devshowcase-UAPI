package com.devshowcase.service;


import com.devshowcase.dto.request.TechnologyRequestDTO;
import com.devshowcase.dto.response.TechnologyResponseDTO;
import com.devshowcase.entity.Technology;
import com.devshowcase.exception.ResourceNotFoundException;
import com.devshowcase.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyService {
    private final TechnologyRepository technologyRepository;

    //Cadastrar
    public TechnologyResponseDTO cadastrar(TechnologyRequestDTO request){
        Technology technology = converteParaEntity(request);
        return converteParaResponse(technologyRepository.save(technology));
    }

    //Listar todos
    public List<TechnologyResponseDTO> listarTodos(){
        List<TechnologyResponseDTO> list = technologyRepository.findAll()
                .stream()
                .map(technology -> converteParaResponse(technology))
                .toList();
        return list;
    }

    //Listar Por ID
    public TechnologyResponseDTO buscarPorid(Long id){
        Technology technology = technologyRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tecnologia não encontrada"));
        return converteParaResponse(technology);
    }

    //Editar
    public TechnologyResponseDTO editar(Long id, TechnologyRequestDTO request){
        Technology technology = technologyRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tecnologia não encontrada"));
        technology.setName(request.name());

        Technology save = technologyRepository.save(technology);

        return converteParaResponse(save);
    }

    //Deletar
    public void deletar(Long id){
        Technology technology = technologyRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tecnologia não encontrada"));
        technologyRepository.delete(technology);
    }

    //====================Métodos auxiliares===============
    private Technology converteParaEntity(TechnologyRequestDTO request){
        return new Technology(
                request.name()
        );
    }

    private TechnologyResponseDTO converteParaResponse(Technology technology){
        return new TechnologyResponseDTO(
                technology.getId(),
                technology.getName()
        );
    }

}
