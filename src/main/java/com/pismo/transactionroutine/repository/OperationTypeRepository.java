package com.pismo.transactionroutine.repository;

import com.pismo.transactionroutine.domain.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Shridhar
 */

public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
}
