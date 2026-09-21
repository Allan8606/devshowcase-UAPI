package com.devshowcase.controller;


import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.dto.response.ProfileResponseDTO;
import com.devshowcase.service.ProfileService;
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
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/profiles")
    @Operation(
            summary = "Cadastrar perfil",
            description = "Cadastra um novo perfil de desenvolvedor."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Perfil cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    public ResponseEntity<ProfileResponseDTO> cadastrar(
            @RequestBody @Valid ProfileRequestDTO request) {

        ProfileResponseDTO cadastrar = profileService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cadastrar);
    }


    @GetMapping("/profiles")
    @Operation(
            summary = "Listar perfis",
            description = "Retorna todos os perfis de desenvolvedores cadastrados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfis retornados com sucesso"
            )
    })
    public ResponseEntity<List<ProfileResponseDTO>> listarTodos() {

        List<ProfileResponseDTO> profileResponseDTOS =
                profileService.listarTodos();

        return ResponseEntity.ok(profileResponseDTOS);
    }


    @GetMapping("/profiles/{id}")
    @Operation(
            summary = "Buscar perfil por ID",
            description = "Retorna os dados de um perfil específico pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil encontrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil não encontrado"
            )
    })
    public ResponseEntity<ProfileResponseDTO> buscarPorId(
            @Parameter(
                    description = "ID do perfil",
                    example = "1"
            )
            @PathVariable Long id) {

        ProfileResponseDTO profileResponseDTO =
                profileService.buscarPorId(id);

        return ResponseEntity.ok(profileResponseDTO);
    }


    @PutMapping("/profiles/{id}")
    @Operation(
            summary = "Atualizar perfil",
            description = "Atualiza os dados de um perfil existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil não encontrado"
            )
    })
    public ResponseEntity<ProfileResponseDTO> editar(
            @Parameter(
                    description = "ID do perfil",
                    example = "1"
            )
            @PathVariable Long id,

            @RequestBody @Valid ProfileRequestDTO request) {

        ProfileResponseDTO editar =
                profileService.editar(id, request);

        return ResponseEntity.ok(editar);
    }


    @DeleteMapping("/profiles/{id}")
    @Operation(
            summary = "Excluir perfil",
            description = "Exclui um perfil existente pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Perfil excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil não encontrado"
            )
    })
    public ResponseEntity<Void> deletar(
            @Parameter(
                    description = "ID do perfil",
                    example = "1"
            )
            @PathVariable Long id) {

        profileService.deletar(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
