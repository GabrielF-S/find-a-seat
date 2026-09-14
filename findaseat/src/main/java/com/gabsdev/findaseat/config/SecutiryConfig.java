package com.gabsdev.findaseat.config;

import com.gabsdev.findaseat.service.UserService;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecutiryConfig {

    private final UserService userService;
    private final FilterConfig securityFilter;

    public SecutiryConfig(UserService userService, FilterConfig securityFilter) {
        this.userService = userService;
        this.securityFilter = securityFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(httpSecurity))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .requestMatchers(HttpMethod.POST, "auth/register").permitAll()
                                .requestMatchers("auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "auth/adm/register").hasRole("SUPER")
                                .requestMatchers(HttpMethod.POST, "/api/business/**").hasAnyRole("ADMIN", "SUPER")
                                .requestMatchers(HttpMethod.DELETE, "/api/business/**").hasAnyRole("ADMIN", "SUPER")
                                .requestMatchers(HttpMethod.PUT, "/api/business/**").hasAnyRole("ADMIN", "SUPER")
                                .requestMatchers(HttpMethod.POST, "/api/floor/**").hasAnyRole("ADMIN", "SUPER")
                                .requestMatchers(HttpMethod.DELETE, "/api/floor/**").hasAnyRole("ADMIN", "SUPER")
                                .requestMatchers(HttpMethod.PUT, "/api/floor/**").hasAnyRole("ADMIN", "SUPER")
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
