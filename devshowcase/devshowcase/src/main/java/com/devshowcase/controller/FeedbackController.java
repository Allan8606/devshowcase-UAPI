package com.devshowcase.controller;


import com.devshowcase.dto.request.FeedbackRequestDTO;
import com.devshowcase.dto.request.TechnologyRequestDTO;
import com.devshowcase.dto.response.FeedbackResponseDTO;
import com.devshowcase.dto.response.TechnologyResponseDTO;
import com.devshowcase.service.FeedbackService;
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
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/projects/{id}/feedbacks")
    @Operation(
            summary = "Cadastrar feedback",
            description = "Cadastra um novo feedback para um projeto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Feedback cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projeto não encontrado"
            )
    })
    public ResponseEntity<FeedbackResponseDTO> cadastrar(
            @Parameter(
                    description = "ID do projeto que receberá o feedback",
                    example = "1"
            )
            @PathVariable Long id,
            @RequestBody @Valid FeedbackRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedbackService.cadastrar(id, request));
    }


    @Operation(
            summary = "Listar feedbacks",
            description = "Retorna todos os feedbacks cadastrados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Feedbacks retornados com sucesso"
            )
    })
    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackResponseDTO>> listarTodos() {
        return ResponseEntity.ok(feedbackService.listarTodos());
    }


    @Operation(
            summary = "Buscar feedback por ID",
            description = "Retorna os dados de um feedback específico pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Feedback encontrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Feedback não encontrado"
            )
    })
    @GetMapping("/feedbacks/{id}")
    public ResponseEntity<FeedbackResponseDTO> buscarPorId(
            @Parameter(
                    description = "ID do feedback",
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(feedbackService.buscarPorId(id));
    }


    @PutMapping("/feedbacks/{id}")
    @Operation(
            summary = "Atualizar feedback",
            description = "Atualiza a nota e o comentário de um feedback existente e recalcula a nota média do projeto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Feedback atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Feedback não encontrado"
            )
    })
    public ResponseEntity<FeedbackResponseDTO> editar(
            @Parameter(
                    description = "ID do feedback",
                    example = "1"
            )
            @PathVariable Long id,
            @RequestBody @Valid FeedbackRequestDTO request) {

        return ResponseEntity.ok(
                feedbackService.editar(id, request)
        );
    }


    @DeleteMapping("/feedbacks/{id}")
    @Operation(
            summary = "Excluir feedback",
            description = "Exclui um feedback existente pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Feedback excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Feedback não encontrado"
            )
    })
    public ResponseEntity<Void> deletar(
            @Parameter(
                    description = "ID do feedback",
                    example = "1"
            )
            @PathVariable Long id) {

        feedbackService.deletar(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
