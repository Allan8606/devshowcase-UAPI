package com.devshowcase.controller;

import com.devshowcase.dto.request.ProjectRequestDTO;
import com.devshowcase.dto.request.TechnologyRequestDTO;
import com.devshowcase.dto.response.ProjectResponseDTO;
import com.devshowcase.dto.response.TechnologyResponseDTO;
import com.devshowcase.service.TechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @Operation(
            summary = "Cadastrar tecnologia",
            description = "Cadastra uma nova tecnologia."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tecnologia cadastrada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping("/technologies")
    public ResponseEntity<TechnologyResponseDTO> cadastrar(
            @RequestBody @Valid TechnologyRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(technologyService.cadastrar(request));
    }


    @Operation(
            summary = "Listar tecnologias",
            description = "Retorna todas as tecnologias cadastradas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tecnologias retornadas com sucesso"
            )
    })
    @GetMapping("/technologies")
    public ResponseEntity<List<TechnologyResponseDTO>> listarTodos() {
        return ResponseEntity.ok(technologyService.listarTodos());
    }


    @Operation(
            summary = "Buscar tecnologia por ID",
            description = "Retorna os dados de uma tecnologia específica pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tecnologia encontrada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tecnologia não encontrada"
            )
    })
    @GetMapping("/technologies/{id}")
    public ResponseEntity<TechnologyResponseDTO> buscarPorId(
            @Parameter(
                    description = "ID da tecnologia",
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(technologyService.buscarPorid(id));
    }


    @Operation(
            summary = "Atualizar tecnologia",
            description = "Atualiza os dados de uma tecnologia existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tecnologia atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tecnologia não encontrada"
            )
    })
    @PutMapping("/technologies/{id}")
    public ResponseEntity<TechnologyResponseDTO> editar(
            @Parameter(
                    description = "ID da tecnologia",
                    example = "1"
            )
            @PathVariable Long id,
            @RequestBody @Valid TechnologyRequestDTO request) {

        return ResponseEntity.ok(
                technologyService.editar(id, request)
        );
    }


    @Operation(
            summary = "Excluir tecnologia",
            description = "Exclui uma tecnologia existente pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Tecnologia excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tecnologia não encontrada"
            )
    })
    @DeleteMapping("/technologies/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(
                    description = "ID da tecnologia",
                    example = "1"
            )
            @PathVariable Long id) {

        technologyService.deletar(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}