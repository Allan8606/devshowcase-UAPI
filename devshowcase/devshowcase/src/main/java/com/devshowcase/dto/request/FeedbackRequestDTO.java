package com.devshowcase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequestDTO(
        @NotBlank
        String comment,

        @NotNull
        Long projectId) {
}
