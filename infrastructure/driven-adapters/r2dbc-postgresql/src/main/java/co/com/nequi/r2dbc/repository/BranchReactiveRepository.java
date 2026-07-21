package co.com.nequi.r2dbc.repository;

import co.com.nequi.r2dbc.entity.BranchData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchReactiveRepository extends ReactiveCrudRepository<BranchData, Long> {
    Mono<BranchData> findByNameAndFranchiseId(String name, Long franchiseId);
    Flux<BranchData> findAllByFranchiseId(Long franchiseId);
}
