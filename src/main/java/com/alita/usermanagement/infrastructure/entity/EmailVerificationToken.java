package com.alita.usermanagement.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class EmailVerificationToken {

    @Id
    @GeneratedValue
    private Long id;

    private String token; 

    @ManyToOne
    private User user;

    private LocalDateTime expiresAt;
}
