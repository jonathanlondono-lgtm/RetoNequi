package co.com.nequi.model.branch.gateways;

import co.com.nequi.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Branch> save(Branch branch);
    Mono<Branch> findById(Long id);
    Mono<Branch> findByNameAndFranchiseId(String name, Long franchiseId);
}
