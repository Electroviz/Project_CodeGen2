package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
<<<<<<< HEAD:src/main/java/io/swagger/model/UserDTO.java

import javax.persistence.Table;
=======
>>>>>>> parent of b79f652 (Merge branch 'Melle' into Nick):src/main/java/io/swagger/model/User.java
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * User
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-06-07T15:56:57.563Z[GMT]")

<<<<<<< HEAD:src/main/java/io/swagger/model/UserDTO.java
public class UserDTO   {
=======

public class User   {
>>>>>>> parent of b79f652 (Merge branch 'Melle' into Nick):src/main/java/io/swagger/model/User.java
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

  /**
   * Gets or Sets userRole
   */
  public enum UserRoleEnum {
    EMPLOYEE("Employee"),

    CUSTOMER("Customer");

    private String value;

    UserRoleEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static UserRoleEnum fromValue(String text) {
      for (UserRoleEnum b : UserRoleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("userRole")
  private UserRoleEnum userRole = null;

  @JsonProperty("transactionLimit")
  private BigDecimal transactionLimit = null;

  @JsonProperty("dayLimit")
  private BigDecimal dayLimit = null;

<<<<<<< HEAD:src/main/java/io/swagger/model/UserDTO.java
  public UserDTO id(Long id) {
=======
  public User id(Long id) {
>>>>>>> parent of b79f652 (Merge branch 'Melle' into Nick):src/main/java/io/swagger/model/User.java
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")
<<<<<<< HEAD:src/main/java/io/swagger/model/UserDTO.java

  public Long getId() {
=======
  
    public Long getId() {
>>>>>>> parent of b79f652 (Merge branch 'Melle' into Nick):src/main/java/io/swagger/model/User.java
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDTO username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   **/
  @Schema(example = "Kees1978", required = true, description = "")
  @NotNull

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserDTO fullname(String fullname) {
    this.fullname = fullname;
    return this;
  }

  /**
   * Get fullname
   * @return fullname
   **/
  @Schema(example = "Kees Post", description = "")

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public UserDTO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  @Schema(example = "PieterBG@gmail.com", required = true, description = "")
  @NotNull

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserDTO password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   **/
  @Schema(example = "fhdnd_Hdkf", required = true, description = "")
  @NotNull

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserDTO phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
   **/
  @Schema(example = "612345345", required = true, description = "")
  @NotNull

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public UserDTO dateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  /**
   * Get dateOfBirth
   * @return dateOfBirth
   **/
  @Schema(example = "17-09-1990", required = true, description = "")
  @NotNull

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public UserDTO userRole(UserRoleEnum userRole) {
    this.userRole = userRole;
    return this;
  }

  /**
   * Get userRole
   * @return userRole
   **/
  @Schema(required = true, description = "")
<<<<<<< HEAD:src/main/java/io/swagger/model/UserDTO.java
  @NotNull
=======
      @NotNull
>>>>>>> parent of b79f652 (Merge branch 'Melle' into Nick):src/main/java/io/swagger/model/User.java

    public UserRoleEnum getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRoleEnum userRole) {
    this.userRole = userRole;
  }

  public UserDTO transactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
    return this;
  }

  /**
   * Get transactionLimit
   * @return transactionLimit
   **/
  @Schema(example = "500", required = true, description = "")
  @NotNull

  @Valid
  public BigDecimal getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
  }

  public UserDTO dayLimit(BigDecimal dayLimit) {
    this.dayLimit = dayLimit;
    return this;
  }

  /**
   * Get dayLimit
   * @return dayLimit
   **/
  @Schema(example = "1000", required = true, description = "")
  @NotNull

  @Valid
  public BigDecimal getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(BigDecimal dayLimit) {
    this.dayLimit = dayLimit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDTO user = (UserDTO) o;
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

  @Override
  public int hashCode() {
    return Objects.hash(id, username, fullname, email, password, phone, dateOfBirth, userRole, transactionLimit, dayLimit);
  }

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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
