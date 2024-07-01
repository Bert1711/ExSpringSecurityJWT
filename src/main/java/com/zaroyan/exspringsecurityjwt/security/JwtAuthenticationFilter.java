package com.zaroyan.exspringsecurityjwt.security;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zaroyan.exspringsecurityjwt.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Zaroyan
 */


@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtTokenUtils;
    private final UserDetailsServiceImpl userEntityDetailsService;

    @Autowired
    public JwtAuthenticationFilter(UserDetailsServiceImpl userEntityDetailsService, JwtUtil jwtTokenUtils) {
        this.userEntityDetailsService = userEntityDetailsService;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("Начало выполнения JwtRequestFilter");

        String authHeader = httpServletRequest.getHeader("Authorization");
        log.info("Auth Header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                log.info("Неверный токен JWT в заголовке носителя");
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
                return;
            } else {
                try {
                    log.info("Validate jwt");
                    String username = jwtTokenUtils.validateTokenAndRetrieveClaim(jwt);
                    log.info("Username from JWT: {}", username);
                    UserDetails userDetails = userEntityDetailsService.loadUserByUsername(username);
                    log.info("User details loaded for username: {}", username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.info("Аутентификация по токену выполнена");
                    }
                } catch (JWTVerificationException exc) {
                    log.info("Invalid JWT Token");
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                    return;
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
        log.info("Конец выполнения JwtRequestFilter");
    }
}
