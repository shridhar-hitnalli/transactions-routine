package com.pismo.transactionroutine.service.impl;

import com.pismo.transactionroutine.domain.Account;
import com.pismo.transactionroutine.domain.OperationType;
import com.pismo.transactionroutine.exception.AccountNotFoundException;
import com.pismo.transactionroutine.exception.OperationTypeNotFoundException;
import com.pismo.transactionroutine.repository.AccountRepository;
import com.pismo.transactionroutine.repository.OperationTypeRepository;
import com.pismo.transactionroutine.service.AccountService;
import com.pismo.transactionroutine.service.OperationTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
