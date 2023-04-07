package ru.vorobyov.authorization.service.database;

import ru.vorobyov.authorization.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(Long userId);
    boolean verify(String token);
    int deleteByUserId(Long userId);
}
