package co.com.nequi.usecase.product;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exception.BusinessException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.command.CreateProductCommand;
import co.com.nequi.model.product.command.UpdateProductNameCommand;
import co.com.nequi.model.product.command.UpdateStockCommand;
import co.com.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private ProductUseCase useCase;

    @Test
    @DisplayName("should create product successfully")
    void createProduct() {
        CreateProductCommand command = new CreateProductCommand("Product 1", 1L);

        Product product = Product.builder().id(1L).name("Product 1").stock(0).branchId(1L).build();
        Branch branch = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();


        when(branchRepository.findById(1L)).thenReturn(Mono.just(branch));
        when(productRepository.findByNameAndBranchId("Product 1", 1L)).thenReturn(Mono.empty());
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Product> result = useCase.createProduct(command);

        StepVerifier.create(result)
                .expectNext(product)
                .verifyComplete();

        verify(branchRepository).findById(1L);
        verify(productRepository).findByNameAndBranchId("Product 1", 1L);
    }

    @Test
    @DisplayName("should throw exception when product already exists")
    void createProductAlreadyExists() {
        CreateProductCommand command = new CreateProductCommand("Product 1", 1L);

        Branch branch = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();
        Product existing = Product.builder().id(1L).name("Product 1").stock(0).branchId(1L).build();

        when(branchRepository.findById(1L)).thenReturn(Mono.just(branch));
        when(productRepository.findByNameAndBranchId("Product 1", 1L)).thenReturn(Mono.just(existing));

        Mono<Product> result = useCase.createProduct(command);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();

        verify(branchRepository).findById(1L);
        verify(productRepository).findByNameAndBranchId("Product 1", 1L);
    }

    @Test
    @DisplayName("should throw exception when branch not found")
    void createProductBranchNotFound() {
        CreateProductCommand command = new CreateProductCommand("Product 1", 1L);

        when(branchRepository.findById(1L)).thenReturn(Mono.empty());
        lenient().when(productRepository.findByNameAndBranchId("Product 1", 1L)).thenReturn(Mono.empty());

        Mono<Product> result = useCase.createProduct(command);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();

        verify(branchRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("should delete product successfully")
    void deleteProduct() {
        Product product = Product.builder().id(1L).name("Product 1").stock(0).branchId(1L).build();

        when(productRepository.findById(1L)).thenReturn(Mono.just(product));
        when(productRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.deleteProduct(1L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    @DisplayName("should update product stock successfully")
    void updateStock() {
        UpdateStockCommand command = new UpdateStockCommand(1L, 10);

        Product existing = Product.builder().id(1L).name("Product 1").stock(0).branchId(1L).build();
        Product updated = Product.builder().id(1L).name("Product 1").stock(10).branchId(1L).build();

        when(productRepository.findById(1L)).thenReturn(Mono.just(existing));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updated));

        Mono<Product> result = useCase.updateStock(command);

        StepVerifier.create(result)
                .expectNext(updated)
                .verifyComplete();

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("should update product name successfully")
    void updateProductName() {
        UpdateProductNameCommand command = new UpdateProductNameCommand(1L, "Product Updated");

        Product existing = Product.builder().id(1L).name("Product 1").stock(0).branchId(1L).build();
        Product updated = Product.builder().id(1L).name("Product Updated").stock(0).branchId(1L).build();

        when(productRepository.findById(1L)).thenReturn(Mono.just(existing));
        when(productRepository.findByNameAndBranchId("Product Updated", 1L)).thenReturn(Mono.empty());
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updated));

        Mono<Product> result = useCase.updateName(command);

        StepVerifier.create(result)
                .expectNext(updated)
                .verifyComplete();

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("should return top products per branch successfully")
    void getTopProductsPerBranch() {
        Franchise franchise = Franchise.builder().id(1L).name("KFC").build();
        Product product1 = Product.builder().id(1L).name("Product 1").stock(50).branchId(1L).build();

        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(productRepository.getTopProductsPerBranch(1L)).thenReturn(Flux.just(product1));


        Flux<Product> result = useCase.getTopProductsPerBranch(1L);

        StepVerifier.create(result)
                .expectNext(product1)
                .verifyComplete();

        verify(franchiseRepository).findById(1L);
        verify(productRepository).getTopProductsPerBranch(1L);
    }


}
