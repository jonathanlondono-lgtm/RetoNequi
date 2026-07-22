package co.com.nequi.api.handler;

import co.com.nequi.api.dto.request.BranchDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.dto.response.BranchResponseDTO;
import co.com.nequi.api.mapper.BranchApiMapper;
import co.com.nequi.api.validator.RequestValidator;
import co.com.nequi.usecase.branch.BranchUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
        summary = "Add a branch to a franchise",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = BranchDTO.class),
            examples = @ExampleObject(value = "{\"name\": \"Branch Bogota\", \"franchiseId\": 1}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Branch created successfully",
                content = @Content(schema = @Schema(implementation = BranchResponseDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Branch Bogota\", \"franchiseId\": 1}"))),
            @ApiResponse(responseCode = "404", description = "Franchise not found"),
            @ApiResponse(responseCode = "409", description = "Branch already exists in this franchise")
        }
    )
    public Mono<ServerResponse> createBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchDTO.class)
                .flatMap(requestValidator::validate)
                .map(BranchApiMapper::toCommand)
                .flatMap(branchUseCase::createBranch)
                .map(BranchApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Operation(
        summary = "Update branch name",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UpdateNameDTO.class),
            examples = @ExampleObject(value = "{\"name\": \"Branch Medellin\"}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Branch name updated successfully",
                content = @Content(schema = @Schema(implementation = BranchResponseDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Branch Medellin\", \"franchiseId\": 1}"))),
            @ApiResponse(responseCode = "404", description = "Branch not found"),
            @ApiResponse(responseCode = "409", description = "Branch name already exists in this franchise")
        }
    )
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
