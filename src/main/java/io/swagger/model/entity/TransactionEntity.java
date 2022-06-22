package io.swagger.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Transaction entity.
 */
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

    /**
     * Get transactionID
     * @return transactionID
     **/
    public Long getTransactionID() {
        return transactionID;
    }

    /**
     * Set transaction id
     * @param transactionID
     */
    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
    }

    /**
     * Get from
     * @return from
     **/
    public String getFromAccount() {
        return fromAccount;
    }

    /**
     * Set from.
     * @param fromAccount
     */
    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    /**
     * Get to
     * @return to
     **/
    public String getToAccount() {
        return toAccount;
    }

    /**
     * Set to
     * @param toAccount
     */
    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    /**
     * Get userIDPerforming
     * @return userIDPerforming
     **/
    public Long getUserIDPerforming() {
        return userIDPerforming;
    }

    /**
     * Set userIdPerforming
     * @param userIDPerforming
     */
    public void setUserIDPerforming(Long userIDPerforming) {
        this.userIDPerforming = userIDPerforming;
    }

    /**
     * Get amount
     * @return amount
     **/
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Set amount
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Get timestamp
     * @return timestamp
     **/
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Set timestamp
     * @param timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get description
     * @return description
     **/
    public String getDescription() {
        return description;
    }

    /**
     * Set description.
     * @param description
     */
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
        TransactionEntity transaction = (TransactionEntity) o;
        return Objects.equals(this.transactionID, transaction.transactionID) &&
                Objects.equals(this.fromAccount, transaction.fromAccount) &&
                Objects.equals(this.toAccount, transaction.toAccount) &&
                Objects.equals(this.userIDPerforming, transaction.userIDPerforming) &&
                Objects.equals(this.amount, transaction.amount) &&
                Objects.equals(this.timestamp, transaction.timestamp) &&
                Objects.equals(this.description, transaction.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionID, fromAccount, toAccount, userIDPerforming, amount, timestamp, description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Transaction {\n");

        sb.append("    transactionID: ").append(toIndentedString(transactionID)).append("\n");
        sb.append("    from: ").append(toIndentedString(fromAccount)).append("\n");
        sb.append("    to: ").append(toIndentedString(toAccount)).append("\n");
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
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
