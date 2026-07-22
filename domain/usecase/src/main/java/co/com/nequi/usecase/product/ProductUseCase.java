package co.com.nequi.usecase.product;

import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exception.BusinessException;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.command.CreateProductCommand;
import co.com.nequi.model.product.command.UpdateProductNameCommand;
import co.com.nequi.model.product.command.UpdateStockCommand;
import co.com.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.nequi.model.enums.ErrorCode.*;

@RequiredArgsConstructor
public class ProductUseCase {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Product> createProduct(CreateProductCommand command) {
        return validateBranchExists(command.branchId())
                .then(validateProductIsUnique(command.name(), command.branchId()))
                .then(Mono.defer(() -> productRepository.save(Product.fromCommand(command))));
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(PRODUCT_NOT_FOUND, id)))
                .flatMap(product -> productRepository.deleteById(product.getId()));
    }

    public Mono<Product> updateStock(UpdateStockCommand command) {
        return productRepository.findById(command.productId())
                .switchIfEmpty(Mono.error(new BusinessException(PRODUCT_NOT_FOUND, command.productId())))
                .map(product -> product.toBuilder().stock(command.stock()).build())
                .flatMap(productRepository::save);
    }

    public Mono<Product> updateName(UpdateProductNameCommand command) {
        return productRepository.findById(command.productId())
                .switchIfEmpty(Mono.error(new BusinessException(PRODUCT_NOT_FOUND, command.productId())))
                .flatMap(product -> productRepository.findByNameAndBranchId(command.name(), product.getBranchId())
                        .flatMap(existing -> Mono.<Product>error(new BusinessException(PRODUCT_ALREADY_EXISTS)))
                        .switchIfEmpty(Mono.defer(() -> productRepository.save(product.toBuilder().name(command.name()).build()))));
    }

    public Flux<Product> getTopProductsPerBranch(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(FRANCHISE_NOT_FOUND, franchiseId)))
                .flatMapMany(f -> productRepository.getTopProductsPerBranch(franchiseId));
    }

    private Mono<Void> validateBranchExists(Long branchId) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BusinessException(BRANCH_NOT_FOUND, branchId)))
                .then();
    }

    private Mono<Void> validateProductIsUnique(String name, Long branchId) {
        return productRepository.findByNameAndBranchId(name, branchId)
                .flatMap(existing -> Mono.<Void>error(new BusinessException(PRODUCT_ALREADY_EXISTS)))
                .switchIfEmpty(Mono.empty());
    }
}
