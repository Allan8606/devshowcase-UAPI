package com.devshowcase.dto.response;

import java.util.Set;

public record ProjectResponseDTO(Long id,
                                 String title,
                                 String description,
                                 String githubUrl,
                                 String projectUrl,
                                 Long profileId,
                                 Set<Long> technologyIds) {
}
