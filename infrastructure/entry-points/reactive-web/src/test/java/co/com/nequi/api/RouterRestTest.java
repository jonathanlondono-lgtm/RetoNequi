package co.com.nequi.api;

import co.com.nequi.api.dto.request.*;
import co.com.nequi.api.dto.response.BranchResponseDTO;
import co.com.nequi.api.dto.response.FranchiseResponseDTO;
import co.com.nequi.api.dto.response.ProductResponseDTO;
import co.com.nequi.api.handler.BranchHandler;
import co.com.nequi.api.handler.FranchiseHandler;
import co.com.nequi.api.handler.ProductHandler;
import co.com.nequi.api.validator.RequestValidator;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.product.Product;
import co.com.nequi.usecase.branch.BranchUseCase;
import co.com.nequi.usecase.franchise.FranchiseUseCase;
import co.com.nequi.usecase.product.ProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class, FranchiseHandler.class, BranchHandler.class, ProductHandler.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranchiseUseCase franchiseUseCase;

    @MockitoBean
    private BranchUseCase branchUseCase;

    @MockitoBean
    private ProductUseCase productUseCase;

    @MockitoBean
    private RequestValidator requestValidator;

    @Test
    @DisplayName("should create franchise successfully")
    void createFranchise() {
        FranchiseResponseDTO response = new FranchiseResponseDTO(1L, "KFC");
        Franchise franchise = new Franchise(1L, "KFC");
        FranchiseDTO request = new FranchiseDTO("KFC");


        when(requestValidator.validate(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(franchiseUseCase.createFranchise(any())).thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/api/franchises")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranchiseResponseDTO.class)
                .isEqualTo(response);
    }

    @Test
    @DisplayName("should create branch successfully")
    void createBranch() {
        BranchDTO request = new BranchDTO("Branch 1", 1L);
        Branch branch = new Branch(1L, "Branch 1", 1L);
        BranchResponseDTO response = new BranchResponseDTO(1L, "Branch 1", 1L);

        when(requestValidator.validate(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(branchUseCase.createBranch(any())).thenReturn(Mono.just(branch));

        webTestClient.post()
                .uri("/api/branches")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchResponseDTO.class)
                .isEqualTo(response);
    }

    @Test
    @DisplayName("should delete product successfully")
    void deleteProduct() {
        when(productUseCase.deleteProduct(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/products/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("should update product stock successfully")
    void updateProductStock() {
        UpdateStockDTO request = new UpdateStockDTO(10);
        Product product = new Product(1L, "Product 1", 10, 1L);
        ProductResponseDTO response = new ProductResponseDTO(1L, "Product 1", 10, 1L);

        when(requestValidator.validate(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(productUseCase.updateStock(any())).thenReturn(Mono.just(product));

        webTestClient.patch()
                .uri("/api/products/1/stock")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseDTO.class)
                .isEqualTo(response);
    }
    @Test
    @DisplayName("should return top products per branch successfully")
    void getTopProductsPerBranch() {
        Product product = new Product(1L, "Product 1", 50, 1L);
        List<ProductResponseDTO> response = List.of(new ProductResponseDTO(1L, "Product 1", 50, 1L));

        when(productUseCase.getTopProductsPerBranch(1L)).thenReturn(Flux.just(product));

        webTestClient.get()
                .uri("/api/products/franchise/1/top-products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponseDTO.class)
                .isEqualTo(response);
    }

    @Test
    @DisplayName("should update franchise name successfully")
    void updateFranchiseName() {
        UpdateNameDTO request = new UpdateNameDTO("McDonald's");
        Franchise franchise = new Franchise(1L, "McDonald's");
        FranchiseResponseDTO response = new FranchiseResponseDTO(1L, "McDonald's");

        when(requestValidator.validate(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(franchiseUseCase.updateName(any())).thenReturn(Mono.just(franchise));

        webTestClient.patch()
                .uri("/api/franchises/1/name")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranchiseResponseDTO.class)
                .isEqualTo(response);
    }

    @Test
    @DisplayName("should update branch name successfully")
    void updateBranchName() {
        UpdateNameDTO request = new UpdateNameDTO("Branch Updated");
        Branch branch = new Branch(1L, "Branch Updated", 1L);
        BranchResponseDTO response = new BranchResponseDTO(1L, "Branch Updated", 1L);

        when(requestValidator.validate(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(branchUseCase.updateName(any())).thenReturn(Mono.just(branch));

        webTestClient.patch()
                .uri("/api/branches/1/name")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchResponseDTO.class)
                .isEqualTo(response);
    }

    @Test
    @DisplayName("should update product name successfully")
    void updateProductName() {
        UpdateNameDTO request = new UpdateNameDTO("Product Updated");
        Product product = new Product(1L, "Product Updated", 0, 1L);
        ProductResponseDTO response = new ProductResponseDTO(1L, "Product Updated", 0, 1L);

        when(requestValidator.validate(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(productUseCase.updateName(any())).thenReturn(Mono.just(product));

        webTestClient.patch()
                .uri("/api/products/1/name")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseDTO.class)
                .isEqualTo(response);
    }

    @Test
    @DisplayName("should create product successfully")
    void createProduct() {
        ProductDTO request = new ProductDTO("Product 1", 1L);
        Product product = new Product(1L, "Product 1", 0, 1L);
        ProductResponseDTO response = new ProductResponseDTO(1L, "Product 1", 0, 1L);

        when(requestValidator.validate(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(productUseCase.createProduct(any())).thenReturn(Mono.just(product));

        webTestClient.post()
                .uri("/api/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseDTO.class)
                .isEqualTo(response);
    }



}
