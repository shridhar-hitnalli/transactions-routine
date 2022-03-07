package com.pismo.transactionroutine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class OperationTypeNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 4448119411634586762L;

    public OperationTypeNotFoundException() {
        super();
    }

    public OperationTypeNotFoundException(String message) {
        super(message);
    }

    public OperationTypeNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public OperationTypeNotFoundException(Throwable t) {
        super(t);
    }

}
