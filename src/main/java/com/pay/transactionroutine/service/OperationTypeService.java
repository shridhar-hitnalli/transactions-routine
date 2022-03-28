package com.pay.transactionroutine.service;

import com.pay.transactionroutine.domain.OperationType;

/**
 * @author Shridhar
 */

public interface OperationTypeService {

    OperationType findById(Long id);
}
