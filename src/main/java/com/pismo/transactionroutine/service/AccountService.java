package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.domain.Account;

/**
 * @author Shridhar
 */

public interface AccountService {

    Long create(String documentNumber);

    Account findById(Long accountId);
}
