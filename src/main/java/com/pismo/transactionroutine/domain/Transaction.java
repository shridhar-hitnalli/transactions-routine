package com.pismo.transactionroutine.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Shridhar
 */

@Table(name = "transaction")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Transaction implements Serializable {

    private static final long serialVersionUID = 290968723743544700L;

    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the Transaction.", example = "1", required = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_type_id")
    @NotNull
    @Schema(description = "Operation type", required = true)
    private OperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @NotNull
    @Schema(description = "Account information", required = true)
    private Account account;

    @Column(name = "amount", nullable = false, columnDefinition = "DECIMAL(4,2)")
    @NotNull
    @Schema(description = "It holds the value of the transaction. Positive amount only for 'Credit Voucher'. ", example = "-30.00", required = true)
    private Double amount;

    @Column(name = "balance", nullable = false, columnDefinition = "DECIMAL(4,2)")
    @NotNull
    @Schema(description = "It holds the balance of the transaction.", example = "-30.00", required = true)
    private Double balance;


    @CreationTimestamp
    @Column(name = "event_date", nullable = false)
    @NotNull
    @Schema(description = "It holds the time when the transaction is occurred.", example = "2020-01-05T09:34:18.5893223", required = true)
    private Date eventDate;




}
