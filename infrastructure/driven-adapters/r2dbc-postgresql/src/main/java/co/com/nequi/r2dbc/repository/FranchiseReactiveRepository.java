package co.com.nequi.r2dbc.repository;

import co.com.nequi.r2dbc.entity.FranchiseData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranchiseReactiveRepository extends ReactiveCrudRepository<FranchiseData, Long> {
    Mono<FranchiseData> findByName(String name);
}
