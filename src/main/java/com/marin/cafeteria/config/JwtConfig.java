package com.marin.cafeteria.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class JwtConfig {

    private String secret = "secret";
    private Long expiration = 3600L;
}
