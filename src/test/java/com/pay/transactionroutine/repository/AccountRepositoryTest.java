package com.pay.transactionroutine.repository;

import com.pay.transactionroutine.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Tag("IntegrationTest")
@DisplayName("Account Repository Integration Tests")
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void init() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("when save in account repository, then account should be created successfully")
    public void whenSave_thenCreatingAccountShouldBeSuccessful() {
        //given
        String documentNumber = "123123213";
        Account account = Account.builder().documentNumber(documentNumber).build();

        //when
        Account createdAccount = accountRepository.save(account);

        //then
        assertNotNull(createdAccount);
        assertNotNull(createdAccount.getId());
        assertEquals(documentNumber, createdAccount.getDocumentNumber());
        assertEquals(1L, accountRepository.count());
    }

    @Test
    @DisplayName("when save in account repository, then account creation should fail")
    public void whenSaveWithSameDocumentNumber_thenCreatingAccountShouldFail() {
        //given
        String documentNumber = "123123213";
        Account account = Account.builder().documentNumber(documentNumber).build();
        Account account2 = Account.builder().documentNumber(documentNumber).build();

        //when
        Account createdAccount = accountRepository.save(account);


        //then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account2));
        assertNotNull(createdAccount);
        assertNotNull(createdAccount.getId());
        assertEquals(documentNumber, createdAccount.getDocumentNumber());
        assertEquals(1L, accountRepository.count());
    }

    @Test
    @DisplayName("when findById from account repository, then account should be retrieved successfully")
    public void whenFindById_thenAccountRetrievalShouldBeSuccessful() {
        //given
        String documentNumber = "123123213";
        Account account = Account.builder().documentNumber(documentNumber).build();
        Long newAccountId = accountRepository.save(account).getId();

        //when
        Optional<Account> optionalAccount = accountRepository.findById(newAccountId);

        //then
        assertTrue(optionalAccount.isPresent());

        assertNotNull(optionalAccount.get());
        assertEquals(newAccountId, optionalAccount.get().getId());
        assertEquals(documentNumber, optionalAccount.get().getDocumentNumber());
    }

    @Test
    @DisplayName("when find by non existing id from account repository, then account retrieval should fail")
    public void whenFindByNonExistentId_thenAccountRetrievalShouldFail() {
        //given
        String documentNumber = "123123213";
        Account account = Account.builder().documentNumber(documentNumber).build();

        //when
        Optional<Account> optionalAccount = accountRepository.findById(40L);

        //then
        assertFalse(optionalAccount.isPresent());
    }
}
