package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.praktikum.UserCreds.*;

public class LoginUserTest {

    private User user;
    private UserClient userClient;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        user = new User().userGenerator();
        userClient = new UserClient();
        userClient.createUser(user);
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void authUserTest() {
        response = userClient.loginUser(credsFrom(user));
        response.statusCode(SC_OK).and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным email")
    public void authUserWithIncorrectEmailTest() {
        response = userClient.loginUser(credsWithIncorrectEmailFrom(user));
        response.statusCode(SC_UNAUTHORIZED).and().assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным password")
    public void authUserWithoutPasswordTest() {
        response = userClient.loginUser(credsWithIncorrectPasswordFrom(user));
        response.statusCode(SC_UNAUTHORIZED).and().assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        if(response.extract().path("accessToken") != null) {
            String token = response.extract().path("accessToken");
            ValidatableResponse responseDeleteUser = userClient.deleteUser(token);
            responseDeleteUser.statusCode(SC_ACCEPTED).and().assertThat().body("message", equalTo("User successfully removed"));
        }
    }
}
