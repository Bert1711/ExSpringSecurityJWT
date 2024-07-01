package com.zaroyan.exspringsecurityjwt.service;

import com.zaroyan.exspringsecurityjwt.security.UserEntityDetails;
import com.zaroyan.exspringsecurityjwt.entities.User;
import com.zaroyan.exspringsecurityjwt.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Zaroyan
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository usersRepository)  {
        this.userRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Запрошена загрузка пользователя с именем: " + username);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            log.info("Пользователь с именем '%s' найден", username);
            return new UserEntityDetails(user.get());
        } else {
            log.warn("Пользователь с именем '%s' не найден", username);
            throw new UsernameNotFoundException("Пользователь не найден!");
        }
    }



//    public User createNewUser(RegistrationUserDto registrationUserDto) {
//        User user = new User();
//        user.setUsername(registrationUserDto.getUsername());
//        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
//        return userRepository.save(user);
//    }
}