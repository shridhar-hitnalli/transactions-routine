package com.pay.transactionroutine.rest;

import com.pay.transactionroutine.TransactionRoutineApplication;
import com.pay.transactionroutine.domain.Transaction;
import com.pay.transactionroutine.repository.TransactionRepository;
import com.pay.transactionroutine.rest.request.AccountRequest;
import com.pay.transactionroutine.rest.request.TransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TransactionRoutineApplication.class)
@ActiveProfiles("test")
@DisplayName("Transaction REST API Tests")
@Tag("IntegrationTest")
public class TransactionControllerItTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
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
    @DisplayName("given transactions, when creating new transaction with credit type, then returns new transaction with balance after payment discharge")
    public void givenTransactions_whenCreatingNewTransactionWithCreditType_thenReturnsNewTransactionAfterPaymentDischarge() {

        //given
        HttpEntity<AccountRequest> request = new HttpEntity<>(AccountRequest.builder().documentNumber("11111111").build());
        restTemplate.postForEntity("/accounts", request, Void.class);
        HttpEntity<TransactionRequest> transactionRequest = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(1L).amount(-20.00).build());
        ResponseEntity<Transaction> responseCreate1 = restTemplate.postForEntity("/transactions", transactionRequest, Transaction.class);
        assertNotNull(responseCreate1.getBody());

        HttpEntity<TransactionRequest> transactionRequest2 = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(2L).amount(-20.00).build());
        ResponseEntity<Transaction> responseCreate2 = restTemplate.postForEntity("/transactions", transactionRequest2, Transaction.class);
        assertNotNull(responseCreate2.getBody());

        //when
        HttpEntity<TransactionRequest> transactionRequest3 = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(4L).amount(50.00).build());
        ResponseEntity<Transaction> responseEntity =  restTemplate.postForEntity("/transactions", transactionRequest3, Transaction.class);

        ResponseEntity<Transaction> responseGet1 =  restTemplate.getForEntity("/transactions/" + responseCreate1.getBody().getId(), Transaction.class);
        ResponseEntity<Transaction> responseGet2 =  restTemplate.getForEntity("/transactions/" + responseCreate2.getBody().getId(), Transaction.class);

        //then
        assertEquals(CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
        assertEquals(50.00, responseEntity.getBody().getAmount());
        assertEquals(10.00, responseEntity.getBody().getBalance());
        assertNotNull(responseGet1.getBody());
        assertEquals(-20.00, responseGet1.getBody().getAmount());
        assertEquals(0.00, responseGet1.getBody().getBalance());
        assertNotNull(responseGet2.getBody());
        assertEquals(-20.00, responseGet2.getBody().getAmount());
        assertEquals(0.00, responseGet2.getBody().getBalance());
    }

}
