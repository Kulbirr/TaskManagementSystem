package com.example.TaskManagementSystem.Config;

import com.example.TaskManagementSystem.Repositories.UserRepository;
import com.example.TaskManagementSystem.Services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {
//    @Autowired
//    private MyUserDetailsService userDetailsService;

    @Bean
    MyUserDetailsService myUserDetailsService(){
        return new MyUserDetailsService();
    }

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/api/user/**").permitAll()
                .requestMatchers("/api/task/user-get-tasklist").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/task/admin-get-allTasks").hasRole("Admin")
                .requestMatchers("/api/task/update-task/").hasAnyRole("Admin", "USER")
                .requestMatchers("/api/task/mark-as-completed/").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/task/delete-task/").hasAnyRole("Admin", "USER")
                .anyRequest().permitAll()
                .and()
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .permitAll());

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        // provide your user details service here
        return myUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

