package co.com.nequi.model.exception;

import co.com.nequi.model.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ProcessorException extends RuntimeException {

    private final ErrorCode errorCode;

    public ProcessorException(ErrorCode errorCode,  Object... args) {
        super(String.format(errorCode.getMessage(), args));
        this.errorCode = errorCode;
    }
}
