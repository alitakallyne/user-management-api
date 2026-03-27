package com.alita.usermanagement.infrastructure.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(
    @NotEmpty(message = "Nome é obrigatório") String name,
    @NotEmpty(message = "E-mail é obrigatório") String email,
    @NotEmpty(message = "Senha é obrigatória") String password
) {

}
