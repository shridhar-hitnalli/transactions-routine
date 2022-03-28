package com.pay.transactionroutine.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Digits;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NonNull
    @Schema(description = "Account identifier", example = "1", required = true)
    private Long accountId;

    @NonNull
    @Schema(description = "Operation type identifier", example = "1", required = true)
    private Long operationTypeId;

    @NonNull
    @Digits(integer = 4, fraction = 2, message = "The amount cam have only 2 digits after precision ")
    @Schema(description = "Transaction amount", example = "-30.00", required = true)
    private Double amount;

}
