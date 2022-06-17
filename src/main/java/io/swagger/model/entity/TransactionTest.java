package io.swagger.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;

@Entity
@Table(name = "TransactionsDTO")
public class TransactionTest {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String from = null;

    @Column(nullable = false)
    private String to = null;

    @Column(nullable = false)
    private Integer userIDPerforming = null;

    @Column(nullable = false)
    private Double amount = null;

    @Column(nullable = false)
    private OffsetDateTime timestamp = null;

    @Column(nullable = false)
    private String description = null;

    public TransactionTest(Long id, String from, String to, Integer userIDPerforming, Double amount, OffsetDateTime timestamp, String description) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.userIDPerforming = userIDPerforming;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
    }

    public TransactionTest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getUserIDPerforming() {
        return userIDPerforming;
    }

    public void setUserIDPerforming(Integer userIDPerforming) {
        this.userIDPerforming = userIDPerforming;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
