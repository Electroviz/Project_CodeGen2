package io.swagger.model;

import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * User
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-06-07T15:56:57.563Z[GMT]")

public class UserDTO   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("fullname")
  private String fullname = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("dateOfBirth")
  private String dateOfBirth = null;

  @JsonProperty("transactionLimit")
  private BigDecimal transactionLimit;

  @JsonProperty("dayLimit")
  private BigDecimal dayLimit;

  @JsonProperty("role")
  private UserRoleEnum role = null;


  public UserDTO() {
  }

  public UserDTO(Long id, String username, String fullname, String email, String password, String phone, String dateOfBirth, BigDecimal transactionLimit, BigDecimal dayLimit, UserRoleEnum role) {
    this.id = id;
    this.username = username;
    this.fullname = fullname;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.dateOfBirth = dateOfBirth;
    this.transactionLimit = transactionLimit;
    this.dayLimit = dayLimit;
    this.role = role;
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

  public void setDayLimit(BigDecimal dayLimit) {
    this.dayLimit = dayLimit;
  }

  /**
   * Get role
   * @return role
   **/
  @Schema(required = true, description = "")
  //@NotNull

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

  @Override
  public String toString() {
    return "UserDTO{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", fullname='" + fullname + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", phone='" + phone + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            ", transactionLimit=" + transactionLimit +
            ", dayLimit=" + dayLimit +
            ", role=" + role +
            ", roles=" + roles +
            '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, fullname, email, password, phone, dateOfBirth, transactionLimit, dayLimit, role, roles);
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
