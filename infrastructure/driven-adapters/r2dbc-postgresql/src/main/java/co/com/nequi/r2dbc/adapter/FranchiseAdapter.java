package co.com.nequi.r2dbc.adapter;

import co.com.nequi.model.exception.TechnicalException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.r2dbc.mapper.FranchiseMapper;
import co.com.nequi.r2dbc.repository.FranchiseReactiveRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.nequi.model.enums.ErrorCode.DATABASE_ERROR;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FranchiseAdapter implements FranchiseRepository {

    private final FranchiseReactiveRepository repository;
    private final CircuitBreaker databaseCircuitBreaker;
    private final TimeLimiter databaseTimeLimiter;
    private final Retry databaseRetry;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return applyResilience(
                repository.save(FranchiseMapper.toData(franchise)),
                "saving franchise"
        ).map(FranchiseMapper::toModel);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return applyResilience(
                repository.findById(id),
                "finding franchise by id"
        ).map(FranchiseMapper::toModel);
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        return applyResilience(
                repository.findByName(name),
                "finding franchise by name"
        ).map(FranchiseMapper::toModel);
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