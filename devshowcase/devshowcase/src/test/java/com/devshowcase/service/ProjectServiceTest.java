package com.devshowcase.service;

import com.devshowcase.dto.request.ProjectRequestDTO;
import com.devshowcase.dto.response.ProjectResponseDTO;
import com.devshowcase.entity.Profile;
import com.devshowcase.entity.Project;
import com.devshowcase.exception.ResourceNotFoundException;
import com.devshowcase.repository.ProfileRepository;
import com.devshowcase.repository.ProjectRepository;
import com.devshowcase.repository.TechnologyRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @InjectMocks
    private ProjectService projectService;

    @Captor
    private ArgumentCaptor<Project> projectArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> idArgumentCaptor;

    private Profile profile;
    private Project project;

    private ProjectRequestDTO criarRequest(){
        return new ProjectRequestDTO(
                "Projeto Java com testes.",
                "Projeto desenvolvido com fins acadêmicos e de aprendizado.",
                "github.com",
                "www.projectjava.com",
                1L,
                Set.of()
        );
    }

    @BeforeEach
    void setup() {

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

    }

        @Nested
        class cadastrar{

            @Test
            @DisplayName("Deve cadastrar um Project corretamente")
            void deveCadastrarUmProjectCorretamente(){


                //ARRANGE
                var request = criarRequest();

                when(profileRepository.findById(profile.getId()))
                        .thenReturn(Optional.of(profile));

                when(technologyRepository.findAllById(request.technologyIds()))
                        .thenReturn(List.of());

                //Simulando a chamada do repository quando for necessario.
                when(projectRepository.save(any(Project.class)))
                        .thenReturn(project);

                //ACT

                //Guardando o valor retornado do método do service.
                var cadastrado = projectService.cadastrar(request);


                //ASSERT

                //Verifica se o id é igual.
                assertEquals(project.getId(), cadastrado.id());

                // Verifica se o título retornado é o esperado
                assertEquals(project.getTitle(), cadastrado.title());

                //Verifica se  o resultado não é nulo.
                assertNotNull(cadastrado);

                //Verifica se o método save do repositorio foi chamado apenas uma vez. E salvou o project
                verify(projectRepository).save(projectArgumentCaptor.capture());
            }

            @Test
            @DisplayName("Deve verificar se um id de um Profile não foi encontrado")
            void deveLancarExcecaoQuandoPerfilNaoForEncontrado(){

                var request = criarRequest();

                when(profileRepository.findById(request.profileId())).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, ()-> projectService.cadastrar(request));

                verify(profileRepository).findById(request.profileId());
                verify(projectRepository, never()).save(any(Project.class));
            }

        }


        @Nested
        class listarTodas{

            @Test
            @DisplayName("Deve listar todos os projetos cadastrados.")
            void deveListarTodosProjetos(){
                List<Project> projectList = List.of(project);

                when(projectRepository.findAll()).thenReturn(projectList);

                List<ProjectResponseDTO> listService = projectService.listarTodos();

                assertNotNull(listService);
                assertEquals(projectList.size(), listService.size());
                assertEquals(project.getTitle(), listService.get(0).title());
                verify(projectRepository).findAll();
            }

            @Test
            @DisplayName("Deve retornar uma lista vazia quando não houver projetos")
            void deveRetornarListaVaziaQuandoNaoHouverProjetos() {
                // ARRANGE: simula um repositório sem projetos cadastrados
                when(projectRepository.findAll()).thenReturn(List.of());

                // ACT: executa o método que estamos testando
                List<ProjectResponseDTO> lista = projectService.listarTodos();


                assertNotNull(lista);
                assertTrue(lista.isEmpty());
                verify(projectRepository).findAll();
            }
        }


        @Nested
        class buscarPorId{

            @Test
            @DisplayName("Deve buscar um projeto pelo ID")
            void deveBuscarUmProjetoPeloId() {

                // ARRANGE: simula o repositório encontrando o projeto
                when(projectRepository.findById(project.getId()))
                        .thenReturn(Optional.of(project));

                // ACT: executa o método que estamos testando
                ProjectResponseDTO projectResponseDTO =
                        projectService.buscarPorId(project.getId());

                // ASSERT: verifica se o resultado foi retornado corretamente
                assertNotNull(projectResponseDTO);
                assertEquals(project.getId(), projectResponseDTO.id());
                assertEquals(project.getTitle(), projectResponseDTO.title());

                // VERIFY: verifica se o repositório foi consultado pelo ID esperado
                verify(projectRepository).findById(project.getId());
            }

            @Test
            @DisplayName("Deve lançar exceção quando não encontrar um projeto")
            void deveRetornarExcecaoQuandoNaoEncontrarProjeto() {

                // ARRANGE: simula que o projeto não foi encontrado
                when(projectRepository.findById(project.getId()))
                        .thenReturn(Optional.empty());

                // ACT + ASSERT: verifica se o serviço lança a exceção esperada
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> projectService.buscarPorId(project.getId())
                );
                verify(projectRepository).findById(project.getId());
            }


        }







}