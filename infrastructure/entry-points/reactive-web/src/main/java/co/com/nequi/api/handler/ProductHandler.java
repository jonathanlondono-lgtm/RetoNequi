package co.com.nequi.api.handler;

import co.com.nequi.api.dto.request.ProductDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.dto.request.UpdateStockDTO;
import co.com.nequi.api.mapper.ProductApiMapper;
import co.com.nequi.api.validator.RequestValidator;
import co.com.nequi.usecase.product.ProductUseCase;
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

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProductDTO.class)
                .flatMap(requestValidator::validate)
                .map(ProductApiMapper::toCommand)
                .flatMap(productUseCase::createProduct)
                .map(ProductApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable("productId"));
        return productUseCase.deleteProduct(productId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable("productId"));
        return serverRequest.bodyToMono(UpdateStockDTO.class)
                .flatMap(requestValidator::validate)
                .map(dto -> ProductApiMapper.toUpdateStockCommand(productId, dto))
                .flatMap(productUseCase::updateStock)
                .map(ProductApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getTopProducts(ServerRequest serverRequest) {
        Long franchiseId = Long.valueOf(serverRequest.pathVariable("franchiseId"));
        return productUseCase.getTopProductsPerBranch(franchiseId)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

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
