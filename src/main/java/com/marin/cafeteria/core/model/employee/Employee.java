package com.marin.cafeteria.core.model.employee;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marin.cafeteria.core.model.auth.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;

    @JsonIgnore
    private int pin;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @Column(columnDefinition = "boolean default false" )
    private boolean active;


}
