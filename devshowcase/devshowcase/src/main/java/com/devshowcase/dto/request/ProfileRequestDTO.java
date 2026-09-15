package com.devshowcase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProfileRequestDTO(
                                @NotBlank
                                String name,

                                @NotBlank
                                String bio,

                                @NotBlank
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
