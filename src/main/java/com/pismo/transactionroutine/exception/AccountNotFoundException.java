package com.pismo.transactionroutine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 941467785519660954L;

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public AccountNotFoundException(Throwable t) {
        super(t);
    }

}
