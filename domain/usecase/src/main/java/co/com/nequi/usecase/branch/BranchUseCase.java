package co.com.nequi.usecase.branch;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.command.CreateBranchCommand;
import co.com.nequi.model.branch.command.UpdateBranchNameCommand;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exception.BusinessException;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.nequi.model.enums.ErrorCode.BRANCH_ALREADY_EXISTS;
import static co.com.nequi.model.enums.ErrorCode.BRANCH_NOT_FOUND;
import static co.com.nequi.model.enums.ErrorCode.FRANCHISE_NOT_FOUND;

@RequiredArgsConstructor
public class BranchUseCase {
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> createBranch(CreateBranchCommand command) {
        return validateFranchiseExists(command.franchiseId())
                .then(validateBranchIsUnique(command.name(), command.franchiseId()))
                .then(Mono.defer(() -> branchRepository.save(Branch.fromCommand(command))));
    }

    public Mono<Branch> updateName(UpdateBranchNameCommand command) {
        return branchRepository.findById(command.branchId())
                .switchIfEmpty(Mono.error(new BusinessException(BRANCH_NOT_FOUND, command.branchId())))
                .flatMap(branch -> validateBranchIsUnique(command.name(), branch.getFranchiseId())
                        .then(Mono.defer(() -> branchRepository.save(branch.toBuilder().name(command.name()).build()))));
    }

    private Mono<Void> validateFranchiseExists(long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(FRANCHISE_NOT_FOUND, franchiseId)))
                .then();
    }

    private Mono<Void> validateBranchIsUnique(String branchName, Long franchiseId) {
        return branchRepository.findByNameAndFranchiseId(branchName, franchiseId)
                .flatMap(existing -> Mono.<Void>error(new BusinessException(BRANCH_ALREADY_EXISTS)))
                .switchIfEmpty(Mono.empty());
    }
}