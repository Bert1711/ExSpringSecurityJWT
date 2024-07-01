package com.zaroyan.exspringsecurityjwt.service;

import com.zaroyan.exspringsecurityjwt.dto.UserDto;
import com.zaroyan.exspringsecurityjwt.entities.User;
import com.zaroyan.exspringsecurityjwt.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Zaroyan
 */
@Service
public class RegistrationService {

    private final UserRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        peopleRepository.save(user);
    }
}
