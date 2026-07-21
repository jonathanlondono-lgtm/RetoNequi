package co.com.nequi.model.product.command;

public record UpdateStockCommand(Long productId, Integer stock) {}
