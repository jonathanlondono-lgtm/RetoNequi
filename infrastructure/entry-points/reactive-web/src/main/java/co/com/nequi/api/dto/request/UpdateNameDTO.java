package co.com.nequi.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameDTO(@NotBlank String name) {}
