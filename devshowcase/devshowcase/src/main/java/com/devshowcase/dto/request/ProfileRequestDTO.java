package com.devshowcase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProfileRequestDTO(
                                @NotBlank(message = "Nome é obrigatório")
                                String name,

                                @NotBlank(message = "Biografia é obrigatória")
                                String bio,

                                @NotBlank(message = "URL do GitHub é obrigatória")
                                @Pattern(
                                        regexp = "https?://.+",
                                        message = "URL do GitHub inválida"
                                )
                                String githubUrl,

                                @Pattern(
                                        regexp = "https?://.+",
                                        message = "URL inválida"
                                )
                                String linkedinUrl) {
}
