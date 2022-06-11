package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BankAccount
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-12T15:22:53.754Z[GMT]")

@Entity
public class BankAccount   {

  @Id
  @GeneratedValue
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("userId")
  private Long userId = null;

  @JsonProperty("iban")
  private String iban = null;

  @JsonProperty("balance")
  private Double balance = null;

  /**
   * Gets or Sets accountType
   */
  public enum AccountTypeEnum {
    CURRENT("Current"),
    
    SAVINGS("Savings");

    private String value;

    AccountTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AccountTypeEnum fromValue(String text) {
      for (AccountTypeEnum b : AccountTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  public enum AccountStatusEnum {
    ACTIVE("Active"),

    INACTIVE("Inactive"),

    CLOSED("Closed");

    private String value;

    AccountStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AccountStatusEnum fromValue(String text) {
      for (AccountStatusEnum b : AccountStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("accountType")
  private AccountTypeEnum accountType = null;

  @JsonProperty("absolute limit")
  private Double absoluteLimit = null;

  @JsonProperty("creationDate")
  private String creationDate = null;

  @JsonProperty("status")
  private AccountStatusEnum accountStatus = null;

  public void SetAccountStatus(AccountStatusEnum status) {
    this.accountStatus = status;
  }

  public BankAccount userId(long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
   **/
  @Schema(example = "1", required = true, description = "")

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public BankAccount iban(String iban) {
    this.iban = iban;
    return this;
  }

  public boolean CanWithdrawMoney(Double amount) {
    if(this.balance < amount) return false;
    else return true;
  }

  public void SetBalance(Double balanceAmount) {
      this.balance = balanceAmount;
  }

  /**
   * Get iban
   * @return iban
   **/
  @Schema(example = "NLxxINHO0xxxxxxxxx", description = "")
  
    public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  /**
   * Get balance
   * @return balance
   **/
  @Schema(example = "23.45", description = "")
  
    @Valid
    public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public BankAccount accountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
    return this;
  }

  /**
   * Get accountType
   * @return accountType
   **/
  @Schema(required = true, description = "")
      @NotNull

    public AccountTypeEnum getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
  }

  public BankAccount absoluteLimit(Double absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
    return this;
  }

  public String GetIBAN() {
    return this.iban;
  }

  /**
   * Get absoluteLimit
   * @return absoluteLimit
   **/
  @Schema(example = "1", required = true, description = "")
      @NotNull

    @Valid
    public Double getAbsoluteLimit() {
    return absoluteLimit;
  }

  public void setAbsoluteLimit(Double absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
  }

  public BankAccount creationDate(String creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * Get creationDate
   * @return creationDate
   **/
  @Schema(example = "01-05-2022:12:00:00", description = "")
  
    public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankAccount bankAccount = (BankAccount) o;
    return Objects.equals(this.userId, bankAccount.userId) &&
        Objects.equals(this.iban, bankAccount.iban) &&
        Objects.equals(this.balance, bankAccount.balance) &&
        Objects.equals(this.accountType, bankAccount.accountType) &&
        Objects.equals(this.absoluteLimit, bankAccount.absoluteLimit) &&
        Objects.equals(this.creationDate, bankAccount.creationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, iban, balance, accountType, absoluteLimit, creationDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankAccount {\n");
    
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    absoluteLimit: ").append(toIndentedString(absoluteLimit)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
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
