package com.academy.fourtk.springbootessentials.services;

import com.academy.fourtk.springbootessentials.repositories.DevUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevUserDetailsService implements UserDetailsService {

    private final DevUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(repository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("DevUser not found! "));
    }
}
