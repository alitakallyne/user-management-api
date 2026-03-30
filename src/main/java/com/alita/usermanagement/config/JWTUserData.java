package com.alita.usermanagement.config;

import java.util.List;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email,List<String> roles) {

}
