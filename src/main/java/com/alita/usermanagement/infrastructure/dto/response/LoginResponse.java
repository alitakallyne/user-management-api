package com.alita.usermanagement.infrastructure.dto.response;

public record LoginResponse(String access_token,
    String refresh_token,String token_type) {

}
