package com.pay.transactionroutine.repository;

import com.pay.transactionroutine.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author Shridhar
 */

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(
            value = "SELECT * FROM transaction t WHERE t.account_id = ?1",
            nativeQuery = true)
    List<Transaction> findByAccountId(Long accountId);


    @Query(value = "SELECT t FROM Transaction t LEFT JOIN FETCH t.account LEFT JOIN FETCH t.operationType ot where t.id = :id")
    Optional<Transaction> findById(Long id);
}
