package com.alita.usermanagement.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.alita.usermanagement.config.TokenConfig;
import com.alita.usermanagement.infrastructure.dto.request.LoginRequest;
import com.alita.usermanagement.infrastructure.dto.request.RefreshRequest;
import com.alita.usermanagement.infrastructure.dto.request.RegisterUserRequest;
import com.alita.usermanagement.infrastructure.dto.response.LoginResponse;
import com.alita.usermanagement.infrastructure.dto.response.RegisterUserResponse;
import com.alita.usermanagement.infrastructure.entity.RefreshToken;
import com.alita.usermanagement.infrastructure.entity.Role;
import com.alita.usermanagement.infrastructure.entity.User;
import com.alita.usermanagement.infrastructure.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, TokenConfig tokenConfig,
            RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @GetMapping("/teste")
    public String test() {
        return "Testando segurança";
    }

    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        
        attempts.put(request.email(), attempts.getOrDefault(request.email(), 0) + 1);
        if (attempts.get(request.email()) > 5) {
            throw new RuntimeException("Muitas tentativas. Tente novamente mais tarde.");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.email(),
                request.password());

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciais inválidas");
        }

        User user = (User) authentication.getPrincipal();

        if (!user.isVerified()) {
            throw new RuntimeException("Email não verificado");
        }

        String accessToken = tokenConfig.generateToken(user);
        String refreshTokenRaw = tokenConfig.generateRefreshToken();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(passwordEncoder.encode(refreshTokenRaw)); // HASH
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        attempts.remove(request.email());

        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshTokenRaw, "Bearer"));
    }

    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema com role padrão USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = RegisterUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuário já existente"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        if (userRepository.findUserByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }

        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setVerified(true);
        newUser.setPassword(passwordEncoder.encode(request.password()));

        if (request.role() != null) {
            newUser.setRoles(Set.of(request.role()));
        } else {
            newUser.setRoles(Set.of(Role.ROLE_USER));
        }

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterUserResponse(newUser.getName(), newUser.getEmail()));
    }

    @PostMapping("/refresh")
public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest request) {

    List<RefreshToken> tokens = refreshTokenRepository.findAll();

    RefreshToken validToken = tokens.stream()
        .filter(t -> passwordEncoder.matches(request.refreshToken(), t.getToken()))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido"));

    if (validToken.isRevoked() || validToken.getExpiresAt().isBefore(LocalDateTime.now())) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expirado");
    }

    User user = validToken.getUser();

   
    validToken.setRevoked(true);
    refreshTokenRepository.save(validToken);

  
    String newAccessToken = tokenConfig.generateToken(user);
    String newRefreshTokenRaw = tokenConfig.generateRefreshToken();

    RefreshToken newToken = new RefreshToken();
    newToken.setUser(user);
    newToken.setToken(passwordEncoder.encode(newRefreshTokenRaw));
    newToken.setExpiresAt(LocalDateTime.now().plusDays(7));
    newToken.setRevoked(false);

    refreshTokenRepository.save(newToken);

    return ResponseEntity.ok(
        new LoginResponse(newAccessToken, newRefreshTokenRaw, "Bearer")
    );
}
}
