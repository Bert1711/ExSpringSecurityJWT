package com.zaroyan.exspringsecurityjwt.service;

import com.zaroyan.exspringsecurityjwt.dto.UserDto;
import com.zaroyan.exspringsecurityjwt.entities.User;
import com.zaroyan.exspringsecurityjwt.repositories.UserRepository;
import com.zaroyan.exspringsecurityjwt.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author Zaroyan
 */
@Service
public class AuthService {

    private final JwtUtil jwtTokenUtils;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(JwtUtil jwtTokenUtils, UserRepository userRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userRepository = userRepository;
    }


    public String performLogin(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtTokenUtils.generateToken(userDetails.getUsername());
    }


    public void logout(UserDto userDto) {

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(null);
        userRepository.delete(user);

    }

}
