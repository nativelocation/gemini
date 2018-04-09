package com.gemini.admin.beans;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/8/18
 * Time: 11:35 AM
 */
public class AdminUser implements UserDetails, CredentialsContainer {

    private Long userId;
    private String username;
    private String password;
    private boolean enabled;
    private int precedence;
    private final Set<GrantedAuthority> authorities;
    private List<Long> allowedRegions;
    private List<Long> allowedSchools;

    public AdminUser(Long userId, String username, String password, boolean enabled, int precedence, Collection<? extends GrantedAuthority> authorities, List<Long> allowedRegions, List<Long> allowedSchools) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.precedence = precedence;
        this.authorities = Collections.unmodifiableSet(new HashSet<>(authorities));
        this.allowedRegions = allowedRegions;
        this.allowedSchools = allowedSchools;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    public List<Long> getAllowedRegions() {
        return allowedRegions;
    }

    public List<Long> getAllowedSchools() {
        return allowedSchools;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }
}