package co.com.nequi.usecase.branch;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.command.CreateBranchCommand;
import co.com.nequi.model.branch.command.UpdateBranchNameCommand;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exception.BusinessException;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import co.com.nequi.model.franchise.Franchise;



@ExtendWith(MockitoExtension.class)
public class BranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;
    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private BranchUseCase useCase;

    @Test
    @DisplayName("should create branch successfully")
    void createBranch() {
        CreateBranchCommand command = new CreateBranchCommand("Branch 1", 1L);

        Branch branch = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();

        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(Franchise.builder().id(1L).name("KFC").build()));
        when(branchRepository.findByNameAndFranchiseId("Branch 1", 1L)).thenReturn(Mono.empty());
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(branch));

        Mono<Branch> result = useCase.createBranch(command);

        StepVerifier.create(result)
                .expectNext(branch)
                .verifyComplete();

        verify(franchiseRepository).findById(1L);
        verify(branchRepository).findByNameAndFranchiseId("Branch 1", 1L);
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    @DisplayName("should throw exception when branch already exists")
    void createBranchAlreadyExists() {
        CreateBranchCommand command = new CreateBranchCommand("Branch 1", 1L);

        Branch existing = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();

        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(Franchise.builder().id(1L).name("KFC").build()));
        when(branchRepository.findByNameAndFranchiseId("Branch 1", 1L)).thenReturn(Mono.just(existing));

        Mono<Branch> result = useCase.createBranch(command);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();

        verify(franchiseRepository).findById(1L);
        verify(branchRepository).findByNameAndFranchiseId("Branch 1", 1L);
    }

    @Test
    @DisplayName("should throw exception when franchise not found")
    void createBranchFranchiseNotFound() {
        CreateBranchCommand command = new CreateBranchCommand("Branch 1", 1L);

        when(franchiseRepository.findById(1L)).thenReturn(Mono.empty());
        lenient().when(branchRepository.findByNameAndFranchiseId("Branch 1", 1L)).thenReturn(Mono.empty());


        Mono<Branch> result = useCase.createBranch(command);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();

        verify(franchiseRepository).findById(1L);
    }
    @Test
    @DisplayName("should update branch name successfully")
    void updateBranchName() {
        UpdateBranchNameCommand command = new UpdateBranchNameCommand(1L, "Branch Updated");

        Branch existing = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();
        Branch updated = Branch.builder().id(1L).name("Branch Updated").franchiseId(1L).build();

        when(branchRepository.findById(1L)).thenReturn(Mono.just(existing));
        when(branchRepository.findByNameAndFranchiseId("Branch Updated", 1L)).thenReturn(Mono.empty());
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(updated));

        Mono<Branch> result = useCase.updateName(command);

        StepVerifier.create(result)
                .expectNext(updated)
                .verifyComplete();

        verify(branchRepository).findById(1L);
        verify(branchRepository).save(any(Branch.class));
    }
    @Test
    @DisplayName("should throw exception when branch not found on update name")
    void updateBranchNameNotFound() {
        UpdateBranchNameCommand command = new UpdateBranchNameCommand(1L, "Branch Updated");

        when(branchRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<Branch> result = useCase.updateName(command);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();

        verify(branchRepository).findById(1L);
        verify(branchRepository, never()).save(any(Branch.class));
    }

}
