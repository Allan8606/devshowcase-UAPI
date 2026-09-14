package com.devshowcase.dto.request;

import java.util.Set;

public record ProjectRequestDTO(String title,
                                String description,
                                String githubUrl,
                                String projectUrl,
                                Long profileId,
                                Set<Long> technologyIds) {
}
