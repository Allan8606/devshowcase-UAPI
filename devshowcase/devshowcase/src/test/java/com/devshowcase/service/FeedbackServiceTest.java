package com.devshowcase.service;


import com.devshowcase.dto.request.FeedbackRequestDTO;
import com.devshowcase.dto.response.FeedbackResponseDTO;
import com.devshowcase.entity.Feedback;
import com.devshowcase.entity.Profile;
import com.devshowcase.entity.Project;
import com.devshowcase.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.*;


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

    @Nested
    class listarTodos{

        @Test
        @DisplayName("Deve listar todos os feedbacks cadastrados.")
        void deveListarTodosFeedbacks(){
            List<Feedback> feedbackList = List.of(feedback);

            when(feedbackRepository.findAll()).thenReturn(feedbackList);

            List<FeedbackResponseDTO> listService = feedbackService.listarTodos();

            assertNotNull(listService);
            assertEquals(feedbackList.size(), listService.size());
            assertEquals(feedback.getComment(), listService.get(0).comment());

            verify(feedbackRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia quando não houver feedbacks cadastrados.")
        void deveRetornarUmaListaVaziaQuandoNaoHouverFeedbacks(){

            when(feedbackRepository.findAll()).thenReturn(List.of());

            List<FeedbackResponseDTO> listService = feedbackService.listarTodos();

            assertNotNull(listService);
            assertTrue(listService.isEmpty());
            verify(feedbackRepository).findAll();
        }

    }

    @Nested
    class buscarPorId{

        @Test
        @DisplayName("Deve buscar feedback pelo ID")
        void deveBuscarFeedbackPeloId(){

            when(feedbackRepository.findById(feedback.getId())).thenReturn(Optional.of(feedback));

            FeedbackResponseDTO buscarPorIdService = feedbackService.buscarPorId(project.getId());

            assertNotNull(buscarPorIdService);
            assertEquals(feedback.getId(), buscarPorIdService.id());
            assertEquals(feedback.getComment(), buscarPorIdService.comment());

            verify(feedbackRepository).findById(feedback.getId());
        }

        @Test
        @DisplayName("Deve retornar uma exceção quando não encontrar um feedback.")
        void deveRetornarExcecaoQuandoNaoEncontrarFeedback(){

            when(feedbackRepository.findById(feedback.getId())).thenReturn(Optional.empty());

         assertThrows(ResourceNotFoundException.class, ()-> feedbackService.buscarPorId(feedback.getId()));

         verify(feedbackRepository).findById(feedback.getId());
        }



    }

    @Nested
    class editar{


        @Test
        @DisplayName("Deve editar um feedback")
        void deveEditarUmFeedback() {

            // ARRANGE - Preparar os dados
            FeedbackRequestDTO request = criaRequest();

            when(feedbackRepository.findById(feedback.getId()))
                    .thenReturn(Optional.of(feedback));

            when(feedbackRepository.findByProjectId(project.getId()))
                    .thenReturn(List.of(feedback));

            when(feedbackRepository.save(any(Feedback.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // ACT - Executar o método
            FeedbackResponseDTO resultado =
                    feedbackService.editar(feedback.getId(), request);

            // ASSERT - Verificar o resultado
            assertNotNull(resultado);
            assertEquals(feedback.getId(), resultado.id());
            assertEquals(request.comment(), resultado.comment());
            assertEquals(request.rating(), resultado.rating());

            // VERIFY - Verificar e capturar o feedback salvo
            verify(feedbackRepository).save(feedbackArgumentCaptor.capture());

            Feedback feedbackSalvo = feedbackArgumentCaptor.getValue();

            assertEquals(request.comment(), feedbackSalvo.getComment());
            assertEquals(request.rating(), feedbackSalvo.getRating());

            verify(feedbackRepository).findById(feedback.getId());
            verify(feedbackRepository).findByProjectId(project.getId());
            verify(projectRepository).save(project);

            assertEquals((double) request.rating(), project.getAverageRating());
        }


        @Test
        @DisplayName("Deve lançar exceção quando não existir um feedback")
        void deveLancarExcecaoQuandoNaoExistirFeedback(){
            FeedbackRequestDTO request = criaRequest();

            when(feedbackRepository.findById(feedback.getId())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> feedbackService.editar(feedback.getId(), request));

            verify(feedbackRepository).findById(feedback.getId());
            verify(feedbackRepository, never()).save(any(Feedback.class));

        }

    }

    @Nested
    class deletar{
        @Test
        @DisplayName("Deve deletar com feedback com sucesso.")
        void deveDeletarUmFeedbackComSucesso(){

            when(feedbackRepository.findById(feedback.getId())).thenReturn(Optional.of(feedback));

            feedbackService.deletar(feedback.getId());

            verify(feedbackRepository).findById(feedback.getId());
            verify(feedbackRepository).delete(feedback);
        }

        @Test
        @DisplayName("Deve lançar uma exceção quando não encontrar um feedback.")
        void deveLancarUmaExcecaoQuandoNaoEncontrarFeedback(){

            when(feedbackRepository.findById(feedback.getId())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> feedbackService.deletar(feedback.getId()));

            verify(feedbackRepository).findById(feedback.getId());
            verify(feedbackRepository, never()).delete(feedback);
        }
    }
    
}