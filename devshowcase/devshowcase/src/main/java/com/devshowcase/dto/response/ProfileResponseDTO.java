package com.devshowcase.dto.response;

import java.util.Set;

public record ProfileResponseDTO(Long id,
                                 String name,
                                 String bio,
                                 String githubUrl,
                                 String linkedinUrl,
                                 Set<ProjectResponseDTO> projects) {
}
