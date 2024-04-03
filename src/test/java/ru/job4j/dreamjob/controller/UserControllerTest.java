package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        request = mock(HttpServletRequest.class);
    }

    @Test
    void whenRegisterReturnsCorrectPage() {
        var user = new User(1, "e-mail", "password");
        var model = new ConcurrentModel();
        when(userService.save(user)).thenReturn(Optional.of(user));
        assertThat(userController.register(model, user)).isEqualTo("redirect:/vacancies");
    }

    @Test
    void whenRegisterReturnsIncorrectPage() {
        var user = new User(1, "e-mail", "password");
        var model = new ConcurrentModel();
        assertThat(userController.register(model, user)).isEqualTo("errors/404");
    }

    @Test
    void whenUserIsPresent() {
        var user = new User(1, "e-mail", "password");
        var model = new ConcurrentModel();
        when(userService.findByEmailAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(new MockHttpSession());
        assertThat(userController.loginUser(user, model, request)).isEqualTo("redirect:/vacancies");
    }

    @Test
    void whenUserIsNotPresent() {
        var user = new User(1, "e-mail", "password");
        var model = new ConcurrentModel();
        when(request.getSession()).thenReturn(new MockHttpSession());
        assertThat(userController.loginUser(user, model, request)).isEqualTo("users/login");
    }
}