package com.pay.transactionroutine.service;

import com.pay.transactionroutine.domain.Account;

/**
 * @author Shridhar
 */

public interface AccountService {

    Long create(String documentNumber);

    Account findById(Long accountId);
}
