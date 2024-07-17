package ru.job4j.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleUserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void initService() {
        userRepository = mock(UserRepository.class);
        userService = new SimpleUserService(userRepository);
    }

    @Test
    public void whenSaveUserThenGetUserOptional() {
        var user = new User(1, "user", "user@mail.com", "password");
        when(userRepository.save(user)).thenReturn(Optional.of(user));
        var actualOptionalUser = userService.save(user);
        assertThat(actualOptionalUser).isEqualTo(Optional.of(user));
    }

    @Test
    public void whenSaveExistUserThenGetEmptyOptional() {
        var user = new User(1, "user", "user@mail.com", "password");
        when(userRepository.save(user)).thenReturn(Optional.empty());
        var actualOptionalUser = userService.save(user);
        assertThat(actualOptionalUser).isEqualTo(Optional.empty());
    }

    @Test
    public void whenFindUserThenGetOptionalUser() {
        var user = new User(1, "user", "user@mail.com", "password");
        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));
        var actualOptionalUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(actualOptionalUser).usingRecursiveComparison().isEqualTo(Optional.of(user));
    }

    @Test
    public void whenFindExistUserThenGetEmptyOptional() {
        var user = new User(1, "user", "user@mail.com", "password");
        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.empty());
        var actualOptionalUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(actualOptionalUser).usingRecursiveComparison().isEqualTo(Optional.empty());
    }
}