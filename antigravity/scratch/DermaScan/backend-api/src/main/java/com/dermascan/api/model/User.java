package com.dermascan.api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Scan> scans;

    // Implementing the required methods from UserDetails interface
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can customize roles or authorities here if needed
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true; // Your logic for account expiration
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true; // Your logic for account locking
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Your logic for credential expiration
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    @Override
    public boolean isEnabled() {
        return true; // Your logic for whether the user is enabled
    }
}
