package com.marin.cafeteria.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserAuthDTO {

    private String username;
    private int pin;
}
