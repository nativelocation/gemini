package com.gemini.admin.beans;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/8/18
 * Time: 11:35 AM
 */
public class AdminUser implements UserDetails, CredentialsContainer {

    private Long secUserId;
    private String username;
    private String password;
    private boolean enabled;

    private int precedence = 2;
    private List<Long> allowedRegions;
    private List<Long> allowedSchools;


    public Long getSecUserId() {
        return secUserId;
    }

    public void setSecUserId(Long secUserId) {
        this.secUserId = secUserId;
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

    public void setAllowedRegions(List<Long> allowedRegions) {
        this.allowedRegions = allowedRegions;
    }

    public List<Long> getAllowedSchools() {
        return allowedSchools;
    }

    public void setAllowedSchools(List<Long> allowedSchools) {
        this.allowedSchools = allowedSchools;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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