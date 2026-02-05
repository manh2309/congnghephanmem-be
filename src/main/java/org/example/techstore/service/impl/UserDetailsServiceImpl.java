package org.example.techstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.techstore.entity.Account;
import org.example.techstore.repository.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account accountEntity = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(accountEntity.getUsername().equals(username)) {
            return new User(username, accountEntity.getPassword(), new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("Khong tim thay user: " + username);
        }
    }
}
