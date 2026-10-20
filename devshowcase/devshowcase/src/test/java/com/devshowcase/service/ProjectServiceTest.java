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


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;




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

        @Nested
        class editar{

            @Test
            @DisplayName("Deve editar um projeto com sucesso")
            void deveEditarUmProjeto() {

                // ARRANGE: prepara os dados da edição
                var request = criarRequest();

                when(projectRepository.findById(project.getId()))
                        .thenReturn(Optional.of(project));

                when(profileRepository.findById(request.profileId()))
                        .thenReturn(Optional.of(profile));

                when(technologyRepository.findAllById(request.technologyIds()))
                        .thenReturn(List.of());

                // Retorna o próprio objeto recebido pelo save()
                when(projectRepository.save(any(Project.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));


                // ACT: executa a edição
                ProjectResponseDTO resultado =
                        projectService.editar(project.getId(), request);

                // ASSERT: verifica os dados retornados
                assertNotNull(resultado);
                assertEquals(project.getId(), resultado.id());
                assertEquals(request.title(), resultado.title());
                assertEquals(request.description(), resultado.description());
                assertEquals(request.githubUrl(), resultado.githubUrl());
                assertEquals(request.projectUrl(), resultado.projectUrl());

                // VERIFY + CAPTOR: captura o projeto enviado para salvar
                verify(projectRepository).save(projectArgumentCaptor.capture());

                Project projetoEnviado = projectArgumentCaptor.getValue();

                // Verifica se o objeto enviado contém os dados atualizados
                assertEquals(request.title(), projetoEnviado.getTitle());
                assertEquals(request.description(), projetoEnviado.getDescription());
                assertEquals(request.githubUrl(), projetoEnviado.getGithubUrl());
                assertEquals(request.projectUrl(), projetoEnviado.getProjectUrl());

                // Verifica se o projeto foi buscado pelo ID correto
                verify(projectRepository).findById(project.getId());
            }

            @Test
            @DisplayName("Deve lançar exceção se projeto não existir")
            void deveLancarExcecaoSeProjetoNaoExistir() {

                var request = criarRequest();

                when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, ()-> projectService.editar(project.getId(), request));
                verify(projectRepository).findById(project.getId());
                verify(projectRepository, never()).save(any(Project.class));
            }

            @Test
            @DisplayName("Deve lançar exceção se perfil não existir")
            void deveLancarExcecaoSePerfilNaoExistir() {

                var request = criarRequest();

                when(projectRepository.findById(project.getId()))
                        .thenReturn(Optional.of(project));
                when(profileRepository.findById(profile.getId())).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, ()-> projectService.editar(project.getId(), request));
                verify(profileRepository).findById(profile.getId());
                verify(projectRepository, never()).save(any(Project.class));
            }





        }

        @Nested
        class deletar{

            @Test
            @DisplayName("Deve deletar um Project pelo ID")
            void deveDeletarUmPorjectPeloId(){

                when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

                projectService.deletar(project.getId());

                verify(projectRepository, times(1)).findById(project.getId());
                verify(projectRepository).delete(project);
            }

            @Test
            @DisplayName("Deve lançar uma exceção caso não seja localizado um projeto")
            void develancarUmaExcecaoCasoNaoSejaLocalizadaoUmProjeto(){

                when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, ()-> projectService.deletar(project.getId()));

                verify(projectRepository).findById(project.getId());
                verify(projectRepository, never()).delete(project);
            }


        }

    @Nested
    class buscarComFiltro {

        @Test
        @DisplayName("Deve buscar projetos filtrando por tecnologia")
        void deveBuscarProjetosPorTecnologia() {

            // ARRANGE: prepara os dados do teste
            String technology = "Java";
            Pageable pageable = PageRequest.of(0, 10);

            Page<Project> paginaProjetos =
                    new PageImpl<>(List.of(project));

            // Simula o repositório retornando projetos filtrados
            when(projectRepository.findByTechnologiesNameIgnoreCase(
                    technology, pageable))
                    .thenReturn(paginaProjetos);

            // ACT: executa o método que estamos testando
            Page<ProjectResponseDTO> resultado =
                    projectService.buscarComFiltro(technology, pageable);

            // ASSERT: verifica o resultado
            assertNotNull(resultado);
            assertEquals(1, resultado.getTotalElements());
            assertEquals(project.getId(),
                    resultado.getContent().get(0).id());
            assertEquals(project.getTitle(),
                    resultado.getContent().get(0).title());

            // VERIFY: confirma que o repositório usou o filtro
            verify(projectRepository)
                    .findByTechnologiesNameIgnoreCase(technology, pageable);

            // Confirma que o caminho sem filtro não foi utilizado
            verify(projectRepository, never()).findAll(pageable);
        }


        @Test
        @DisplayName("Deve listar todos os projetos quando a tecnologia for null")
        void deveListarTodosQuandoTecnologiaForNull() {

            // ARRANGE: prepara a página simulada
            Pageable pageable = PageRequest.of(0, 10);

            Page<Project> paginaProjetos =
                    new PageImpl<>(List.of(project));

            // Simula o repositório retornando todos os projetos
            when(projectRepository.findAll(pageable))
                    .thenReturn(paginaProjetos);

            // ACT: executa o método sem informar tecnologia
            Page<ProjectResponseDTO> resultado =
                    projectService.buscarComFiltro(null, pageable);

            // ASSERT: verifica o resultado
            assertNotNull(resultado);
            assertEquals(1, resultado.getTotalElements());
            assertEquals(project.getId(),
                    resultado.getContent().get(0).id());

            // VERIFY: confirma que todos os projetos foram consultados
            verify(projectRepository).findAll(pageable);

            // Confirma que o filtro por tecnologia não foi utilizado
            verify(projectRepository, never())
                    .findByTechnologiesNameIgnoreCase(
                            anyString(), eq(pageable));
        }


        @Test
        @DisplayName("Deve listar todos os projetos quando a tecnologia estiver em branco")
        void deveListarTodosQuandoTecnologiaEstiverEmBranco() {

            // ARRANGE: prepara a página simulada
            Pageable pageable = PageRequest.of(0, 10);

            Page<Project> paginaProjetos =
                    new PageImpl<>(List.of(project));

            when(projectRepository.findAll(pageable))
                    .thenReturn(paginaProjetos);

            // ACT: executa o método com uma tecnologia em branco
            Page<ProjectResponseDTO> resultado =
                    projectService.buscarComFiltro("   ", pageable);

            // ASSERT: verifica o resultado
            assertNotNull(resultado);
            assertEquals(1, resultado.getTotalElements());
            assertEquals(project.getId(),
                    resultado.getContent().get(0).id());

            // VERIFY: confirma que foi feita a busca sem filtro
            verify(projectRepository).findAll(pageable);

            // Confirma que o filtro por tecnologia não foi utilizado
            verify(projectRepository, never())
                    .findByTechnologiesNameIgnoreCase(
                            anyString(), eq(pageable));
        }
    }










}