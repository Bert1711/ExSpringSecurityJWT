package com.zaroyan.exspringsecurityjwt.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 50, message = "Длина логина должна быть от 2 до 50 символов")
    @NotEmpty(message = "Логин не должен быть пустым")
    @Column(nullable = false, unique = true)
    private String username;

    @Size(min = 6, max = 1200, message = "Длина пароля должна быть от 6 до 20 символов")
    @Column(nullable = false)
    private String password;
}
