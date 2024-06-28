package com.zaroyan.exspringsecurityjwt.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Zaroyan
 */
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
}
