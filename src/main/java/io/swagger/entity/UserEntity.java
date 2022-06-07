package io.swagger.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

public class UserEntity {

    /* --------  User Properties ------- */
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    @Column(nullable = false, unique = true)
    private String username = null;

    @Column(nullable = false)
    private String fullname = null;

    @Column(nullable = false)
    private String email = null;

    @Column(nullable = false)
    private String password = null;

    @Column(nullable = false)
    private String phone = null;

    @Column(nullable = false)
    private String dateOfBirth = null;

    @Column(nullable = false)
    private String userRole = null;

    @Column(nullable = false)
    private BigDecimal transactionLimit = null;

    @Column(nullable = false)
    private BigDecimal dayLimit = null;

    /**
     * @return The user id.
     **/
    public Long getId() {
        return id;
    }


    /**
     * Set user id.
     *
     * @param id the id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get username.
     *
     * @return username
     **/
    public String getUsername() {
        return username;
    }


    /**
     * Set username.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get fullname
     *
     * @return fullname
     **/
    public String getFullname() {
        return fullname;
    }

    /**
     * Set the fullname.
     *
     * @param fullname the full name.
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Get email
     *
     * @return email
     **/
    public String getEmail() {
        return email;
    }

    /**
     * Set the email.
     *
     * @param email the email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get password
     *
     * @return password
     **/
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get phone
     *
     * @return phone
     **/
    public String getPhone() {
        return phone;
    }

    /**
     * Set the phone number.
     *
     * @param phone The phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get dateOfBirth
     *
     * @return dateOfBirth
     **/
    public String getDateOfBirth() {
        return dateOfBirth;
    }


    /**
     * Set the date of birth.
     *
     * @param dateOfBirth The date of birth.
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get userRole
     *
     * @return userRole
     **/
    public String getUserRole() {
        return userRole;
    }

    /**
     * Set the user role.
     *
     * @param userRole the user role.
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * Get transactionLimit
     *
     * @return transactionLimit
     **/
    public BigDecimal getTransactionLimit() {
        return transactionLimit;
    }

    /**
     * Set the transaction limit.
     *
     * @param transactionLimit The transaction limit.
     */
    public void setTransactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    /**
     * Get dayLimit
     *
     * @return dayLimit
     **/
    public BigDecimal getDayLimit() {
        return dayLimit;
    }


    /**
     * Set the day limit.
     *
     * @param dayLimit The day limit.
     */
    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    /**
     * Compare this user entity to another object to determine if they are equal.
     *
     * @param o Other object.
     * @return whether they are equal.
     */
    @Override
    public boolean equals(Object o) {
        // is it the same object?
        if (this == o) {
            return true;
        }

        // is the other object null or of a different class (type)
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // are each of the fields equal to the other object's fields?
        UserEntity user = (UserEntity) o;
        return Objects.equals(this.id, user.id) &&
                Objects.equals(this.username, user.username) &&
                Objects.equals(this.fullname, user.fullname) &&
                Objects.equals(this.email, user.email) &&
                Objects.equals(this.password, user.password) &&
                Objects.equals(this.phone, user.phone) &&
                Objects.equals(this.dateOfBirth, user.dateOfBirth) &&
                Objects.equals(this.userRole, user.userRole) &&
                Objects.equals(this.transactionLimit, user.transactionLimit) &&
                Objects.equals(this.dayLimit, user.dayLimit);
    }

    /**
     * @return The hash code of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, fullname, email, password, phone, dateOfBirth, userRole, transactionLimit, dayLimit);
    }

    /**
     * @return The string representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    fullname: ").append(toIndentedString(fullname)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
        sb.append("    dateOfBirth: ").append(toIndentedString(dateOfBirth)).append("\n");
        sb.append("    userRole: ").append(toIndentedString(userRole)).append("\n");
        sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
        sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
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
