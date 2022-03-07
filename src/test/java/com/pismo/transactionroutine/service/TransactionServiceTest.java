package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.domain.Account;
import com.pismo.transactionroutine.domain.OperationType;
import com.pismo.transactionroutine.domain.Transaction;
import com.pismo.transactionroutine.exception.BadRequestException;
import com.pismo.transactionroutine.repository.TransactionRepository;
import com.pismo.transactionroutine.rest.request.TransactionRequest;
import com.pismo.transactionroutine.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(PER_CLASS)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Transaction Service Unit Tests")
public class TransactionServiceTest {

    private TransactionRepository transactionRepositoryMock;
    private AccountService accountServiceMock;
    private OperationTypeService operationTypeServiceMock;
    private TransactionServiceImpl transactionService;


    @BeforeAll
    public void init() {
        transactionRepositoryMock = mock(TransactionRepository.class);
        accountServiceMock = mock(AccountService.class);
        operationTypeServiceMock = mock(OperationTypeService.class);
        transactionService = new TransactionServiceImpl(transactionRepositoryMock, accountServiceMock, operationTypeServiceMock) {};
    }

    @Test
    @DisplayName("given valid transaction input, when create new Transaction, then new Transaction Id is returned")
    void givenTransactionRequest_whenCreateTransaction_ThenTransactionObjectReturned() {

        //given
        TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1L).operationTypeId(1L).amount(-30.00).build();
        Account account = Account.builder().id(transactionRequest.getAccountId()).documentNumber("21312331231").build();
        OperationType operationType = OperationType.builder().id(transactionRequest.getOperationTypeId()).description("Normal Purchase").build();
        when(accountServiceMock.findById(transactionRequest.getAccountId())).thenReturn(account);
        when(operationTypeServiceMock.findById(transactionRequest.getOperationTypeId())).thenReturn(operationType);
        Transaction transaction =  Transaction.builder()
                .id(1L)
                .account(account)
                .amount(transactionRequest.getAmount())
                .operationType(operationType)
                .eventDate(new Date())
                .build();
        //when
        when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(transaction);
        Long createdTransactionId = transactionService.create(transactionRequest);

        //then
        assertNotNull(createdTransactionId);
        assertEquals(transaction.getId(), createdTransactionId);
    }

    @Test
    @DisplayName("given invalid amount for credit voucher operation type, when create new Transaction, then exception is thrown")
    void givenInvalidAmountForCreditVoucher_whenCreateTransaction_ThenExceptionIsThrown() {

        //given
        TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1L).operationTypeId(4L).amount(-30.00).build();
        Account account = Account.builder().id(transactionRequest.getAccountId()).documentNumber("21312331231").build();
        OperationType operationType = OperationType.builder().id(transactionRequest.getOperationTypeId()).description("Credit Voucher").build();
        when(accountServiceMock.findById(transactionRequest.getAccountId())).thenReturn(account);
        when(operationTypeServiceMock.findById(transactionRequest.getOperationTypeId())).thenReturn(operationType);

        String errorMsg = String.format("Invalid amount value for %s", operationType.getDescription());
        when(transactionRepositoryMock.save(any(Transaction.class))).thenThrow(new BadRequestException(errorMsg));

        //when
        RuntimeException throwException = assertThrows(BadRequestException.class, () ->  transactionService.create(transactionRequest));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }

    @Test
    @DisplayName("given invalid amount for other operation type, when create new Transaction, then exception is thrown")
    void givenInvalidAmountForOtherOperationType_whenCreateTransaction_ThenExceptionIsThrown() {

        //given
        TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1L).operationTypeId(1L).amount(30.00).build();
        Account account = Account.builder().id(transactionRequest.getAccountId()).documentNumber("21312331231").build();
        OperationType operationType = OperationType.builder().id(transactionRequest.getOperationTypeId()).description("Normal Purchase").build();
        when(accountServiceMock.findById(transactionRequest.getAccountId())).thenReturn(account);
        when(operationTypeServiceMock.findById(transactionRequest.getOperationTypeId())).thenReturn(operationType);

        String errorMsg = String.format("Invalid amount value for %s", operationType.getDescription());
        when(transactionRepositoryMock.save(any(Transaction.class))).thenThrow(new BadRequestException(errorMsg));

        //when
        RuntimeException throwException = assertThrows(BadRequestException.class, () ->  transactionService.create(transactionRequest));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }
}
