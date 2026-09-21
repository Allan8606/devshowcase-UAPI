package com.devshowcase.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequestDTO(

        @NotNull(message = "A nota é obrigatória")
        @Min(1)
        @Max(5)
        Integer rating,

        @NotBlank

        @NotBlank(message = "O comentario obrigatória")
        String comment,

        @NotNull(message = "O ID do projeto é obrigatória")
        Long projectId
) {
}
