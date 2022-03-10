package com.pismo.transactionroutine.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Shridhar
 */

@Table(name = "account")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = { "id", "documentNumber"})
public class Account implements Serializable {

    private static final long serialVersionUID = -67337012200244606L;

    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the Account.", example = "1", required = true)
    private Long id;

    @Column(name = "document_number", nullable = false, unique = true)
    @NotBlank
    @Schema(description = "Unique document number.", example = "11111", required = true)
    private String documentNumber;



}
