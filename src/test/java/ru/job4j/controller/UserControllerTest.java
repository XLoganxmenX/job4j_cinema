package ru.job4j.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.model.User;
import ru.job4j.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestRegistrationPage() {
        var view = userController.getRegistrationPage();
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterIsSuccess() {
        User user = new User();
        when(userService.save(user)).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);

        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    public void whenRegisterExistUserThenGetBackRegistrationPageAndMessage() {
        String expectedMessage = "Пользователь с такой почтой уже существует";
        User user = new User();
        when(userService.save(user)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("users/register");
        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void whenRequestLoginPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginIsSuccess() {
        User user = new User(1, "test@test.com", "test", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        HttpServletRequest request = new MockHttpServletRequest();
        var session = request.getSession();
        var view = userController.loginUser(user, model, request);
        var actualUser = session.getAttribute("user");

        assertThat(view).isEqualTo("redirect:/index");
        assertThat(user).isEqualTo(actualUser);
    }

    @Test
    public void whenLoginIncorrectThenReturnLoginPageAndError() {
        String expectedErrorMessage = "Почта или пароль введены неверно";
        User user = new User(1, "test@test.com", "test", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        HttpServletRequest request = new MockHttpServletRequest();
        var view = userController.loginUser(user, model, request);
        var actualErrorMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
    }

    @Test
    public void whenLogoutThenRedirectToLoginPage() {
        MockHttpSession httpSession = new MockHttpSession();
        var view = userController.logout(httpSession);

        assertThat(httpSession.isInvalid()).isTrue();
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}