package io.swagger.model.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.enums.UserRoleEnum;
import io.swagger.model.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.DataAmount;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String dateOfBirth;

    @JsonProperty("role")
    private UserRoleEnum role = null;

    @Column(nullable = false)
    private BigDecimal transactionLimit;

    @Column(nullable = false)
    private BigDecimal dayLimit;

    /**
     * Get role
     * @return role
     **/
    @Schema(required = true, description = "")
    @NotNull

    @Valid
    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }


    // Get a list of all available roles
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRoleEnum> roles;

    public List<UserRoleEnum> getRoles() {
        return roles;
    }
    public void setRoles(List<UserRoleEnum> roles) {
        this.roles = roles;
    }

    public User() {
    }

    public User(Long id, String username, String fullname, String email, String password, String phone, String dateOfBirth, UserRoleEnum role, BigDecimal transactionLimit, BigDecimal dayLimit, List<UserRoleEnum> roles) {
    private BigDecimal dayLimitConst;
    private Long lastUpdateTime;

    public User(Long id, String username, String fullname, String email, String password, String phone, String dateOfBirth, UserRoleEnum userRole, BigDecimal transactionLimit, BigDecimal dayLimit) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.transactionLimit = transactionLimit;
        this.dayLimit = dayLimit;
        this.roles = roles;
        this.dayLimitConst = dayLimit;

        this.lastUpdateTime = new Date().getTime();
    }

    public User() {

    }

    public BigDecimal getDayLimitConst() {
        return dayLimitConst;
    }

    public void setDayLimitConst(BigDecimal dayLimitConst) {
        this.dayLimitConst = dayLimitConst;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public BigDecimal getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    //nicky
    public void setDayLimit(BigDecimal dayLimit) {
        var currentTime = new Date().getTime();
        if(currentTime - lastUpdateTime >= 24*60*60*1000) // number of milliseconds in a day
        {
            lastUpdateTime = currentTime;
            this.dayLimit = dayLimitConst;
            this.dayLimit = dayLimit;
        }
        else {
            this.dayLimit = dayLimit;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", role=" + role +
                ", transactionLimit=" + transactionLimit +
                ", dayLimit=" + dayLimit +
                ", roles=" + roles +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, fullname, email, password, phone, dateOfBirth, transactionLimit, dayLimit, role, roles);
    }

}
