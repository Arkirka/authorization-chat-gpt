package ru.vorobyov.authorization.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vorobyov.authorization.dto.JwtRequest;
import ru.vorobyov.authorization.dto.JwtResponse;
import ru.vorobyov.authorization.dto.RefreshJwtRequest;
import ru.vorobyov.authorization.service.AuthService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token;
        try {
            token = authService.login(authRequest);
        } catch (AuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.getAccessToken(request.getRefreshToken());
        } catch (AuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<?> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.refresh(request.getRefreshToken());
        } catch (AuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(token);
    }

}
