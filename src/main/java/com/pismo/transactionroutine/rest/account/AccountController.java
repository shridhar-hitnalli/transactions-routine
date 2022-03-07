package com.pismo.transactionroutine.rest.account;

import com.pismo.transactionroutine.domain.Account;
import com.pismo.transactionroutine.rest.request.AccountRequest;
import com.pismo.transactionroutine.service.AccountService;
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
@RequestMapping(value = "/accounts")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    private static final String ACCOUNT_ID = "accountId";

    @Operation(summary = "Creates a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the account"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@Valid @RequestBody AccountRequest accountRequest) {
        log.info("POST /accounts : {}", accountRequest);
        Long accountId = accountService.create(accountRequest.getDocumentNumber());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(accountId).toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Retrieves an account by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content) })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Account> findAccountById(@PathVariable("id") Long id) {

        log.info("GET /accounts/{}",id);
        return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);

    }
}
