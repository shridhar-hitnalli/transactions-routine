package com.pismo.transactionroutine.repository;

import com.pismo.transactionroutine.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Shridhar
 */

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
