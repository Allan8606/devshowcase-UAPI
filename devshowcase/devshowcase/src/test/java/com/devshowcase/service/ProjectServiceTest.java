package com.devshowcase.service;

import com.devshowcase.dto.request.ProjectRequestDTO;
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







}