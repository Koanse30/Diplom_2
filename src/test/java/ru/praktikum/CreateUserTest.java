package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {

    private User user;
    private UserClient userClient;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание пользователя со всеми полями")
    public void createUserWithAllFieldsTest() {
        user = new User().userGenerator();
        response = userClient.createUser(user);
        response.statusCode(SC_OK).and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailTest() {
        user = new User().withoutEmail();
        response = userClient.createUser(user);
        response.statusCode(SC_FORBIDDEN).and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без password")
    public void createUserWithoutPasswordTest() {
        user = new User().withoutPassword();
        response = userClient.createUser(user);
        response.statusCode(SC_FORBIDDEN).and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без name")
    public void createUserWithoutNameTest() {
        user = new User().withoutName();
        response = userClient.createUser(user);
        response.statusCode(SC_FORBIDDEN).and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    public void createTwoSameUsersTest() {
        user = new User().userGenerator();
        response = userClient.createUser(user);
        ValidatableResponse responseSecondUser = userClient.createUser(user);
        responseSecondUser.statusCode(SC_FORBIDDEN).and().assertThat().body("message", equalTo( "User already exists"));
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
