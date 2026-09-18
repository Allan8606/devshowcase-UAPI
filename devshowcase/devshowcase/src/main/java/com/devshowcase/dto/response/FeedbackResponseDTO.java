package com.devshowcase.dto.response;

public record FeedbackResponseDTO(
        Long id,
        Integer rating,
        String comment,
        Long projectId
) {
}