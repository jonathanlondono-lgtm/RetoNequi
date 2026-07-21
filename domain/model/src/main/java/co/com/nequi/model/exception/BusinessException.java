package co.com.nequi.model.exception;

import co.com.nequi.model.enums.ErrorCode;

public class BusinessException extends ProcessorException {
    public BusinessException(ErrorCode errorCode, Object... args) {

        super(errorCode, args);
    }
}
