package com.spring.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "email")
    private String email;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column(name = "provider_id")
    private String providerId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    public Set<Role> getRoles() {
        if (role != null) {
            return Set.of(role);
        } else {
            return Collections.emptySet();
        }
    }

    public void setRoles(Set<Role> roles) {
        if (roles.size() != 1) {
            throw new IllegalArgumentException("El usuario solo puede tener un único rol");
        }
        this.role = roles.iterator().next();
    }
}
