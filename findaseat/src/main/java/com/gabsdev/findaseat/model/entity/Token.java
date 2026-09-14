package com.gabsdev.findaseat.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity(name = "tb_tokens")
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    private String tokenString;
}
