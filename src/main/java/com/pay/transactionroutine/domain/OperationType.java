package com.pay.transactionroutine.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Shridhar
 */

@Table(name = "operation_type")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = {"id"})
public class OperationType implements Serializable {

    private static final long serialVersionUID = -67337012200244606L;

    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Schema(description = "Unique identifier of the Operation type.", example = "1", required = true)
    private Long id;

    @Column(name = "description", nullable = false, unique = true)
    @NotBlank
    @Schema(description = "Transactions type.", example = "Normal Purchase", required = true)
    private String description;


}
