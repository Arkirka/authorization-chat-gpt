package ru.vorobyov.authorization.service.database;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vorobyov.authorization.entity.User;
import ru.vorobyov.authorization.repository.UserRepository;

import java.util.Optional;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> create(User user) {
        return Optional.of(userRepository.save(user));
    }
}
