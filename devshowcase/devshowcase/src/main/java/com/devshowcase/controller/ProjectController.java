package com.devshowcase.controller;

import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.dto.request.ProjectRequestDTO;
import com.devshowcase.dto.response.ProfileResponseDTO;
import com.devshowcase.dto.response.ProjectResponseDTO;
import com.devshowcase.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    @Operation(
            summary = "Cadastrar projeto",
            description = "Cadastra um novo projeto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Projeto criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil ou tecnologia não encontrado"
            )
    })
    public ResponseEntity<ProjectResponseDTO> cadastrar(
            @RequestBody @Valid ProjectRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(projectService.cadastrar(request));
    }


    @GetMapping("/projects")
    @Operation(
            summary = "Listar projetos",
            description = "Retorna projetos cadastrados, com opção de filtragem por tecnologia e paginação."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Projetos retornados com sucesso"
            )
    })
    public ResponseEntity<Page<ProjectResponseDTO>> buscarProjetos(
            @Parameter(description = "Nome da tecnologia utilizada no projeto", example = "Java")
            @RequestParam(required = false) String technology,
            Pageable pageable) {

        return ResponseEntity.ok(projectService.buscarComFiltro(technology, pageable));
    }


    @GetMapping("/projects/{id}")
    @Operation(
            summary = "Buscar projeto por ID",
            description = "Retorna os dados de um projeto específico pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Projeto encontrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projeto não encontrado"
            )
    })
    public ResponseEntity<ProjectResponseDTO> buscarPorId(
            @Parameter(
                    description = "ID do projeto",
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                projectService.buscarPorId(id)
        );
    }


    @PutMapping("/projects/{id}")
    @Operation(
            summary = "Atualizar projeto",
            description = "Atualiza os dados de um projeto existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Projeto atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projeto, perfil ou tecnologia não encontrado"
            )
    })
    public ResponseEntity<ProjectResponseDTO> editar(
            @Parameter(
                    description = "ID do projeto",
                    example = "1"
            )
            @PathVariable Long id,

            @RequestBody @Valid ProjectRequestDTO request) {

        return ResponseEntity.ok(
                projectService.editar(id, request)
        );
    }


    @DeleteMapping("/projects/{id}")
    @Operation(
            summary = "Excluir projeto",
            description = "Exclui um projeto existente pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Projeto excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projeto não encontrado"
            )
    })
    public ResponseEntity<Void> deletar(
            @Parameter(
                    description = "ID do projeto",
                    example = "1"
            )
            @PathVariable Long id) {

        projectService.deletar(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @PutMapping("/projects/{id}/upvote")
    @Operation(
            summary = "Adicionar upvote ao projeto",
            description = "Incrementa em uma unidade a quantidade de upvotes do projeto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Upvote adicionado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projeto não encontrado"
            )
    })
    public ResponseEntity<ProjectResponseDTO> adicionarUpvote(
            @Parameter(
                    description = "ID do projeto",
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                projectService.adicionarUpvote(id)
        );
    }
}