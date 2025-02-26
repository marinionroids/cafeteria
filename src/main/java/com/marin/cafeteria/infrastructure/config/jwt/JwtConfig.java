package com.marin.cafeteria.infrastructure.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class JwtConfig {

    private String secret = "0vwaZQMxY8qJubVDcASrxWSRkgVmAnWT";
    private Long expiration = 36000L;
}
