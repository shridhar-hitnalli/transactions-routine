package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.domain.Account;
import com.pismo.transactionroutine.domain.OperationType;
import com.pismo.transactionroutine.domain.Transaction;
import com.pismo.transactionroutine.exception.AccountNotFoundException;
import com.pismo.transactionroutine.repository.AccountRepository;
import com.pismo.transactionroutine.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Account Service Unit Tests")
public class AccountServiceTest {

    private AccountRepository accountRepositoryMock;
    private AccountService accountService;


    @BeforeAll
    public void init() {
        accountRepositoryMock = mock(AccountRepository.class);
        accountService = new AccountServiceImpl(accountRepositoryMock) {};
    }

    @Test
    @DisplayName("given valid document number input, when create new Account, then new Account Id is returned")
    void givenDocumentNumber_whenCreateAccount_ThenNewAccountReturned() {

        //given
        long newAccountId = 1L;
        String documentNumber = "21312331231";
        Account account = Account.builder().id(newAccountId).documentNumber(documentNumber).build();

        //when
        when(accountRepositoryMock.save(any(Account.class))).thenReturn(account);
        Long createdAccountId = accountService.create(documentNumber);

        //then
        assertNotNull(createdAccountId);
        assertEquals(newAccountId, createdAccountId);
    }

    @Test
    @DisplayName("given existing account id, when find Account, then Account object is returned")
    void givenExistingAccountId_whenFindAccountById_ThenAccountReturned() {

        //given
        long accountId = 1L;
        String documentNumber = "21312331231";
        Account account = Account.builder().id(accountId).documentNumber(documentNumber).build();

        //when
        when(accountRepositoryMock.findById(accountId)).thenReturn(Optional.of(account));
        Account existingAccount = accountService.findById(accountId);

        //then
        assertNotNull(existingAccount);
        assertEquals(accountId, existingAccount.getId());
        assertEquals(documentNumber, existingAccount.getDocumentNumber());
    }

    @Test
    @DisplayName("given non existing account id, when find Account, then exception is thrown")
    void givenNonExistentAccountId_whenFindAccountById_ThenExceptionThrown() {

        //given
        long accountId = 11L;
        String errorMsg = String.format("Account not found with id : %s ", accountId);
        when(accountRepositoryMock.findById(accountId)).thenThrow(new AccountNotFoundException(errorMsg));

        //when
        RuntimeException throwException = assertThrows(AccountNotFoundException.class, () ->  accountService.findById(accountId));

        //then
        assertEquals(errorMsg, throwException.getMessage());
    }




}
