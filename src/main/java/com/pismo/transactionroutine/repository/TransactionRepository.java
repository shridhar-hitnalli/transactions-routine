package com.pismo.transactionroutine.repository;

import com.pismo.transactionroutine.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Shridhar
 */

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(
            value = "SELECT * FROM transaction t WHERE t.account_id = ?1",
            nativeQuery = true)
    List<Transaction> findByAccountId(Long accountId);
}
