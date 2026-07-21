package co.com.nequi.api.mapper;

import co.com.nequi.api.dto.request.ProductDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.dto.request.UpdateStockDTO;
import co.com.nequi.api.dto.response.ProductResponseDTO;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.command.CreateProductCommand;
import co.com.nequi.model.product.command.UpdateProductNameCommand;
import co.com.nequi.model.product.command.UpdateStockCommand;

public final class ProductApiMapper {
    private ProductApiMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CreateProductCommand toCommand(ProductDTO request) {
        return new CreateProductCommand(request.name(), request.branchId());
    }

    public static ProductResponseDTO toResponse(Product product) {
        return new ProductResponseDTO(product.getId(), product.getName(), product.getStock(), product.getBranchId());
    }

    public static UpdateStockCommand toUpdateStockCommand(Long productId, UpdateStockDTO dto) {
        return new UpdateStockCommand(productId, dto.stock());
    }

    public static UpdateProductNameCommand toUpdateNameCommand(Long productId, UpdateNameDTO dto) {
        return new UpdateProductNameCommand(productId, dto.name());
    }
}
