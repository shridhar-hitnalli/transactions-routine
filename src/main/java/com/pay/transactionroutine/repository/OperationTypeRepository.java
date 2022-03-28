package com.pay.transactionroutine.repository;

import com.pay.transactionroutine.domain.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Shridhar
 */

public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
}
