package com.pay.transactionroutine.service.impl;

import com.pay.transactionroutine.service.OperationTypeService;
import com.pay.transactionroutine.domain.OperationType;
import com.pay.transactionroutine.exception.OperationTypeNotFoundException;
import com.pay.transactionroutine.repository.OperationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Shridhar
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationTypeServiceImpl implements OperationTypeService {

    private final OperationTypeRepository operationTypeRepository;

    @Override
    public OperationType findById(Long id) {
        log.info("find operation type by id {}", id);
        return operationTypeRepository.findById(id)
                .orElseThrow( () -> new OperationTypeNotFoundException(String.format("Operation type not found with id %s", id)));
    }
}
