package co.com.nequi.usecase.franchise;

import co.com.nequi.model.exception.BusinessException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.command.CreateFranchiseCommand;
import co.com.nequi.model.franchise.command.UpdateFranchiseNameCommand;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository repository;

    @InjectMocks
    private FranchiseUseCase useCase;

    @Test
    @DisplayName("should create franchise successfully when name does not exist")
    void createFranchise(){
        CreateFranchiseCommand command = new CreateFranchiseCommand("KFC");

        Franchise franchise = Franchise.builder()
                .id(1L)
                .name("KFC")
                .build();

        when(repository.findByName("KFC"))
                .thenReturn(Mono.empty());
        when(repository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));


        Mono<Franchise> result = useCase.createFranchise(command);

        StepVerifier.create(result)
                .expectNext(franchise)
                .verifyComplete();

        verify(repository).findByName("KFC");
        verify(repository).save(any(Franchise.class));


    }

    @Test
    @DisplayName("should throw exception when franchise already exists")
    void createFranchiseAlreadyExists(){
        CreateFranchiseCommand command = new CreateFranchiseCommand("KFC");

        Franchise existing = Franchise.builder()
                .id(1L)
                .name("KFC")
                .build();

        when(repository.findByName("KFC"))
                .thenReturn(Mono.just(existing));

        Mono<Franchise> result = useCase.createFranchise(command);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
        verify(repository).findByName("KFC");
    }

    @Test
    @DisplayName("should update franchise name successfully")
    void updateFranchiseName(){
        UpdateFranchiseNameCommand command = new UpdateFranchiseNameCommand(1L, "McDonald's");

        Franchise existing = Franchise.builder().id(1L).name("KFC").build();
        Franchise updated = Franchise.builder().id(1L).name("McDonald's").build();



        when(repository.findById(1L)).thenReturn(Mono.just(existing));
        when(repository.findByName("McDonald's")).thenReturn(Mono.empty());
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(updated));

        Mono<Franchise> result = useCase.updateName(command);

        StepVerifier.create(result)
                .expectNext(updated)
                .verifyComplete();

        verify(repository).findById(1L);
        verify(repository).save(any(Franchise.class));

    }

    @Test
    @DisplayName("should throw exception when franchise not found on update name")
    void updateFranchiseNameNotFound() {
        UpdateFranchiseNameCommand command = new UpdateFranchiseNameCommand(1L, "McDonald's");

        when(repository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateName(command))
                .expectError(BusinessException.class)
                .verify();

        verify(repository).findById(1L);
    }
    }

