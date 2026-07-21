package co.com.nequi.api.validator;

import co.com.nequi.model.enums.ErrorCode;
import co.com.nequi.model.exception.BusinessException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> Mono<T> validate(T request){
        return Mono.just(request)
                .filter(req ->validator.validate(req).isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_PARAMETERS)));
    }
}
