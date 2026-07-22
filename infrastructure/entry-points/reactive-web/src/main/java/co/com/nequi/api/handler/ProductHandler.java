package co.com.nequi.api.handler;

import co.com.nequi.api.dto.request.ProductDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.dto.request.UpdateStockDTO;
import co.com.nequi.api.dto.response.ProductResponseDTO;
import co.com.nequi.api.mapper.ProductApiMapper;
import co.com.nequi.api.validator.RequestValidator;
import co.com.nequi.usecase.product.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;
    private final RequestValidator requestValidator;

    @Operation(
        summary = "Add a product to a branch",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ProductDTO.class),
            examples = @ExampleObject(value = "{\"name\": \"Pollo frito\", \"branchId\": 1}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Product created successfully",
                content = @Content(schema = @Schema(implementation = ProductResponseDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Pollo frito\", \"stock\": 0, \"branchId\": 1}"))),
            @ApiResponse(responseCode = "404", description = "Branch not found"),
            @ApiResponse(responseCode = "409", description = "Product already exists in this branch")
        }
    )
    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProductDTO.class)
                .flatMap(requestValidator::validate)
                .map(ProductApiMapper::toCommand)
                .flatMap(productUseCase::createProduct)
                .map(ProductApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Operation(
        summary = "Delete a product",
        responses = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
        }
    )
    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable("productId"));
        return productUseCase.deleteProduct(productId)
                .then(ServerResponse.noContent().build());
    }

    @Operation(
        summary = "Update product stock",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UpdateStockDTO.class),
            examples = @ExampleObject(value = "{\"stock\": 50}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully",
                content = @Content(schema = @Schema(implementation = ProductResponseDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Pollo frito\", \"stock\": 50, \"branchId\": 1}"))),
            @ApiResponse(responseCode = "404", description = "Product not found")
        }
    )
    public Mono<ServerResponse> updateProductStock(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable("productId"));
        return serverRequest.bodyToMono(UpdateStockDTO.class)
                .flatMap(requestValidator::validate)
                .map(dto -> ProductApiMapper.toUpdateStockCommand(productId, dto))
                .flatMap(productUseCase::updateStock)
                .map(ProductApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Operation(
        summary = "Get top stock product per branch for a franchise",
        responses = {
            @ApiResponse(responseCode = "200", description = "Top products retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class)),
                    examples = @ExampleObject(value = "[{\"id\": 1, \"name\": \"Pollo frito\", \"stock\": 55, \"branchId\": 1}]"))),
            @ApiResponse(responseCode = "404", description = "Franchise not found")
        }
    )
    public Mono<ServerResponse> getTopProducts(ServerRequest serverRequest) {
        Long franchiseId = Long.valueOf(serverRequest.pathVariable("franchiseId"));
        return productUseCase.getTopProductsPerBranch(franchiseId)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Operation(
        summary = "Update product name",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UpdateNameDTO.class),
            examples = @ExampleObject(value = "{\"name\": \"Pollo asado\"}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Product name updated successfully",
                content = @Content(schema = @Schema(implementation = ProductResponseDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Pollo asado\", \"stock\": 50, \"branchId\": 1}"))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Product name already exists in this branch")
        }
    )
    public Mono<ServerResponse> updateProductName(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable("productId"));
        return serverRequest.bodyToMono(UpdateNameDTO.class)
                .flatMap(requestValidator::validate)
                .map(dto -> ProductApiMapper.toUpdateNameCommand(productId, dto))
                .flatMap(productUseCase::updateName)
                .map(ProductApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }
}
