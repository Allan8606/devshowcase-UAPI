package com.devshowcase.service;

import com.devshowcase.dto.request.TechnologyRequestDTO;
import com.devshowcase.dto.response.TechnologyResponseDTO;
import com.devshowcase.entity.Technology;
import com.devshowcase.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyServiceTest {

    @Mock
    private TechnologyRepository technologyRepository;

    @InjectMocks
    private TechnologyService technologyService;

    @Captor
    private ArgumentCaptor<Technology> technologyArgumentCaptor;

    private Technology technology;

    private TechnologyRequestDTO criarRequest () {
        return new TechnologyRequestDTO("Java");
    }

    @BeforeEach
    void setup(){
        technology = new Technology("Java");
        technology.setId(1L);
    }



    @Nested
    class cadastrar{

        @Test
        @DisplayName("Deve cadastrar uma tecnologia")
        void deveCadastrarUmaTecnologia() {
            var request = criarRequest();

            when(technologyRepository.save(any(Technology.class))).thenReturn(technology);

            TechnologyResponseDTO cadastrarService = technologyService.cadastrar(request);

            //Dados retornados
            assertNotNull(cadastrarService);
            assertEquals(technology.getId(), cadastrarService.id());
            assertEquals(request.name(), cadastrarService.name());

            verify(technologyRepository).save(technologyArgumentCaptor.capture());

            Technology tecnologiaEnviada  = technologyArgumentCaptor.getValue();

            assertEquals(request.name(), tecnologiaEnviada.getName());
        }
    }

    @Nested
    class listarTodos{
        @Test
        @DisplayName("Deve listar todas as tecnologias cadastradas.")
        void deveListarTodastecnologias() {

            List<Technology> technologyList = List.of(technology);

            when(technologyRepository.findAll()).thenReturn(technologyList);

            List<TechnologyResponseDTO> listarService = technologyService.listarTodos();

            assertNotNull(listarService);
            assertEquals(technologyList.size(), listarService.size());
            assertEquals(technology.getId(), listarService.get(0).id());
            assertEquals(technology.getName(), listarService.get(0).name());

            verify(technologyRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia quando não tiver nenhuma tecnologia cadastrada.")
        void deveRetornarUmaListaVaziaQuandoNaoTiverTecnologiasCadastradas() {


            when(technologyRepository.findAll()).thenReturn(List.of());


            List<TechnologyResponseDTO> listarService = technologyService.listarTodos();

            assertNotNull(listarService);
            assertTrue(listarService.isEmpty());

            verify(technologyRepository).findAll();
        }
    }

    @Nested
    class buscarPorid{

        @Test
        @DisplayName("Deve buscar uma tecnologia pelo id")
        void deveBuscarUmaTecnologiaPeloId() {

            when(technologyRepository.findById(technology.getId())).thenReturn(Optional.of(technology));

            TechnologyResponseDTO buscarPorIdService = technologyService.buscarPorid(technology.getId());

            assertNotNull(buscarPorIdService);
            assertEquals(technology.getId(), buscarPorIdService.id());
            assertEquals(technology.getName(), buscarPorIdService.name());

            verify(technologyRepository).findById(technology.getId());
        }

        @Test
        @DisplayName("Deve retornar uma exceção quando não encontrar uma tecnologia pelo ID")
        void deveRetornarUmaexcecaoQuandoNaoEncontrarUmaTecnologiaPeloId() {

            when(technologyRepository.findById(technology.getId())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, ()-> technologyService.buscarPorid(technology.getId()));

            verify(technologyRepository).findById(technology.getId());
        }
    }

    @Nested
    class editar{

        @Test
        @DisplayName("Deve editar uma tecnologia cadastrada")
        void deveEditarUmaTecnologia() {
            TechnologyRequestDTO request = criarRequest();

            when(technologyRepository.findById(technology.getId())).thenReturn(Optional.of(technology));
            when(technologyRepository.save(any(Technology.class))).thenReturn(technology);

            TechnologyResponseDTO editarService = technologyService.editar(technology.getId(), request);

            assertNotNull(editarService);
            assertEquals(request.name(), editarService.name());
            assertEquals(technology.getId(), editarService.id());

            verify(technologyRepository).findById(technology.getId());
            verify(technologyRepository).save(technologyArgumentCaptor.capture());

            Technology tecnologiaEnviada = technologyArgumentCaptor.getValue();

            assertEquals(request.name(), tecnologiaEnviada.getName());
        }

        @Test
        @DisplayName("Deve retornar uma exceção quando não encontrar uma tecnologia pelo ID")
        void deveRetornarUmaexcecaoQuandoNaoEncontrarUmaTecnologiaPeloId() {
            TechnologyRequestDTO request = criarRequest();

            when(technologyRepository.findById(technology.getId())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, ()-> technologyService.editar(technology.getId(), request));

            verify(technologyRepository).findById(technology.getId());
            verify(technologyRepository, never()).save(any(Technology.class));
        }
    }

    @Nested
    class deletar{

        @Test
        @DisplayName("Deve deletar uma tecnologia")
        void deveDeletarUmaTecnologia() {
            when(technologyRepository.findById(technology.getId())).thenReturn(Optional.of(technology));

            technologyService.deletar(technology.getId());

            verify(technologyRepository).findById(technology.getId());
            verify(technologyRepository).delete(technology);
        }

        @Test
        @DisplayName("Deve retornar uma exceção quando não encontrar uma tecnologia pelo ID")
        void deveRetornarUmaexcecaoQuandoNaoEncontrarUmaTecnologiaPeloId() {
            when(technologyRepository.findById(technology.getId())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, ()-> technologyService.deletar(technology.getId()));

            verify(technologyRepository).findById(technology.getId());
            verify(technologyRepository, never()).delete(any(Technology.class));
        }


    }










}