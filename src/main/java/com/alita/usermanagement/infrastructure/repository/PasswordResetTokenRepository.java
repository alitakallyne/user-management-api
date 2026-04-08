package com.alita.usermanagement.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alita.usermanagement.infrastructure.entity.PasswordResetToken;

public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken, Long>{

}
