package com.marin.cafeteria.model;


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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private int pin;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(columnDefinition = "boolean default false" )
    private boolean active;


}
