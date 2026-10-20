package com.devshowcase.service;

import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.entity.Profile;
import com.devshowcase.exception.ResourceAlreadyExistsException;
import com.devshowcase.exception.ResourceNotFoundException;
import com.devshowcase.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Captor
    private ArgumentCaptor<Profile> profileArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> idArgumentCaptor;


    @Nested
    class cadastrar {

        @Test
        @DisplayName("Deve criar um Profile com sucesso.")
        void deveCadastrar(){

            //Arrange
            var profile = new Profile(
                    "João",
                    "Dev",
                    "github.com/joao",
                    "linkedin.com/in/joao");


            doReturn(profile).when(profileRepository).save(profileArgumentCaptor.capture());; // não é uma boa pratica  usar o any.

            var input = new ProfileRequestDTO(
                    "username",
                    "biografia",
                    "github.com.br",
                    "www.linkedin.com");

            //ACT
           var output =  profileService.cadastrar(input);


            //Assert
            assertNotNull(output);
            var profileCapturado = profileArgumentCaptor.getValue();

            //1 parametro é o que eu espero, e o segundo é o que de fato retorna.
            assertEquals(input.name(), profileCapturado.getName());
            assertEquals(input.bio(), profileCapturado.getBio());
            assertEquals(input.githubUrl(), profileCapturado.getGithubUrl());
            assertEquals(input.linkedinUrl(), profileCapturado.getLinkedinUrl());
        }

        @Test
        @DisplayName("Deve mostrar exceção caso um erro aconteça")
        void devePorExcecaoQuandoErroAcontecer(){

            //Arrange
            doThrow(new RuntimeException()).when(profileRepository).save(any(Profile.class)); // não é uma boa pratica  usar o any.

            var input = new ProfileRequestDTO(
                    "username",
                    "biografia",
                    "github.com.br",
                    "www.linkedin.com");

            //ACT Assert
            assertThrows(RuntimeException.class, () -> profileService.cadastrar(input));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o perfil já existir")
        void deveLancarExcecaoQuandoProfileJaExistir() {

            // Arrange
            var input = new ProfileRequestDTO(
                    "username",
                    "biografia",
                    "github.com.br",
                    "www.linkedin.com"
            );

            doReturn(true)
                    .when(profileRepository)
                    .existsByGithubUrl(input.githubUrl());

            // Act + Assert
            assertThrows(
                    ResourceAlreadyExistsException.class,
                    () -> profileService.cadastrar(input)
            );

            // Confirma que o save não foi chamado
            verify(profileRepository, never()).save(any(Profile.class));
        }

    }

    @Nested
    class buscarPorId{

        @Test
        @DisplayName("Deve buscar um Profile pelo seu id e Optional tiver conteudo")
        void deveBuscarPorIdOptionalComnaovazio(){

            Long id = 1L;

            Profile profile = new Profile(
                    "João",
                    "Dev",
                    "github.com/joao",
                    "linkedin.com/in/joao");


            profile.setId(id);


            doReturn(Optional.of(profile))
                    .when(profileRepository)
                    .findById(idArgumentCaptor.capture());

            var output = profileService.buscarPorId(id);

            assertNotNull(output);
            assertEquals(profile.getId(), idArgumentCaptor.getValue());
        }


        @Test
        @DisplayName("Deve buscar um Profile pelo seu id, e quando Optional estiver vazio")
        void deveBuscarPorIdQuandoOptionalVazio(){

            Long id = 1L;

            doReturn(Optional.empty())
                    .when(profileRepository)
                    .findById(idArgumentCaptor.capture());

            assertThrows(RuntimeException.class, () -> profileService.buscarPorId(id));
            assertEquals(id, idArgumentCaptor.getValue());
        }

    }

    @Nested
    class listarProfiles{

        @Test
        @DisplayName("Deve listar todos os profiles cadastrados.")
        void deveListarTodosProfiles(){


            Long id = 1L;

            Profile profile = new Profile(
                    "João",
                    "Dev",
                    "github.com/joao",
                    "linkedin.com/in/joao");


            profile.setId(id);

            var profileList = List.of(profile);

            doReturn(profileList)
                    .when(profileRepository)
                    .findAll();


            var output = profileService.listarTodos();

            assertNotNull(output);
            assertEquals(profileList.size(), output.size());

        }


    }

    @Nested
    class deletar{
        @Test
        @DisplayName("Deve deletar um Profile existente.")
        void deveDeletarUmProfile() {

            Long id = 1L;

            // Criamos um perfil fictício para o teste.
            Profile profile = new Profile(
                    "username",
                    "Desenvolvedor JAVA",
                    "github.com",
                    "linkedin.com"
            );

            // Definimos manualmente o ID do perfil,
            profile.setId(id);

            // Simulamos uma busca bem-sucedida no repositório.
            // Quando findById() for chamado, retornará o perfil criado.
            // O ArgumentCaptor captura o ID recebido pelo método.
            doReturn(Optional.of(profile))
                    .when(profileRepository)
                    .findById(idArgumentCaptor.capture());



            // Chamamos o método deletar() do ProfileService,
            // passando o ID do perfil que queremos excluir.
            profileService.deletar(id);


            // Verificamos se o ID capturado é igual ao ID esperado.
            assertEquals(id, idArgumentCaptor.getValue());

            // Verificamos se findById() foi chamado exatamente uma vez.
            verify(profileRepository, times(1))
                    .findById(id);

            // Verifica se o repositório recebeu um perfil para exclusão
            verify(profileRepository, times(1))
                    .delete(profileArgumentCaptor.capture());

            // Verificamos se delete() recebeu o perfil correto,
            // exatamente uma vez.
            verify(profileRepository, times(1))
                    .delete(profile);

            // Recupera o perfil que foi enviado para exclusão
            Profile profileDeletado = profileArgumentCaptor.getValue();

            // Verifica se o perfil enviado tem o ID esperado
            assertEquals(id, profileDeletado.getId());
        }

        @Test
        @DisplayName("Não deve excluir o profile quando o usuário NÃO existe.")
        void quandoProfiliNaoExiste() {

            Long id = 1L;

            doReturn(Optional.empty())
                    .when(profileRepository)
                    .findById(id);


            // a exceção ResourceNotFoundException esperada.
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> profileService.deletar(id)
            );



            // Confirmamos que o serviço consultou o repositório uma única vez.
            verify(profileRepository, times(1))
                    .findById(id);

            // Confirmamos que o método delete() nunca foi chamado.
            // any(Profile.class) aceita qualquer objeto do tipo Profile.
            verify(profileRepository, never())
                    .delete(any(Profile.class));


        }

    }

    @Nested
    class editar{

        @Test
        @DisplayName("Deve editar um perfil")
        void deveEditarUmPerfil() {

            // ARRANGE — Preparação do teste

            // Definimos o ID do perfil que será editado.
            Long id = 1L;

            // Criamos um perfil fictício que já existe no banco de dados.
            Profile profile = new Profile(
                    "João",
                    "Desenvolvedor Java",
                    "github.com/joao",
                    "linkedin.com/in/joao"
            );

            profile.setId(id);

            // Criamos o DTO com os novos dados que serão enviados
            // para atualizar o perfil.
            ProfileRequestDTO profileRequestDTO = new ProfileRequestDTO(
                    "João Silva",
                    "Desenvolvedor Back-end",
                    "github.com/joao",
                    "linkedin.com/in/joao"
            );

            // Configuramos o mock para retornar o perfil existente
            // quando o service procurar pelo ID informado.
            doReturn(Optional.of(profile))
                    .when(profileRepository)
                    .findById(idArgumentCaptor.capture());

            // Configuramos o mock para capturar o perfil enviado ao save()
            // e devolver esse mesmo perfil como resultado da operação.
            doReturn(profile)
                    .when(profileRepository)
                    .save(profileArgumentCaptor.capture());

            // ACT — Executamos o método que queremos testar.
            var output = profileService.editar(id, profileRequestDTO);



            // ASSERT — Verificamos os resultados.

            // Verifica se o resultado retornado não é nulo.
            assertNotNull(output);

            // Recuperamos o perfil enviado ao método save().
            var profileEditado = profileArgumentCaptor.getValue();

            // Verifica se o nome foi atualizado corretamente.
            assertEquals(profileRequestDTO.name(), profileEditado.getName());

            // Verifica se a biografia foi atualizada corretamente.
            assertEquals(profileRequestDTO.bio(), profileEditado.getBio());

            // Verifica se o GitHub continua com o valor esperado.
            assertEquals(profileRequestDTO.githubUrl(), profileEditado.getGithubUrl());

            // Verifica se o LinkedIn continua com o valor esperado.
            assertEquals(profileRequestDTO.linkedinUrl(), profileEditado.getLinkedinUrl());

            // Verifica se o perfil editado mantém o ID original.
            assertEquals(id, profileEditado.getId());

            // Verifica se o repositório foi consultado uma única vez
            // com o ID informado.
            verify(profileRepository, times(1)).findById(id);

            // Verifica se o método save() foi chamado uma única vez
            // com o perfil capturado.
            verify(profileRepository, times(1)).save(profileEditado);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o perfil não existir")
        void deveLancarExcecaoQuandoProfileNaoExistir() {

            // ARRANGE

            Long id = 1L;

            ProfileRequestDTO request = new ProfileRequestDTO(
                    "João Silva",
                    "Desenvolvedor Back-end",
                    "github.com/joao",
                    "linkedin.com/in/joao"
            );

            // Simulamos um ID que não foi encontrado.
            doReturn(Optional.empty())
                    .when(profileRepository)
                    .findById(id);

            // ACT + ASSERT

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> profileService.editar(id, request)
            );

            // O perfil não foi encontrado, então não deve ser salvo.
            verify(profileRepository, never()).save(any(Profile.class));

            // A verificação de duplicidade nem deve ser executada.
            verify(profileRepository, never())
                    .existsByGithubUrl(anyString());
        }


        @Test
        @DisplayName("Deve lançar exceção quando o GitHub pertencer a outro perfil")
        void deveLancarExcecaoQuandoGithubJaExistir() {

            // ARRANGE

            Long id = 1L;

            // Perfil que estamos tentando editar.
            Profile profile = new Profile(
                    "João",
                    "Desenvolvedor Java",
                    "github.com/joao",
                    "linkedin.com/in/joao"
            );
            profile.setId(id);

            // João tenta utilizar o GitHub de outro perfil.
            ProfileRequestDTO request = new ProfileRequestDTO(
                    "João Silva",
                    "Desenvolvedor Back-end",
                    "github.com/maria",
                    "linkedin.com/in/joao"
            );

            doReturn(Optional.of(profile))
                    .when(profileRepository)
                    .findById(id);

            // Simulamos que o GitHub solicitado já está cadastrado.
            doReturn(true)
                    .when(profileRepository)
                    .existsByGithubUrl(request.githubUrl());

            // ACT + ASSERT

            assertThrows(
                    ResourceAlreadyExistsException.class,
                    () -> profileService.editar(id, request)
            );

            // O cadastro duplicado deve ser interrompido antes do save.
            verify(profileRepository, never()).save(any(Profile.class));

            // Confirmamos que a URL foi verificada.
            verify(profileRepository, times(1))
                    .existsByGithubUrl(request.githubUrl());
        }


        @Test
        @DisplayName("Deve permitir alterar o GitHub para uma URL disponível")
        void deveEditarComNovoGithubDisponivel() {

            // ARRANGE

            Long id = 1L;

            Profile profile = new Profile(
                    "João",
                    "Desenvolvedor Java",
                    "github.com/joao",
                    "linkedin.com/in/joao"
            );
            profile.setId(id);

            // Agora João deseja utilizar uma URL diferente.
            ProfileRequestDTO request = new ProfileRequestDTO(
                    "João",
                    "Desenvolvedor Java",
                    "github.com/joao-dev",
                    "linkedin.com/in/joao"
            );

            doReturn(Optional.of(profile))
                    .when(profileRepository)
                    .findById(id);

            // Simulamos que a nova URL ainda não está cadastrada.
            doReturn(false)
                    .when(profileRepository)
                    .existsByGithubUrl(request.githubUrl());

            doReturn(profile)
                    .when(profileRepository)
                    .save(profileArgumentCaptor.capture());

            // ACT

            var output = profileService.editar(id, request);

            // ASSERT

            assertNotNull(output);

            var profileEditado = profileArgumentCaptor.getValue();

            assertEquals(id, profileEditado.getId());
            assertEquals(request.githubUrl(), profileEditado.getGithubUrl());
            assertEquals("github.com/joao-dev", output.githubUrl());

            // O perfil foi salvo uma única vez.
            verify(profileRepository, times(1)).save(profileEditado);

            // A nova URL foi verificada.
            verify(profileRepository, times(1))
                    .existsByGithubUrl(request.githubUrl());
        }
    }

}