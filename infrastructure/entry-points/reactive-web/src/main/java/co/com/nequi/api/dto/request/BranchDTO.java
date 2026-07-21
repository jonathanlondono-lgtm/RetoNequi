package co.com.nequi.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BranchDTO(
        @NotBlank String name,
        @NotNull Long franchiseId) {}
