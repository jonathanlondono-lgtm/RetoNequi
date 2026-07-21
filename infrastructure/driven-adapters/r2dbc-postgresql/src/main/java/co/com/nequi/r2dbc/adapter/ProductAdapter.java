package co.com.nequi.r2dbc.adapter;

import co.com.nequi.model.exception.TechnicalException;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import co.com.nequi.r2dbc.mapper.ProductMapper;
import co.com.nequi.r2dbc.repository.ProductReactiveRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.nequi.model.enums.ErrorCode.DATABASE_ERROR;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ProductAdapter implements ProductRepository {

    private final ProductReactiveRepository repository;
    private final CircuitBreaker databaseCircuitBreaker;
    private final TimeLimiter databaseTimeLimiter;
    private final Retry databaseRetry;

    @Override
    public Mono<Product> save(Product product) {
        return applyResilience(
                repository.save(ProductMapper.toData(product)),
                "saving product"
        ).map(ProductMapper::toModel);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return applyResilience(
                repository.findById(id),
                "finding product by id"
        ).map(ProductMapper::toModel);
    }

    @Override
    public Mono<Product> findByNameAndBranchId(String name, Long branchId) {
        return applyResilience(
                repository.findByNameAndBranchId(name, branchId),
                "finding product by name and branchId"
        ).map(ProductMapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return applyResilience(
                repository.deleteById(id).then(Mono.just(true)),
                "deleting product"
        ).then();
    }

    @Override
    public Flux<Product> getTopProductsPerBranch(Long franchiseId) {
        return applyResilience(
                repository.getTopProductsPerBranch(franchiseId)
                        .map(ProductMapper::toModel),
                "finding top products per branch"
        );
    }

    private <T> Flux<T> applyResilience(Flux<T> publisher, String action) {
        return publisher
                .transformDeferred(TimeLimiterOperator.of(databaseTimeLimiter))
                .transformDeferred(CircuitBreakerOperator.of(databaseCircuitBreaker))
                .transformDeferred(RetryOperator.of(databaseRetry))
                .doOnComplete(() -> log.info("Successfully completed action: {}", action))
                .doOnError(e -> log.error("Database error while {}: ", action, e))
                .onErrorMap(throwable -> new TechnicalException(DATABASE_ERROR));
    }

    private <T> Mono<T> applyResilience(Mono<T> publisher, String action) {
        return publisher
                .transformDeferred(TimeLimiterOperator.of(databaseTimeLimiter))
                .transformDeferred(CircuitBreakerOperator.of(databaseCircuitBreaker))
                .transformDeferred(RetryOperator.of(databaseRetry))
                .doOnSuccess(result -> log.info("Successfully completed action: {}", action))
                .doOnError(e -> log.error("Database error while {}: ", action, e))
                .onErrorMap(throwable -> new TechnicalException(DATABASE_ERROR));
    }
}
