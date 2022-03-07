package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.domain.OperationType;
import com.pismo.transactionroutine.exception.OperationTypeNotFoundException;
import com.pismo.transactionroutine.repository.OperationTypeRepository;
import com.pismo.transactionroutine.service.impl.OperationTypeServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Operation Type Service Unit Tests")
public class OperationTypeServiceTest {

    private OperationTypeRepository operationTypeRepositoryMock;
    private OperationTypeService operationTypeService;


    @BeforeAll
    public void init() {
        operationTypeRepositoryMock = mock(OperationTypeRepository.class);
        operationTypeService = new OperationTypeServiceImpl(operationTypeRepositoryMock) {};
    }

    @Test
    @DisplayName("given existing operation type id, when find OperationType, then OperationType object is returned")
    void givenExistingOperationTypeId_whenFindOperationTypeById_ThenOperationTypeReturned() {

        //given
        long operationTypeId1 = 1L;
        String description1 = "Normal Purchase";
        OperationType operationTypePurchase = OperationType.builder().id(operationTypeId1).description(description1).build();

        long operationTypeId2 = 2L;
        String description2 = "Purchase with installments";
        OperationType operationTypeInstallments = OperationType.builder().id(operationTypeId2).description(description2).build();

        long operationTypeId3 = 3L;
        String description3 = "Withdrawal";
        OperationType operationTypeWithdrawal = OperationType.builder().id(operationTypeId3).description(description3).build();

        long operationTypeId4 = 4L;
        String description4 = "Credit Voucher";
        OperationType operationTypeCredit = OperationType.builder().id(operationTypeId4).description(description4).build();

        //when
        when(operationTypeRepositoryMock.findById(operationTypeId1)).thenReturn(Optional.of(operationTypePurchase));
        OperationType existingOperationType1 = operationTypeService.findById(operationTypeId1);

        when(operationTypeRepositoryMock.findById(operationTypeId2)).thenReturn(Optional.of(operationTypeInstallments));
        OperationType existingOperationType2 = operationTypeService.findById(operationTypeId2);

        when(operationTypeRepositoryMock.findById(operationTypeId3)).thenReturn(Optional.of(operationTypeWithdrawal));
        OperationType existingOperationType3 = operationTypeService.findById(operationTypeId3);

        when(operationTypeRepositoryMock.findById(operationTypeId4)).thenReturn(Optional.of(operationTypeCredit));
        OperationType existingOperationType4 = operationTypeService.findById(operationTypeId4);
        //then
        assertNotNull(existingOperationType1);
        assertNotNull(existingOperationType2);
        assertNotNull(existingOperationType3);
        assertNotNull(existingOperationType4);
        assertEquals(operationTypeId1, existingOperationType1.getId());
        assertEquals(description1, existingOperationType1.getDescription());
        assertEquals(operationTypeId2, existingOperationType2.getId());
        assertEquals(description2, existingOperationType2.getDescription());
        assertEquals(operationTypeId3, existingOperationType3.getId());
        assertEquals(description3, existingOperationType3.getDescription());
        assertEquals(operationTypeId4, existingOperationType4.getId());
        assertEquals(description4, existingOperationType4.getDescription());
    }

    @Test
    @DisplayName("given non existing operation type id, when find OperationType, then exception is thrown")
    void givenExistingOperationTypeId_whenFindOperationTypeById_ThenExceptionThrown() {

        //given
        long operationTypeId = 11L;
        String errorMsg = String.format("Operation type not found with id %s", operationTypeId);
        when(operationTypeRepositoryMock.findById(operationTypeId)).thenThrow(new OperationTypeNotFoundException(errorMsg));

        //when
        RuntimeException throwException = assertThrows(OperationTypeNotFoundException.class, () ->  operationTypeService.findById(operationTypeId));

        //then
        assertEquals(errorMsg, throwException.getMessage());
    }
}
