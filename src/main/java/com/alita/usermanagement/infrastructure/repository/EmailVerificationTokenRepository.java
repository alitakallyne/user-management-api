package com.alita.usermanagement.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alita.usermanagement.infrastructure.entity.EmailVerificationToken;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

}
