package io.swagger.model;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Transaction
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-14T17:26:10.419Z[GMT]")


public class Transaction   {
  @JsonProperty("transactionID")
  private Integer transactionID = null;

  @JsonProperty("from")
  private String from = null;

  @JsonProperty("to")
  private String to = null;

  @JsonProperty("userIDPerforming")
  private Integer userIDPerforming = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("description")
  private String description = null;

  public Transaction transactionID(Integer transactionID) {
    this.transactionID = transactionID;
    return this;
  }

  /**
   * Get transactionID
   * @return transactionID
   **/
  @Schema(example = "123", description = "")

  public Integer getTransactionID() {
    return transactionID;
  }

  public void setTransactionID(Integer transactionID) {
    this.transactionID = transactionID;
  }

  public Transaction from(String from) {
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

  public Transaction to(String to) {
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

  public Transaction userIDPerforming(Integer userIDPerforming) {
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

  public Transaction amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   **/
  @Schema(example = "1", required = true, description = "")

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Transaction timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   **/
  @Schema(required = true, description = "")
  @NotNull

  @Valid
  public String getTimestamp() {
    return timestamp;
  }

  public LocalDateTime getParsedTimestamp() {
    return LocalDateTime.parse(timestamp);
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public Transaction description(String description) {
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
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction transaction = (Transaction) o;
    return Objects.equals(this.transactionID, transaction.transactionID) &&
            Objects.equals(this.from, transaction.from) &&
            Objects.equals(this.to, transaction.to) &&
            Objects.equals(this.userIDPerforming, transaction.userIDPerforming) &&
            Objects.equals(this.amount, transaction.amount) &&
            Objects.equals(this.timestamp, transaction.timestamp) &&
            Objects.equals(this.description, transaction.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionID, from, to, userIDPerforming, amount, timestamp, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");

    sb.append("    transactionID: ").append(toIndentedString(transactionID)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    userIDPerforming: ").append(toIndentedString(userIDPerforming)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
