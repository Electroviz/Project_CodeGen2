package io.swagger.model.entity;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * BankAccountEntity
 */
@Entity
public class BankAccountEntity {

    @Id
    private String iban = null;

    @Column(nullable = false)
    private Long userId = null;

    @Column(nullable = false)
    private BigDecimal balance = null;

    @Column(nullable = false)
    private String accountType = null;

    @Column(nullable = false)
    private BigDecimal absoluteLimit = null;

    @Column(nullable = false)
    private String creationDate = null;

    /**
     * Get userId
     *
     * @return userId
     **/
    public Long getUserId() {
        return userId;
    }

    /**
     * Set user id.
     *
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Get iban
     *
     * @return iban
     **/
    public String getIban() {
        return iban;
    }
    /**
     * Set Iban.
     *
     * @param iban
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * Get balance
     *
     * @return balance
     **/
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Set balance.
     *
     * @param balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Get accountType
     *
     * @return accountType
     **/
    public String getAccountType() {
        return accountType;
    }

    /**
     * Get account type.
     *
     * @param accountType
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Get absoluteLimit
     *
     * @return absoluteLimit
     **/
    public BigDecimal getAbsoluteLimit() {
        return absoluteLimit;
    }

    /**
     * Set the absolute limit.
     *
     * @param absoluteLimit
     */
    public void setAbsoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }

    /**
     * Get creationDate
     *
     * @return creationDate
     **/
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Set creation date.
     *
     * @param creationDate
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Check if this object equals another.
     * @param o the other object.
     * @return whether this object equals the other object.
     */
    @Override
    public boolean equals(Object o) {
        // is this the same object?
        if (this == o) {
            return true;
        }

        // does the other object belong to a different class to this one?
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // check whether this object has the same properties as the other.
        BankAccountEntity bankAccount = (BankAccountEntity) o;
        return Objects.equals(this.userId, bankAccount.userId) &&
                Objects.equals(this.iban, bankAccount.iban) &&
                Objects.equals(this.balance, bankAccount.balance) &&
                Objects.equals(this.accountType, bankAccount.accountType) &&
                Objects.equals(this.absoluteLimit, bankAccount.absoluteLimit) &&
                Objects.equals(this.creationDate, bankAccount.creationDate);
    }

    /**
     * @return The hashcode of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, iban, balance, accountType, absoluteLimit, creationDate);
    }

    /**
     * @return The string representation of this object.
     */
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
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
