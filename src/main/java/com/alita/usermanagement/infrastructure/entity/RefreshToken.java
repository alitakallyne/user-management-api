package com.alita.usermanagement.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String token; 

    private LocalDateTime expiresAt;

    private boolean revoked;
}
