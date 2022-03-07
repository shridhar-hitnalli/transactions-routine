package com.pismo.transactionroutine.repository;

import com.pismo.transactionroutine.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shridhar
 */

public interface AccountRepository extends JpaRepository<Account, Long> {
}
