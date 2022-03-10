package com.pismo.transactionroutine.service.impl;

import com.pismo.transactionroutine.domain.Account;
import com.pismo.transactionroutine.domain.OperationType;
import com.pismo.transactionroutine.domain.Transaction;
import com.pismo.transactionroutine.exception.BadRequestException;
import com.pismo.transactionroutine.repository.TransactionRepository;
import com.pismo.transactionroutine.rest.request.TransactionRequest;
import com.pismo.transactionroutine.service.AccountService;
import com.pismo.transactionroutine.service.OperationTypeService;
import com.pismo.transactionroutine.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Shridhar
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final OperationTypeService operationTypeService;

    private static final String  OPERATION_TYPE_CREDIT_VOUCHER = "Credit Voucher";

    @Override
    @Transactional
    public Long create(TransactionRequest transactionRequest) {
        log.info("creating transaction {}", transactionRequest);
        final Account account = accountService.findById(transactionRequest.getAccountId());
        final OperationType operationType = operationTypeService.findById(transactionRequest.getOperationTypeId());

        // positive balance
        if (operationType.getId().equals(4L) && OPERATION_TYPE_CREDIT_VOUCHER.equals(operationType.getDescription())) {
            if (transactionRequest.getAmount() < 0) {
                String errorLog = String.format("Invalid amount value for %s", operationType.getDescription());
                log.error(errorLog);
                throw new BadRequestException(errorLog);
            }

        } else {
            if (transactionRequest.getAmount() > 0) {
                String errorLog = String.format("Invalid amount value for %s", operationType.getDescription());
                log.error(errorLog);
                throw new BadRequestException(errorLog);
            }


        }
        Double balance = balanceOperation(account.getId(), transactionRequest.getAmount(), operationType.getDescription());

        return transactionRepository.save(
                Transaction.builder()
                        .account(account)
                        .amount(transactionRequest.getAmount())
                        .operationType(operationType)
                        .balance(balance)
                        .eventDate(new Date())
                        .build()
        ).getId();
    }

    private List<Transaction> getTransactionsByAccountId(Long id) {
        return transactionRepository.findByAccount_Id(id);
    }

    public Double balanceOperation(final Long accountId, Double amount, final String operationType) {
        if (operationType.equals(OPERATION_TYPE_CREDIT_VOUCHER) || amount > 0) {
            var transactions = getTransactionsByAccountId(accountId);
            for (Transaction transaction : transactions) {
                var newAmount = amount - Math.abs(transaction.getBalance()); //10 - 23.5 = -13.5
                balance = (amount - newAmount) - balance; //  10 +13.
                List<Double> values = setBalance(amount, Math.abs(transaction.getBalance())); // 0
                transaction.setBalance(values.get(0)); // 0
                amount = values.get(1); // 10
            }
        }

    }

    private List<Double> setBalance(Double amount, Double balance) {
        var newAmount = amount - balance; //10 - 23.5 = -13.5
        balance = (amount - newAmount) - balance; //  10 +13.5 - 23.5
        return Arrays.asList(balance, newAmount);
    }
}
