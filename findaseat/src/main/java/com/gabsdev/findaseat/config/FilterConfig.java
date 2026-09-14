package com.gabsdev.findaseat.config;

import com.gabsdev.findaseat.dto.JWTUserData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class FilterConfig extends OncePerRequestFilter {
    private final TokenConfig tokenConfig;

    public FilterConfig(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String  authorizeHeader = request.getHeader("Authorization");
        if (Strings.isNotEmpty(authorizeHeader) && authorizeHeader.startsWith("Bearer ")){
            String token = authorizeHeader.substring("Baerer ".length());
            Optional<JWTUserData> optUser = tokenConfig.validadeToken(token);
            if (optUser.isPresent()){
                JWTUserData userData = optUser.get();
                List<SimpleGrantedAuthority> grantedAuthorities = userData.roles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userData,null, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }else{
            filterChain.doFilter(request,response);
        }



    }
}
