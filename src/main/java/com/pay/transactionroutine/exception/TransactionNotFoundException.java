package com.pay.transactionroutine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException{


    private static final long serialVersionUID = 6113667446283361676L;

    public TransactionNotFoundException() {
        super();
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public TransactionNotFoundException(Throwable t) {
        super(t);
    }

}
