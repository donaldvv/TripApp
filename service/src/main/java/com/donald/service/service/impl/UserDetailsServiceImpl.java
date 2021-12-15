package com.donald.service.service.impl;


import com.donald.service.dto.entity.UserDetailsImpl;
import com.donald.service.repository.UserRepository;
import com.donald.service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with the username: %s", username)));

        // will need id, so better to have an Implementation of the UserDetails
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                getAuthority(user)
        );
    }

    private Collection<SimpleGrantedAuthority> getAuthority(User user) {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());
    }
}
