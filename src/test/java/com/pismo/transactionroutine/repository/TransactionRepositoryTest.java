package com.pismo.transactionroutine.repository;

import com.pismo.transactionroutine.domain.Account;
import com.pismo.transactionroutine.domain.OperationType;
import com.pismo.transactionroutine.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Tag("IntegrationTest")
@DisplayName("Transaction Repository Integration Tests")
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    private Account account;

    private OperationType operationType;

    @BeforeEach
    public void init() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        String documentNumber = "123123213";
        account = Account.builder().documentNumber(documentNumber).build();
        accountRepository.save(account);

        operationType = operationTypeRepository.findById(1L).get();

    }

    @Test
    @DisplayName("when save in transaction repository, then transaction should be created successfully")
    public void whenSave_thenCreatingTransactionShouldBeSuccessful() {
        //given
        Double amount = 10.00;
        Transaction transaction = Transaction.builder().account(account).operationType(operationType).amount(amount).eventDate(new Date()).build();
        Transaction transaction2 = Transaction.builder().account(account).operationType(operationType).amount(amount).eventDate(new Date()).build();

        //when
        Transaction createdTransaction = transactionRepository.save(transaction);

        //then
        assertNotNull(createdTransaction);
        assertNotNull(createdTransaction.getId());
        assertEquals(account.getId(), transaction.getAccount().getId());
        assertEquals(operationType.getId(), transaction.getOperationType().getId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(1L, transactionRepository.count());
    }

    @Test
    @DisplayName("when save with multiple operationType in transaction repository, then transaction should be created successfully")
    public void whenSaveMultipleOperationType_thenCreatingAccountShouldBeSuccessful() {
        //given
        Double amount = 10.00;
        Transaction transaction1 = Transaction.builder().account(account).operationType(operationType).amount(amount).eventDate(new Date()).build();

        OperationType operationType2 = operationTypeRepository.findById(2L).get();
        Transaction transaction2 = Transaction.builder().account(account).operationType(operationType2).amount(amount).eventDate(new Date()).build();

        OperationType operationType3 = operationTypeRepository.findById(4L).get();
        Transaction transaction3 = Transaction.builder().account(account).operationType(operationType3).amount(amount).eventDate(new Date()).build();

        //when
        Transaction createdTransaction1 = transactionRepository.save(transaction1);
        Transaction createdTransaction2 = transactionRepository.save(transaction2);
        Transaction createdTransaction3 = transactionRepository.save(transaction3);

        //then
        assertNotNull(createdTransaction1);
        assertNotNull(createdTransaction2);
        assertNotNull(createdTransaction3);
        assertEquals(account.getId(), transaction1.getAccount().getId());
        assertEquals(account.getId(), transaction2.getAccount().getId());
        assertEquals(account.getId(), transaction3.getAccount().getId());
        assertEquals(operationType.getId(), transaction1.getOperationType().getId());
        assertEquals(operationType2.getId(), transaction2.getOperationType().getId());
        assertEquals(operationType3.getId(), transaction3.getOperationType().getId());
        assertEquals(3L, transactionRepository.count());
    }


    @Test
    @DisplayName("when save with multiple accounts in transaction repository, then transaction should be created successfully")
    public void whenSaveMultipleAccounts_thenCreatingAccountShouldBeSuccessful() {
        //given
        Double amount = 10.00;

        Transaction transaction1 = Transaction.builder().account(account).operationType(operationType).amount(amount).eventDate(new Date()).build();

        String documentNumber = "2312331";
        Account account2 = Account.builder().documentNumber(documentNumber).build();
        accountRepository.save(account2);

        Transaction transaction2 = Transaction.builder().account(account2).operationType(operationType).amount(amount).eventDate(new Date()).build();

        //when
        Transaction createdTransaction1 = transactionRepository.save(transaction1);
        Transaction createdTransaction2 = transactionRepository.save(transaction2);

        //then
        assertNotNull(createdTransaction1);
        assertNotNull(createdTransaction2);
        assertEquals(account.getId(), transaction1.getAccount().getId());
        assertEquals(account2.getId(), transaction2.getAccount().getId());
        assertEquals(operationType.getId(), transaction1.getOperationType().getId());
        assertEquals(operationType.getId(), transaction2.getOperationType().getId());
        assertEquals(2L, transactionRepository.count());
    }
}
