package co.com.nequi.model.product.command;

public record UpdateProductNameCommand(Long productId, String name) {}
