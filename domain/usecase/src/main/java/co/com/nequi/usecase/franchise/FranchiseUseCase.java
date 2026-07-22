package co.com.nequi.usecase.franchise;

import co.com.nequi.model.exception.BusinessException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.command.CreateFranchiseCommand;
import co.com.nequi.model.franchise.command.UpdateFranchiseNameCommand;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.nequi.model.enums.ErrorCode.FRANCHISE_ALREADY_EXITS;
import static co.com.nequi.model.enums.ErrorCode.FRANCHISE_NOT_FOUND;

@RequiredArgsConstructor
public class FranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> createFranchise(CreateFranchiseCommand command) {
        return franchiseRepository.findByName(command.name())
                .flatMap(existing -> Mono.<Franchise>error(new BusinessException(FRANCHISE_ALREADY_EXITS)))
                .switchIfEmpty(Mono.defer(() -> franchiseRepository.save(Franchise.fromCommand(command))));
    }

    public Mono<Franchise> updateName(UpdateFranchiseNameCommand command) {
        return franchiseRepository.findById(command.franchiseId())
                .switchIfEmpty(Mono.error(new BusinessException(FRANCHISE_NOT_FOUND, command.franchiseId())))
                .flatMap(franchise -> franchiseRepository.findByName(command.name())
                        .flatMap(existing -> Mono.<Franchise>error(new BusinessException(FRANCHISE_ALREADY_EXITS)))
                        .switchIfEmpty(Mono.defer(() -> franchiseRepository.save(franchise.toBuilder().name(command.name()).build()))));
    }
}
