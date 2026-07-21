package co.com.nequi.model.exception;

import co.com.nequi.model.enums.ErrorCode;

public class TechnicalException extends ProcessorException {
    public TechnicalException(ErrorCode errorCode, Object... args) {

        super(errorCode, args);
    }
}
