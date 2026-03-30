package com.alita.usermanagement.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alita.usermanagement.config.TokenConfig;
import com.alita.usermanagement.infrastructure.dto.request.LoginRequest;
import com.alita.usermanagement.infrastructure.dto.request.RegisterUserRequest;
import com.alita.usermanagement.infrastructure.dto.response.LoginResponse;
import com.alita.usermanagement.infrastructure.dto.response.RegisterUserResponse;
import com.alita.usermanagement.infrastructure.entity.Role;
import com.alita.usermanagement.infrastructure.entity.User;
import com.alita.usermanagement.infrastructure.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @GetMapping("/teste")
    public String test() {
        return "Testando segurança";
    }

    @Operation(
        summary = "Realizar login",
        description = "Autentica o usuário e retorna um token JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(authToken);

       User user = (User) authentication.getPrincipal();
       String token = tokenConfig.generateToken(user);
       return ResponseEntity.ok(new LoginResponse(token));
    }

     @Operation(
        summary = "Registrar novo usuário",
        description = "Cria um novo usuário no sistema com role padrão USER"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
            content = @Content(schema = @Schema(implementation = RegisterUserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Usuário já existente"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        if(request.role() != null) {
            newUser.setRoles(Set.of(request.role()));
        } else {
            newUser.setRoles(Set.of(Role.ROLE_USER));
        }

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(newUser.getName(), newUser.getEmail()));
    }
}
