package org.example.techstore.utils.account;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final Long accountId;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long accountId) {
        super(username, password, authorities);
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getRole() {
        return getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
    }
}
