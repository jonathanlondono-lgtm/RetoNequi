package co.com.nequi.r2dbc.adapter;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exception.TechnicalException;
import co.com.nequi.r2dbc.mapper.BranchMapper;
import co.com.nequi.r2dbc.repository.BranchReactiveRepository;
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
public class BranchAdapter implements BranchRepository {

    private final BranchReactiveRepository repository;
    private final CircuitBreaker databaseCircuitBreaker;
    private final TimeLimiter databaseTimeLimiter;
    private final Retry databaseRetry;

    @Override
    public Mono<Branch> save(Branch branch) {
        return applyResilience(
                repository.save(BranchMapper.toData(branch)),
                "saving branch"
        ).map(BranchMapper::toModel);
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return applyResilience(
                repository.findById(id),
                "finding branch by id"
        ).map(BranchMapper::toModel);
    }

    @Override
    public Mono<Branch> findByNameAndFranchiseId(String name, Long franchiseId) {
        return applyResilience(
                repository.findByNameAndFranchiseId(name, franchiseId),
                "finding branch by name and franchiseId"
        ).map(BranchMapper::toModel);
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
