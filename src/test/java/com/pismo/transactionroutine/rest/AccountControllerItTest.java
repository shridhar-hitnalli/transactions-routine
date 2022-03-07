package com.pismo.transactionroutine.rest;

import com.pismo.transactionroutine.TransactionRoutineApplication;
import com.pismo.transactionroutine.domain.Account;
import com.pismo.transactionroutine.rest.request.AccountRequest;
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
@DisplayName("Account REST API Tests")
@Tag("IntegrationTest")
public class AccountControllerItTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("when POST a new Account, then returns 201")
    public void givenNewAccount_whenPostAccount_thenReturns201() {

        //given
        HttpEntity<AccountRequest> request = new HttpEntity<>(AccountRequest.builder().documentNumber("3131212").build());

        //when
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/accounts", request, Void.class);

        //then
        assertEquals(CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
    }

    @Test
    @DisplayName("given account id, when GET existing account, then returns 200")
    public void givenAccountId_whenGetExistingAccount_thenReturns200() {

        //given
        HttpEntity<AccountRequest> request = new HttpEntity<>(AccountRequest.builder().documentNumber("12323").build());
        ResponseEntity<Void> responseEntityAccount = restTemplate.postForEntity("/accounts", request, Void.class);


        //when
        ResponseEntity<Account> responseEntity = restTemplate.getForEntity(responseEntityAccount.getHeaders().getLocation(), Account.class);

        //then
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
        assertNotNull(responseEntity.getBody().getDocumentNumber());
    }

    @Test
    @DisplayName("given account id, when GET non existing account, then returns 404")
    public void givenNonExistentAccountId_whenGetNonExistingAccount_thenReturns404() {

        //given
        long accountId = 404L;

        //when
        ResponseEntity<Account> responseEntity = restTemplate.getForEntity("/accounts/"+accountId, Account.class);

        //then
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
    }

}
