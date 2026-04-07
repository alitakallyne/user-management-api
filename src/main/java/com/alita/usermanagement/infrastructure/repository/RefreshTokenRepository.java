package com.alita.usermanagement.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alita.usermanagement.infrastructure.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

    Optional<RefreshToken> findByToken(String token);
}
