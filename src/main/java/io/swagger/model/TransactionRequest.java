package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.threeten.bp.OffsetDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transaction
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-14T17:26:10.419Z[GMT]")


public class TransactionRequest {

    @JsonProperty("from")
    private String from = null;

    @JsonProperty("to")
    private String to = null;

    @JsonProperty("userIDPerforming")
    private Integer userIDPerforming = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;


    @JsonProperty("description")
    private String description = null;


    public TransactionRequest from(String from) {
        this.from = from;
        return this;
    }

    /**
     * Get from
     * @return from
     **/
    @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
    @NotNull

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public TransactionRequest to(String to) {
        this.to = to;
        return this;
    }

    /**
     * Get to
     * @return to
     **/
    @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
    @NotNull

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public TransactionRequest userIDPerforming(Integer userIDPerforming) {
        this.userIDPerforming = userIDPerforming;
        return this;
    }

    /**
     * Get userIDPerforming
     * @return userIDPerforming
     **/
    @Schema(example = "1234", required = true, description = "")
    @NotNull

    public Integer getUserIDPerforming() {
        return userIDPerforming;
    }

    public void setUserIDPerforming(Integer userIDPerforming) {
        this.userIDPerforming = userIDPerforming;
    }

    public TransactionRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Get amount
     * @return amount
     **/
    @Schema(example = "1", required = true, description = "")
    @NotNull

    @Valid
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionRequest description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     * @return description
     **/
    @Schema(example = "Payment request Anna", required = true, description = "")
    @NotNull

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionRequest transaction = (TransactionRequest) o;
        return
                Objects.equals(this.from, transaction.from) &&
                        Objects.equals(this.to, transaction.to) &&
                        Objects.equals(this.userIDPerforming, transaction.userIDPerforming) &&
                        Objects.equals(this.amount, transaction.amount) &&
                        Objects.equals(this.description, transaction.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, userIDPerforming, amount, description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Transaction {\n");

        sb.append("    from: ").append(toIndentedString(from)).append("\n");
        sb.append("    to: ").append(toIndentedString(to)).append("\n");
        sb.append("    userIDPerforming: ").append(toIndentedString(userIDPerforming)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    public Transaction toTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionID(0);
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setUserIDPerforming(userIDPerforming);
        transaction.setAmount(BigDecimal.valueOf(amount.doubleValue()));
        transaction.setTimestamp(String.valueOf(LocalDateTime.now()));
        transaction.setDescription(description);
        return transaction;
    }
}
