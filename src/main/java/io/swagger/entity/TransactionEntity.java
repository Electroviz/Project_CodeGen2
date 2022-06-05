package io.swagger.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionID = null;

    @Column(nullable = false)
    private String fromAccount = null;

    @Column(nullable = false)
    private String toAccount = null;

    @Column(nullable = false)
    private Long userIDPerforming = null;

    @Column(nullable = false)
    private BigDecimal amount = null;

    @Column(nullable = false)
    private Timestamp timestamp = null;

    @Column
    private String description = null;



}
