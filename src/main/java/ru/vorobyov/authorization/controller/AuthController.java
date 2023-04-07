package ru.vorobyov.authorization.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vorobyov.authorization.dto.*;
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

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getLogin() == null || request.getLogin().isBlank() || request.getPassword() == null || request.getPassword().isBlank())
            return new ResponseEntity<>("Empty login or password", HttpStatus.BAD_REQUEST);
        if (!authService.register(request))
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.ok().build();
    }

    @PostMapping("token")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.getAccessToken(request.getRefreshToken(), request.getAccessToken());
        } catch (AuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<?> getNewRefreshToken(@RequestBody NewRefreshJwtRequest request) {
        final JwtResponse token = authService.getNewRefreshToken(request.getRefreshToken());

        return token != null
                ? ResponseEntity.ok(token)
                : new ResponseEntity<>("Not refresh valid token", HttpStatus.UNAUTHORIZED);
    }
}
