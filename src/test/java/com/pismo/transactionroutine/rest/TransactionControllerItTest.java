package com.pismo.transactionroutine.rest;

import com.pismo.transactionroutine.TransactionRoutineApplication;
import com.pismo.transactionroutine.domain.Transaction;
import com.pismo.transactionroutine.repository.TransactionRepository;
import com.pismo.transactionroutine.rest.request.AccountRequest;
import com.pismo.transactionroutine.rest.request.TransactionRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TransactionRoutineApplication.class)
@ActiveProfiles("test")
@TestInstance(PER_CLASS)
@DisplayName("Transaction REST API Tests")
@Tag("IntegrationTest")
public class TransactionControllerItTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeAll
    public void init() {
        transactionRepository.deleteAll();
    }

    @Test
    @DisplayName("given account, when POST a new Transaction, then returns 201")
    public void givenNewAccount_whenPostTransaction_thenReturns201() {

        //given
        HttpEntity<AccountRequest> request = new HttpEntity<>(AccountRequest.builder().documentNumber("324322").build());
        restTemplate.postForEntity("/accounts", request, Void.class);

        //when
        HttpEntity<TransactionRequest> transactionRequest = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(1L).amount(-10.00).build());
        ResponseEntity<Transaction> responseEntityTransaction = restTemplate.postForEntity("/transactions", transactionRequest, Transaction.class);

        //then
        assertEquals(CREATED, responseEntityTransaction.getStatusCode());
        assertNotNull(responseEntityTransaction.getBody());
        assertNotNull(responseEntityTransaction.getBody().getId());
    }

    @Test
    @DisplayName("given non existing account id, when POST transaction, then returns 404")
    public void givenNonExistingAccount_whenPostTransaction_thenReturns404() {

        //when
        HttpEntity<TransactionRequest> transactionRequest = new HttpEntity<>(TransactionRequest.builder().accountId(20L).operationTypeId(1L).amount(-10.00).build());
        ResponseEntity<Void> responseEntityTransaction = restTemplate.postForEntity("/transactions", transactionRequest, Void.class);

        //then
        assertEquals(NOT_FOUND, responseEntityTransaction.getStatusCode());
    }

    @Test
    @DisplayName("given invalid amount for Credit Voucher operation type, when POST transaction, then returns 400")
    public void givenInvalidAmountForCreditVoucher_whenPostTransaction_thenReturns400() {

        //given
        HttpEntity<AccountRequest> request = new HttpEntity<>(AccountRequest.builder().documentNumber("1234531").build());
        restTemplate.postForEntity("/accounts", request, Void.class);

        //when
        HttpEntity<TransactionRequest> transactionRequest = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(4L).amount(-10.00).build());
        ResponseEntity<Transaction> responseEntityTransaction = restTemplate.postForEntity("/transactions", transactionRequest, Transaction.class);

        //then
        assertEquals(BAD_REQUEST, responseEntityTransaction.getStatusCode());
    }

    @Test
    @DisplayName("given invalid amount for other operation type, when POST transaction, then returns 400")
    public void givenInvalidAmountForOperationType_whenPostTransaction_thenReturns404() {

        //given
        HttpEntity<AccountRequest> request = new HttpEntity<>(AccountRequest.builder().documentNumber("4543324").build());
        restTemplate.postForEntity("/accounts", request, Void.class);

        //when
        HttpEntity<TransactionRequest> transactionRequest = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(1L).amount(20.00).build());
        ResponseEntity<Transaction> responseEntityTransaction = restTemplate.postForEntity("/transactions", transactionRequest, Transaction.class);

        //then
        assertEquals(BAD_REQUEST, responseEntityTransaction.getStatusCode());
    }

    @Test
    @DisplayName("given transaction id for a new balance, when GET existing transaction, then returns 200")
    public void givenTransactionId_whenGetExistingTransaction_thenReturns200() {

        //given
        HttpEntity<AccountRequest> request = new HttpEntity<>(AccountRequest.builder().documentNumber("11111111").build());
        restTemplate.postForEntity("/accounts", request, Void.class);
        HttpEntity<TransactionRequest> transactionRequest = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(1L).amount(-20.00).build());
        restTemplate.postForEntity("/transactions", transactionRequest, Void.class);

        HttpEntity<TransactionRequest> transactionRequest2 = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(2L).amount(-20.00).build());
        restTemplate.postForEntity("/transactions", transactionRequest2, Void.class);

        HttpEntity<TransactionRequest> transactionRequest3 = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(4L).amount(50.00).build());
        ResponseEntity<Transaction> responseEntity =  restTemplate.postForEntity("/transactions", transactionRequest3, Transaction.class);


        //then
        assertEquals(CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
        assertEquals(50.00, responseEntity.getBody().getAmount());
        assertEquals(10.00, responseEntity.getBody().getBalance());
    }


}
