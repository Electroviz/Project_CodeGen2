package io.swagger.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_EMPLOYEE,
    ROLE_USER;
    ;

    @Override
    public String getAuthority() {
        return name();
    }
}
