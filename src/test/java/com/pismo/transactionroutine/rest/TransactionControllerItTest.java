package com.pismo.transactionroutine.rest;

import com.pismo.transactionroutine.TransactionRoutineApplication;
import com.pismo.transactionroutine.rest.request.AccountRequest;
import com.pismo.transactionroutine.rest.request.TransactionRequest;
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

    @Test
    @DisplayName("given account, when POST a new Transaction, then returns 201")
    public void givenNewAccount_whenPostTransaction_thenReturns201() {

        //given
        HttpEntity<AccountRequest> request = new HttpEntity<>(AccountRequest.builder().documentNumber("324322").build());
        restTemplate.postForEntity("/accounts", request, Void.class);

        //when
        HttpEntity<TransactionRequest> transactionRequest = new HttpEntity<>(TransactionRequest.builder().accountId(1L).operationTypeId(1L).amount(-10.00).build());
        ResponseEntity<Void> responseEntityTransaction = restTemplate.postForEntity("/transactions", transactionRequest, Void.class);

        //then
        assertEquals(CREATED, responseEntityTransaction.getStatusCode());
        assertNotNull(responseEntityTransaction.getHeaders().getLocation());
    }

    @Test
    @DisplayName("given non existing account id, when POST transaction, then returns 404")
    public void givenNonExistingAccount_whenPostTransaction_thenReturns404() {

        //when
        HttpEntity<TransactionRequest> transactionRequest = new HttpEntity<>(TransactionRequest.builder().accountId(5L).operationTypeId(1L).amount(-10.00).build());
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
        ResponseEntity<Void> responseEntityTransaction = restTemplate.postForEntity("/transactions", transactionRequest, Void.class);

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
        ResponseEntity<Void> responseEntityTransaction = restTemplate.postForEntity("/transactions", transactionRequest, Void.class);

        //then
        assertEquals(BAD_REQUEST, responseEntityTransaction.getStatusCode());
    }

}
