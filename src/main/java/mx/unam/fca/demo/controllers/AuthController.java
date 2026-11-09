package mx.unam.fca.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mx.unam.fca.demo.dto.AuthResponse;
import mx.unam.fca.demo.dto.LoginRequest;
import mx.unam.fca.demo.dto.RegisterRequest;
import mx.unam.fca.demo.dto.UserResponse;
import mx.unam.fca.demo.models.AppUser;
import mx.unam.fca.demo.repositories.AppUserRepository;
import mx.unam.fca.demo.service.JwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Registro, inicio de sesión y emisión de JWT")
public class AuthController {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthController(AppUserRepository users, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar una cuenta (siempre con rol USER)")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (users.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        AppUser account = users.save(new AppUser(
                request.username(), passwordEncoder.encode(request.password()), "USER"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(account.getId(), account.getUsername(), account.getRole()));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener un JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            String token = jwtTokenService.createToken(authentication);
            return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}