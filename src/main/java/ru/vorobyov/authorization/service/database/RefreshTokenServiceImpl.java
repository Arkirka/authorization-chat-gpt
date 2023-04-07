package ru.vorobyov.authorization.service.database;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vorobyov.authorization.entity.RefreshToken;
import ru.vorobyov.authorization.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service("refreshTokenService")
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expirationMs}")
    private Long refreshTokenDurationMs;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) throws EntityNotFoundException {
        RefreshToken refreshToken = new RefreshToken();
        var user = userService.findById(userId);

        if (user.isEmpty())
            throw new EntityNotFoundException("User not found");

        refreshToken.setUser(user.get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public boolean verify(String token){
        var refreshToken = findByToken(token);
        if (refreshToken.isEmpty())
            return false;
        return refreshToken.get().getExpiryDate().compareTo(Instant.now()) >= 0;
    }

    @Override
    public int deleteByUserId(Long userId) {
        var user = userService.findById(userId);
        if (user.isEmpty())
            return -1;
        return refreshTokenRepository.deleteByUser(user.get());
    }
}
