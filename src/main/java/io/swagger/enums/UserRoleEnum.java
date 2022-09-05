package io.swagger.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.security.core.GrantedAuthority;


public enum UserRoleEnum implements GrantedAuthority {
    ROLE_BANK,
    ROLE_EMPLOYEE,
    ROLE_CUSTOMER;
    /*
    ROLE_BANK("bank"),
    ROLE_EMPLOYEE("employee"),
    ROLE_CUSTOMER("customer");

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
    */
    @Override
    public String getAuthority() {
        return name();
    }
}
