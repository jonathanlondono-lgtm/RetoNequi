package co.com.nequi.api.exception;

import co.com.nequi.api.exception.dto.ErrorResponseDTO;
import co.com.nequi.model.exception.ProcessorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Slf4j
@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          WebProperties webProperties,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        super.setMessageReaders(codecConfigurer.getReaders());
        super.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        String path = request.path();

        if (error instanceof ProcessorException ex) {
            log.error("Domain exception on [{}]: code={}, message={}", path, ex.getErrorCode().getCode(), ex.getMessage());

            HttpStatus status = HttpStatus.valueOf(ex.getErrorCode().getStatusCode());
            return buildResponse(status, ex.getErrorCode().getCode(), ex.getMessage(), path);
        }

        log.error("Unexpected error on [{}]: ", path, error);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "GEN-001",
                "An unexpected error occurred",
                path
        );
    }
    private Mono<ServerResponse> buildResponse(HttpStatus status, String code, String message, String path) {

        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .code(code)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
