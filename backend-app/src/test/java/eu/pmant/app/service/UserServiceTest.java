package eu.pmant.app.service;

import eu.pmant.app.generated.jooq.tables.pojos.UserAccount;
import eu.pmant.app.repository.UserRepository;
import eu.pmant.app.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        String login = "testuser";
        String password = "password123";
        String hashedPassword = "hashedPassword123";
        UserAccount newUser = new UserAccount(1L, login, hashedPassword);

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(passwordUtil.hashPassword(password)).thenReturn(hashedPassword);
        when(userRepository.create(any(UserAccount.class))).thenReturn(newUser);

        Optional<UserAccount> result = userService.registerUser(login, password);

        assertTrue(result.isPresent());
        assertEquals(login, result.get().getLogin());
        assertEquals(hashedPassword, result.get().getPasswordHash());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void registerUser_loginAlreadyExists() {
        String login = "existinguser";
        String password = "password123";
        UserAccount existingUser = new UserAccount(2L, login, "someHash");

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));

        Optional<UserAccount> result = userService.registerUser(login, password);

        assertFalse(result.isPresent());
    }

    @Test
    void findByLogin_userFound() {
        String login = "testuser";
        UserAccount user = new UserAccount(3L, login, "hashedPassword");
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        Optional<UserAccount> foundUser = userService.findByLogin(login);

        assertTrue(foundUser.isPresent());
        assertEquals(login, foundUser.get().getLogin());
    }

    @Test
    void findByLogin_userNotFound() {
        String login = "nonexistent";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        Optional<UserAccount> foundUser = userService.findByLogin(login);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void checkPassword_matches() {
        String rawPassword = "password123";
        String encodedPassword = "hashedPassword";
        when(passwordUtil.matches(rawPassword, encodedPassword)).thenReturn(true);

        assertTrue(userService.checkPassword(rawPassword, encodedPassword));
    }

    @Test
    void checkPassword_doesNotMatch() {
        String rawPassword = "password123";
        String encodedPassword = "wrongHashedPassword";
        when(passwordUtil.matches(rawPassword, encodedPassword)).thenReturn(false);

        assertFalse(userService.checkPassword(rawPassword, encodedPassword));
    }
}