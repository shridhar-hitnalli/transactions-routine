package com.pismo.transactionroutine.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest{

    @NotNull
    @Schema(description = "Unique document number for the creation of the account.", example = "11111", required = true)
    private String documentNumber;
}
