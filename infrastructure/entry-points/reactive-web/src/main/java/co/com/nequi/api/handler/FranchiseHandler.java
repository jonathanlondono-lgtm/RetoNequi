package co.com.nequi.api.handler;

import co.com.nequi.api.dto.request.FranchiseDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.dto.response.FranchiseResponseDTO;
import co.com.nequi.api.mapper.FranchiseApiMapper;
import co.com.nequi.api.validator.RequestValidator;
import co.com.nequi.usecase.franchise.FranchiseUseCase;
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
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;
    private final RequestValidator requestValidator;

    @Operation(
        summary = "Create a new franchise",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = FranchiseDTO.class),
            examples = @ExampleObject(value = "{\"name\": \"KFC\"}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Franchise created successfully",
                content = @Content(schema = @Schema(implementation = FranchiseResponseDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"KFC\"}"))),
            @ApiResponse(responseCode = "409", description = "Franchise already exists")
        }
    )
    public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseDTO.class)
                .flatMap(requestValidator::validate)
                .map(FranchiseApiMapper::toCommand)
                .flatMap(franchiseUseCase::createFranchise)
                .map(FranchiseApiMapper::toResponse)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Operation(
        summary = "Update franchise name",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UpdateNameDTO.class),
            examples = @ExampleObject(value = "{\"name\": \"McDonald's\"}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Franchise name updated successfully",
                content = @Content(schema = @Schema(implementation = FranchiseResponseDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"McDonald's\"}"))),
            @ApiResponse(responseCode = "404", description = "Franchise not found"),
            @ApiResponse(responseCode = "409", description = "Franchise name already exists")
        }
    )
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
