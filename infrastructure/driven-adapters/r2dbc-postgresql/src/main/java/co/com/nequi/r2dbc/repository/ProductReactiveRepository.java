package co.com.nequi.r2dbc.repository;

import co.com.nequi.r2dbc.entity.ProductData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductData, Long> {
    Mono<ProductData> findByNameAndBranchId(String name, Long branchId);

    @Query("""
        SELECT p.id, p.name, p.stock, p.branch_id
        FROM product p
        JOIN branch b ON b.id = p.branch_id AND b.franchise_id = :franchiseId
        WHERE (p.branch_id, p.stock) IN (
            SELECT p2.branch_id, MAX(p2.stock)
            FROM product p2
            GROUP BY p2.branch_id
        )
    """)
    Flux<ProductData> getTopProductsPerBranch(Long franchiseId);
}
