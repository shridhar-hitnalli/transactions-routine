package com.pismo.transactionroutine.repository;

import com.pismo.transactionroutine.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Shridhar
 */

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount_Id(Long accountId);
}
