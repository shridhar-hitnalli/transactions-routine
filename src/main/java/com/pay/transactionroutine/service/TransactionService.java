package com.pay.transactionroutine.service;

import com.pay.transactionroutine.domain.Account;
import com.pay.transactionroutine.domain.OperationType;
import com.pay.transactionroutine.domain.Transaction;
import com.pay.transactionroutine.rest.request.TransactionRequest;

import java.util.List;

/**
 * @author Shridhar
 */

public interface TransactionService {

    Transaction create(TransactionRequest transactionRequest);

    List<Transaction> balanceOperation(Account account, Double amount, OperationType operationType);

    Transaction findById(Long id);
}
