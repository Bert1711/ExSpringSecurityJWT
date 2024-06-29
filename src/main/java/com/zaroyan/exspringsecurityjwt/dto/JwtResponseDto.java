package com.zaroyan.exspringsecurityjwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
public class JwtResponseDto {
    @JsonProperty("auth-token")
    private String authToken;
}