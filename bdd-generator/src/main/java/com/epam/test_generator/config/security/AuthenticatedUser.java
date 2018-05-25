package com.epam.test_generator.config.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Storage of user information which is later encapsulated into Authentication objects. This allows
 * non-security related user information (such as email addresses, telephone numbers etc) to be
 * stored in a convenient location.
 */

public class AuthenticatedUser implements UserDetails {

    private Long id;
    private String email;
    private String token;
    private Collection<? extends GrantedAuthority> authorityList;
    private List<Long> projectIds;
    private Boolean isAccountNonLocked;

    public AuthenticatedUser(Long id, String email, String token,
                             Collection<? extends GrantedAuthority> authorityList,
                             List<Long> projectIds, Boolean locked) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.authorityList = authorityList;
        this.projectIds = projectIds;
        this.isAccountNonLocked = !locked;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }
}
