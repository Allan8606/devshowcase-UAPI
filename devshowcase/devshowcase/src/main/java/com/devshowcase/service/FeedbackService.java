package com.devshowcase.service;

import com.devshowcase.dto.request.FeedbackRequestDTO;
import com.devshowcase.dto.response.FeedbackResponseDTO;
import com.devshowcase.entity.Feedback;
import com.devshowcase.entity.Project;
import com.devshowcase.exception.ResourceNotFoundException;
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
    public FeedbackResponseDTO cadastrar(Long projectId, FeedbackRequestDTO request) {

        Feedback feedback = converterParaEntity(projectId, request);
        Feedback save = feedbackRepository.save(feedback);

        Double media = calcularMedia(save.getProject().getId());

        Project project = save.getProject();
        project.setAverageRating(media);
        projectRepository.save(project);

        return converterParaResponse(save);
    }

    //Listar Todos
    public List<FeedbackResponseDTO> listarTodos() {
        return feedbackRepository.findAll()
                .stream()
                .map(feedback -> converterParaResponse(feedback))
                .toList();
    }

    //Listar por ID
    public FeedbackResponseDTO buscarPorId(Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Feedback não encontrado"));
        return converterParaResponse(feedback);
    }

    //Editar Feedback
    public FeedbackResponseDTO editar(Long id, FeedbackRequestDTO request) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Feedback não encontrado"));

        feedback.setRating(request.rating());
        feedback.setComment(request.comment());

        Feedback save = feedbackRepository.save(feedback);

        Double media = calcularMedia(save.getProject().getId());

        Project project = save.getProject();
        project.setAverageRating(media);
        projectRepository.save(project);

        return converterParaResponse(save);
    }

    //Deletar Feedback
    public void deletar(Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Feedback não encontrado"));
        feedbackRepository.delete(feedback);
    }

    //===================MÉTODOS AUXILIARES================

    private Feedback converterParaEntity(Long projectId, FeedbackRequestDTO request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projeto não encontrado"));

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

    private Double calcularMedia(Long projectId) {

        List<Feedback> feedbacks = feedbackRepository.findByProjectId(projectId);

        if (feedbacks.isEmpty()) {
            return 0.0;
        }

        double soma = feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .sum();

        double media = soma / feedbacks.size();

        return media;
    }
}
