package co.com.nequi.api.exception.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponseDTO(String code, String message, String path, LocalDateTime timestamp) {}

