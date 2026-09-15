package com.devshowcase.dto.response;

public record FeedbackResponseDTO(Long id,
                                  String comment,
                                  Long projectId) {
}
