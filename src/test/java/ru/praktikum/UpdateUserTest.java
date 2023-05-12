package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.praktikum.UserUpdate.*;

public class UpdateUserTest {

    private User user;
    private UserClient userClient;
    private ValidatableResponse response;
    private String token;

    @Before
    public void setUp() {
        user = new User().userGenerator();
        userClient = new UserClient();
        response = userClient.createUser(user);
        token = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Обновление поля email авторизованного пользователя")
    public void updateEmailUserTest() {
        UserUpdate updateEmail = updateEmail(user);
        response = userClient.updateUser(token, updateEmail);
        response.statusCode(SC_OK).and().body("user.email", equalTo(updateEmail.getEmail()));
    }

    @Test
    @DisplayName("Обновление поля name авторизованного пользователя")
    public void updateNameUserTest() {
        UserUpdate updateName = updateName(user);
        response = userClient.updateUser(token, updateName);
        response.statusCode(SC_OK).and().body("user.name", equalTo(updateName.getName()));
    }

    @Test
    @DisplayName("Обновление поля email не авторизованного пользователя")
    public void updateEmailNotAuthUserTest() {
        UserUpdate updateEmail = updateEmail(user);
        response = userClient.updateUser("", updateEmail);
        response.statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Обновление поля name не авторизованного пользователя")
    public void updateNameNotAuthUserTest() {
        UserUpdate updateName = updateName(user);
        response = userClient.updateUser("", updateName);
        response.statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if(response.extract().path("accessToken") != null) {
            ValidatableResponse responseDeleteUser = userClient.deleteUser(token);
            responseDeleteUser.statusCode(SC_ACCEPTED).and().assertThat().body("message", equalTo("User successfully removed"));
        }
    }
}
