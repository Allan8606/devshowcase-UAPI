package com.devshowcase.dto.request;

public record ProfileRequestDTO(String name,
                                String bio,
                                String githubUrl,
                                String linkedinUrl) {
}
