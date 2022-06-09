package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TransactionInfo
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-12T15:22:53.754Z[GMT]")


public class TransactionInfo   {
  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("timestamp")
  private OffsetDateTime timestamp = null;

  @JsonProperty("userIDPerforming")
  private Integer userIDPerforming = null;

  public TransactionInfo amount(BigDecimal amount) {
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

  public TransactionInfo timestamp(OffsetDateTime timestamp) {
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
    public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public TransactionInfo userIDPerforming(Integer userIDPerforming) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionInfo transactionInfo = (TransactionInfo) o;
    return Objects.equals(this.amount, transactionInfo.amount) &&
        Objects.equals(this.timestamp, transactionInfo.timestamp) &&
        Objects.equals(this.userIDPerforming, transactionInfo.userIDPerforming);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, timestamp, userIDPerforming);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionInfo {\n");
    
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    userIDPerforming: ").append(toIndentedString(userIDPerforming)).append("\n");
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
