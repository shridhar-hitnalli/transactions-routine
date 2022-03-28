package com.pay.transactionroutine.service.impl;

import com.pay.transactionroutine.domain.Account;
import com.pay.transactionroutine.domain.OperationType;
import com.pay.transactionroutine.domain.Transaction;
import com.pay.transactionroutine.exception.BadRequestException;
import com.pay.transactionroutine.exception.TransactionNotFoundException;
import com.pay.transactionroutine.repository.TransactionRepository;
import com.pay.transactionroutine.service.AccountService;
import com.pay.transactionroutine.service.OperationTypeService;
import com.pay.transactionroutine.service.TransactionService;
import com.pay.transactionroutine.rest.request.TransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
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
    public Transaction create(TransactionRequest transactionRequest) {
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
            List<Transaction> transactionsToSave = balanceOperation(account, transactionRequest.getAmount(), operationType);
            List<Transaction> savedTransactions =  transactionRepository.saveAll(transactionsToSave);

            return savedTransactions
                    .stream()
                    .max(Comparator.comparing(Transaction::getId))
                    .orElse(transactionsToSave.get(transactionsToSave.size() - 1)); //as max returns optional object

        } else {
            return transactionRepository.save(
                    Transaction.builder()
                            .account(account)
                            .amount(transactionRequest.getAmount())
                            .operationType(operationType)
                            .balance(balance)
                            .eventDate(new Date())
                            .build()
            );
        }

    }

    private List<Transaction> getTransactionsByAccountId(Long id) {
        return transactionRepository.findByAccountId(id);
    }

    public List<Transaction> balanceOperation(Account account, Double amount, OperationType operationType) {
        var transactions = getTransactionsByAccountId(account.getId());
        var balanceNew = amount; //60
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        List<Transaction> transactionsToSave = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getBalance().intValue() == 0) {
                throw new BadRequestException("Primary value of the function is 0");
            }

            if (transaction.getAmount().intValue() <= 0) {
                throw new BadRequestException("Amount can not be zero or negative");
            }

            if (transaction.getBalance() < 0 && balanceNew.intValue() > 0) { //proceed only if the balance is -ve i.e purchase type and amount is positive i.e. Credit type
                var balancePos = Math.abs(transaction.getBalance());
                if (balanceNew > balancePos) {
                    balanceNew = Double.parseDouble(df.format(balanceNew - balancePos));
                    transaction.setBalance(0.00);
                } else {
                    var bal = Double.parseDouble(df.format(balanceNew - balancePos));
                    transaction.setBalance(-bal);
                    balanceNew = 0.00;
                }
                transactionsToSave.add(transaction);
            }
        }
        //new transaction with operation type -> credit after discharge process
        transactionsToSave.add(Transaction.builder()
                .account(account)
                .amount(amount)
                .operationType(operationType)
                .balance(balanceNew)
                .eventDate(new Date())
                .build());

        return transactionsToSave;
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction not found for the id: %s", id)));
    }
}
