package co.com.nequi.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateStockDTO(@NotNull @Min(0) Integer stock) {}
