package com.pay.transactionroutine.service.impl;

import com.pay.transactionroutine.domain.Account;
import com.pay.transactionroutine.exception.AccountNotFoundException;
import com.pay.transactionroutine.repository.AccountRepository;
import com.pay.transactionroutine.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shridhar
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Long create(String documentNumber) {
        log.info("creating account inside service with document number {}", documentNumber);
        return accountRepository.save(
                Account.builder()
                        .documentNumber(documentNumber)
                        .build()
        ).getId();
    }

    @Override
    public Account findById(Long accountId) {
        log.info("find account by account id {}", accountId);
        return accountRepository.findById(accountId)
                .orElseThrow( () -> new AccountNotFoundException(String.format("Account not found with id : %s ", accountId)));

    }
}
