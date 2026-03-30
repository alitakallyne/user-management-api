package com.alita.usermanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuário")
public class UserController {

      @Operation(
        summary = "Dashboard do usuário",
        description = "Acesso permitido para usuários com role USER ou ADMIN"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Acesso autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping("/dashboard")
    public String userDashboard() {
        return "Usuário autenticado (USER ou ADMIN) acessa!";
    }

}
