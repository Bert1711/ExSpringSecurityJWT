package com.zaroyan.exspringsecurityjwt.controllers;

import com.zaroyan.exspringsecurityjwt.dto.UserDto;
import com.zaroyan.exspringsecurityjwt.entities.User;
import com.zaroyan.exspringsecurityjwt.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zaroyan
 */
@RestController
@Data
@Slf4j
@RequestMapping("/public")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userRequest) {
        try {
            registrationService.register(userRequest);
            return ResponseEntity.ok("Пользователь успешно зарегистрирован.");
        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка при регистрации пользователя.");
        }
    }
}
