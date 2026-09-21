package com.devshowcase.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TechnologyRequestDTO(
        @NotBlank(message = "O nome da tecnologia é obrigatória")
        String name) {
}
