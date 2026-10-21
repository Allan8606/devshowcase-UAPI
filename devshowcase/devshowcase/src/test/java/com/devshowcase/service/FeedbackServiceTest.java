package com.devshowcase.service;


import com.devshowcase.dto.request.FeedbackRequestDTO;
import com.devshowcase.dto.response.FeedbackResponseDTO;
import com.devshowcase.entity.Feedback;
import com.devshowcase.entity.Profile;
import com.devshowcase.entity.Project;
import com.devshowcase.repository.FeedbackRepository;
import com.devshowcase.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @Captor
    private ArgumentCaptor<Feedback> feedbackArgumentCaptor;

    private Project project;
    private Feedback feedback;
    private Profile profile;

    private FeedbackRequestDTO criaRequest(){
        return new FeedbackRequestDTO(
                8,
                "Projeto muito bom."
        );
    }


    @BeforeEach
    void setup(){

        profile = new Profile(
                "João",
                "Dev",
                "github.com/joao",
                "linkedin.com/in/joao");

        profile.setId(1L);

        project = new Project(
                "Projeto Java com testes.",
                "Projeto desenvolvido com fins academico e de aprendizado.",
                "github.com",
                "www.projectjava.com",
                profile
        );

        project.setId(1L);

        feedback = new Feedback(
                "Projeto muito bom.",
                8,
                project
        );
        feedback.setId(1L);
    }

    @Nested
    class cadastrar {

        @Test
        @DisplayName("Deve cadastrar um feedback")
        void deveCadastrarUmFeedback() {

            // ARRANGE: prepara os dados
            FeedbackRequestDTO request = criaRequest();

            when(projectRepository.findById(project.getId()))
                    .thenReturn(Optional.of(project));

            when(feedbackRepository.save(any(Feedback.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Simula a lista usada no cálculo da média
            when(feedbackRepository.findByProjectId(project.getId()))
                    .thenReturn(List.of(feedback));

            // ACT: executa o cadastro
            FeedbackResponseDTO resultado =
                    feedbackService.cadastrar(project.getId(), request);

            // ASSERT: verifica os dados retornados
            assertNotNull(resultado);
            assertEquals(request.rating(), resultado.rating());
            assertEquals(request.comment(), resultado.comment());
            assertEquals(project.getId(), resultado.projectId());

            // Captura o feedback enviado para salvar
            verify(feedbackRepository).save(feedbackArgumentCaptor.capture());

            Feedback feedbackEnviado = feedbackArgumentCaptor.getValue();

            // Verifica os dados do feedback enviado
            assertEquals(request.rating(), feedbackEnviado.getRating());
            assertEquals(request.comment(), feedbackEnviado.getComment());
            assertEquals(project.getId(), feedbackEnviado.getProject().getId());

            // Verifica se a média foi atualizada no projeto
            double mediaEsperada = feedback.getRating();

            assertEquals(mediaEsperada, project.getAverageRating());

            // VERIFY: confirma as chamadas aos repositórios
            verify(feedbackRepository).findByProjectId(project.getId());
            verify(projectRepository).save(project);
        }
    }

}