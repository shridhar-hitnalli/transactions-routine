package com.pay.transactionroutine.repository;

import com.pay.transactionroutine.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Shridhar
 */

public interface AccountRepository extends JpaRepository<Account, Long> {
}
