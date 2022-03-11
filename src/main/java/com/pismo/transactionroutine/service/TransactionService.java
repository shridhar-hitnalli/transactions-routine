package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.domain.Account;
import com.pismo.transactionroutine.domain.OperationType;
import com.pismo.transactionroutine.domain.Transaction;
import com.pismo.transactionroutine.rest.request.TransactionRequest;

import java.util.List;

/**
 * @author Shridhar
 */

public interface TransactionService {

    Transaction create(TransactionRequest transactionRequest);

    List<Transaction> balanceOperation(Account account, Double amount, OperationType operationType);
}
