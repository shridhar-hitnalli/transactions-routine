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
        boolean isCreditOperation = false;
        Double balance = transactionRequest.getAmount();
        if (operationType.getId().equals(4L) && OPERATION_TYPE_CREDIT_VOUCHER.equals(operationType.getDescription())) {
            if (transactionRequest.getAmount() < 0) {
                String errorLog = String.format("Invalid amount value for %s", operationType.getDescription());
                log.error(errorLog);
                throw new BadRequestException(errorLog);
            }
            isCreditOperation = true;
        } else {
            if (transactionRequest.getAmount() > 0) {
                String errorLog = String.format("Invalid amount value for %s", operationType.getDescription());
                log.error(errorLog);
                throw new BadRequestException(errorLog);
            }

        }
        if (isCreditOperation) {
            balance = balanceOperation(account.getId(), transactionRequest.getAmount());
        }


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

    @Override
    public Transaction findById(Long id) {
        log.info("FindById :{}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Account not found with id : %s", id)));
    }

    private List<Transaction> getTransactionsByAccountId(Long id) {
        return transactionRepository.findByAccountId(id);
    }

    public Double balanceOperation(final Long accountId, Double amount) {
        var transactions = getTransactionsByAccountId(accountId);
        var amountNew = amount; // 60
        var balanceNew = amount; //60

        for (Transaction transaction : transactions) {
            if (transaction.getBalance() < 0 && amount > 0) { //proceed only if the balance is -ve i.e purchase type and amount is positive i.e Credit type
                var balancePos = Math.abs(transaction.getBalance());
                if (amountNew > balancePos) {
                    amountNew = amountNew - balancePos;
                    balanceNew = amountNew;
                    transaction.setBalance(0.00);
                } else {
                    balanceNew = balanceNew - amountNew;
                    transaction.setBalance(amountNew - balancePos);
                }
                transactionRepository.save(transaction); //the save is called here to test in real scenario we could add in a list and then save one by one
            }
        }
        return balanceNew;
    }
}
