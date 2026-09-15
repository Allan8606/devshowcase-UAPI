package com.devshowcase.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TechnologyRequestDTO(
        @NotBlank
        String name) {
}
