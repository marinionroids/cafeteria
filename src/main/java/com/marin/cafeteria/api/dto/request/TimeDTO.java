package com.marin.cafeteria.api.dto.request;


import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeDTO {

    @Max(31)
    private int day;

    @Max(12)
    private int month;

    private int year;

}
