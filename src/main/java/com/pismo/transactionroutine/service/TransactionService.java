package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.domain.Transaction;
import com.pismo.transactionroutine.rest.request.TransactionRequest;

/**
 * @author Shridhar
 */

public interface TransactionService {

    Transaction create(TransactionRequest transactionRequest);

}
