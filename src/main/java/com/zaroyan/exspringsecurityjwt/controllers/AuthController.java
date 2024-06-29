package com.zaroyan.exspringsecurityjwt.controllers;

import com.zaroyan.exspringsecurityjwt.security.JwtUtil;
import com.zaroyan.exspringsecurityjwt.dto.JwtResponseDto;
import com.zaroyan.exspringsecurityjwt.dto.UserDto;
import com.zaroyan.exspringsecurityjwt.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Data
@Slf4j
@RequestMapping("/public")
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtTokenUtils,
            UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody UserDto userDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtils.generateToken(userDetails);
//            log.info(token);

            return ResponseEntity.ok(new JwtResponseDto(token));
        } catch (AuthenticationException e) {
            log.info("Ошибка аутентификации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response,
                                         @RequestHeader("auth-token") String authToken) {
        log.info("пришел токен: " + authToken);

        String jwt = authToken.substring(7);

        if (jwt == null) {
            return new ResponseEntity<>("Что-то пошло не так",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        log.info("logout");

        return ResponseEntity.ok("Success logout");
    }
}