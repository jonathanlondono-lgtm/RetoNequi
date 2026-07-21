package co.com.nequi.model.product.gateways;

import co.com.nequi.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);
    Mono<Product> findByNameAndBranchId(String name, Long branchId);
    Mono<Void> deleteById(Long id);
    Flux<Product> getTopProductsPerBranch(Long franchiseId);
}
