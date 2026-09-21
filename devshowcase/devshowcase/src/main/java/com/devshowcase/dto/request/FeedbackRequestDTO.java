package com.devshowcase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequestDTO(
        @NotBlank(message = "O comentario obrigatória")
        String comment,

        @NotNull(message = "O ID do projeto é obrigatória")
        Long projectId
) {
}
