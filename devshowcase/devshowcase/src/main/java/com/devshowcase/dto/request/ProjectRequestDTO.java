package com.devshowcase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record ProjectRequestDTO(
        // Esse campo precisa existir e não pode estar vazio ou conter apenas espaços
        @NotBlank
        String title,

        @NotBlank
        String description,

        // Não pode ser vazio e precisa ter formato de URL
        @NotBlank
        @Pattern(
                regexp = "https?://.+",
                message = "URL do GitHub inválida"
        )
        String githubUrl,

        // Opcional, mas se informado precisa ter formato de URL
        @Pattern(
                regexp = "https?://.+",
                message = "URL inválida"
        )
        String projectUrl,

        // O ID do perfil é obrigatório
        @NotNull
        Long profileId,

        // É necessário informar pelo menos uma tecnologia
        @NotEmpty
        Set<Long> technologyIds){
}
