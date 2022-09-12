package io.swagger.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.security.core.GrantedAuthority;


public enum UserRoleEnum implements GrantedAuthority {
    ROLE_BANK,
    ROLE_EMPLOYEE,
    ROLE_CUSTOMER;

    @Override
    public String getAuthority() {
        return name();
    }
}
