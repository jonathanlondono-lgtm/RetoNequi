package co.com.nequi.api.handler;

import co.com.nequi.api.dto.request.FranchiseDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.mapper.FranchiseApiMapper;
import co.com.nequi.api.validator.RequestValidator;
import co.com.nequi.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private  final FranchiseUseCase franchiseUseCase;
    private final RequestValidator requestValidator;
    public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseDTO.class)
                .flatMap(requestValidator::validate)
                .map(FranchiseApiMapper::toCommand)
                .flatMap(franchiseUseCase::createFranchise)
                .map(FranchiseApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest serverRequest) {
        Long franchiseId = Long.valueOf(serverRequest.pathVariable("franchiseId"));
        return serverRequest.bodyToMono(UpdateNameDTO.class)
                .flatMap(requestValidator::validate)
                .map(dto -> FranchiseApiMapper.toUpdateNameCommand(franchiseId, dto))
                .flatMap(franchiseUseCase::updateName)
                .map(FranchiseApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

}
