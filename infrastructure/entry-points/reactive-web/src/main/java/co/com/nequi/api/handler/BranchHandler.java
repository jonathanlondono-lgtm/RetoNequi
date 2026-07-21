package co.com.nequi.api.handler;

import co.com.nequi.api.dto.request.BranchDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.mapper.BranchApiMapper;
import co.com.nequi.api.validator.RequestValidator;
import co.com.nequi.usecase.branch.BranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchUseCase branchUseCase;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> createBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchDTO.class)
                .flatMap(requestValidator::validate)
                .map(BranchApiMapper::toCommand)
                .flatMap(branchUseCase::createBranch)
                .map(BranchApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest serverRequest) {
        Long branchId = Long.valueOf(serverRequest.pathVariable("branchId"));
        return serverRequest.bodyToMono(UpdateNameDTO.class)
                .flatMap(requestValidator::validate)
                .map(dto -> BranchApiMapper.toUpdateNameCommand(branchId, dto))
                .flatMap(branchUseCase::updateName)
                .map(BranchApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }
}
