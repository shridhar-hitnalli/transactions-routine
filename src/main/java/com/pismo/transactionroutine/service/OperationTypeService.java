package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.domain.OperationType;

/**
 * @author Shridhar
 */

public interface OperationTypeService {

    OperationType findById(Long id);
}
