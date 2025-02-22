package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;

import java.util.Objects;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final UserRole userRole;

    public AuthUser(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        AuthUser authUser = (AuthUser) object;
        return Objects.equals(getId(), authUser.getId()) && Objects.equals(getEmail(), authUser.getEmail()) && getUserRole() == authUser.getUserRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getUserRole());
    }
}
