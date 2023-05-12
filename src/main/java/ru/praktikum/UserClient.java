package ru.praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_PATH = "/api/auth/user";

    public UserClient() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(UserCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public  ValidatableResponse deleteUser(String token) {
        String tokenWithoutBearer = token.replaceAll("Bearer ", "");
        return given()
                .auth().oauth2(tokenWithoutBearer)
                .when()
                .delete(USER_PATH)
                .then();
    }

    @Step("Обновление данных пользователя")
    public  ValidatableResponse updateUser(String token, UserUpdate update) {
        String tokenWithoutBearer = token.replaceAll("Bearer ", "");
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(tokenWithoutBearer)
                .and()
                .body(update)
                .when()
                .patch(USER_PATH)
                .then();
    }
}
