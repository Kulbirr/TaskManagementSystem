package com.example.TaskManagementSystem.Services;

import com.example.TaskManagementSystem.Entity.User;
import com.example.TaskManagementSystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collection;
import java.util.Collections;

public class MyUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        Collection<? extends GrantedAuthority> authorities = toGrantedAuthorities(user.getRole());

        return new org.springframework.security.core.userdetails.User(user.getUserName(), hashedPassword, authorities);
    }

    public static Collection<? extends GrantedAuthority> toGrantedAuthorities(GrantedAuthority authority) {
        return Collections.singletonList(new SimpleGrantedAuthority(authority.toString()));
    }
}
