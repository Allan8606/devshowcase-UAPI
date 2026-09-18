package com.devshowcase.service;

import com.devshowcase.dto.request.FeedbackRequestDTO;
import com.devshowcase.dto.response.FeedbackResponseDTO;
import com.devshowcase.entity.Feedback;
import com.devshowcase.entity.Project;
import com.devshowcase.repository.FeedbackRepository;
import com.devshowcase.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProjectRepository projectRepository;


    //Cadastrar
    public FeedbackResponseDTO cadastrar(FeedbackRequestDTO request){
        Feedback feedback = converterParaEntity(request);
        Feedback save = feedbackRepository.save(feedback);
        return converterParaResponse(save);
    }

    //Listar Todos
    public List<FeedbackResponseDTO> listarTodos(){
        return feedbackRepository.findAll()
                .stream()
                .map(feedback -> converterParaResponse(feedback))
                .toList();
    }

    //Listar por ID
    public FeedbackResponseDTO buscarPorId(Long id){
        Feedback feedback = feedbackRepository.findById(id).orElseThrow();
        return converterParaResponse(feedback);
    }

    //Editar Project
    public FeedbackResponseDTO editar(Long id, FeedbackRequestDTO request){
        Feedback feedback = feedbackRepository.findById(id).orElseThrow();

        feedback.setComment(request.comment());
        Feedback save = feedbackRepository.save(feedback);
        return converterParaResponse(save);

    }

    //Deletar Project
    public void deletar(Long id){
        Feedback feedback = feedbackRepository.findById(id).orElseThrow();
        feedbackRepository.delete(feedback);
    }




    //===================MÉTODOS AUXILIARES, PARA CONVERTER================

    private Feedback converterParaEntity(FeedbackRequestDTO request){
        Project project = projectRepository.findById(request.projectId()).orElseThrow();

        return new Feedback(
                request.comment(),
                request.rating(),
                project
        );
    }

    private FeedbackResponseDTO converterParaResponse(Feedback feedback) {
        return new FeedbackResponseDTO(
                feedback.getId(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getProject().getId()
        );
    }
}
