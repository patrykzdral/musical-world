package com.patrykzdral.musicalworldcore.services.user.service.impl;

import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.user.model.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new CustomUserDetails(
                        user.getUsername(),
                        user.getPassword(),
                        user.isConfirmed(),
                        user.isConfirmed(),
                        user.isConfirmed(),
                        !user.isToBeDeleted(),
                        user.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                            .collect(Collectors.toList()),
                        user.getEmail(),
                        user.isRememberMe())
                ).orElseThrow(() -> new UsernameNotFoundException("User with the name"
                        + username + "not found in the database"));

    }
}
