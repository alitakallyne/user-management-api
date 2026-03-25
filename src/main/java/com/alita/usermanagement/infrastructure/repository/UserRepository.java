package com.alita.usermanagement.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alita.usermanagement.infrastructure.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
