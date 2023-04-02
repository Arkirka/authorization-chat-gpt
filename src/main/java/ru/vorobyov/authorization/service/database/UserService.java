package ru.vorobyov.authorization.service.database;

import ru.vorobyov.authorization.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getByLogin(String login);
    void updateRefreshToken(User user, String refreshToken);
}
