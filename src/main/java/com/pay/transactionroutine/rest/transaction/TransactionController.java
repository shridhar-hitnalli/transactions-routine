package com.pay.transactionroutine.rest.transaction;

import com.pay.transactionroutine.domain.Transaction;
import com.pay.transactionroutine.rest.request.TransactionRequest;
import com.pay.transactionroutine.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * @author Shridhar
 */

@RestController
@RequestMapping(value = "/transactions")
@Validated
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Creates a new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the transaction"),
            @ApiResponse(responseCode = "404", description = "Account doesn't exist", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid value for operation type", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Transaction> create(@Valid @RequestBody TransactionRequest transactionRequest) {
        log.info("POST /transactions : {}", transactionRequest);
        Transaction transaction = transactionService.create(transactionRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(transaction.getId()).toUri();

        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieves a transaction by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = { @Content(mediaType = "application/json;charset=utf-8", schema = @Schema(implementation = Transaction.class)) }),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content) })
    @GetMapping( "/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable Long id) {
        log.info("GET /transactions/{}", id);
        Transaction transaction = transactionService.findById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

}
