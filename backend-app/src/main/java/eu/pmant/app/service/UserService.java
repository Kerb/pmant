package eu.pmant.app.service;

import eu.pmant.app.generated.jooq.tables.pojos.UserAccount;
import eu.pmant.app.repository.UserRepository;
import eu.pmant.app.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    public UserService(UserRepository userRepository, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    public Optional<UserAccount> registerUser(String login, String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            logger.warn("Registration attempt with existing login: {}", login);
            return Optional.empty(); // User with this login already exists
        }

        String passwordHash = passwordUtil.hashPassword(password);
        UserAccount newUser = new UserAccount(null, login, passwordHash);
        UserAccount createdUser = userRepository.create(newUser);
        logger.info("User registered successfully with ID: {}", createdUser.getId());
        return Optional.of(createdUser);
    }

    public Optional<UserAccount> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordUtil.matches(rawPassword, encodedPassword);
    }
}