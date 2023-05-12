package ru.praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String INGREDIENT_PATH = "/api/ingredients";
    private static final String ORDER_PATH = "/api/orders";

    public OrderClient() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Получение данных об ингредиентах")
    public ValidatableResponse getIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(INGREDIENT_PATH)
                .then();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Ingredients ingredients, String token) {
        String tokenWithoutBearer = token.replaceAll("Bearer ", "");
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(tokenWithoutBearer)
                .and()
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getOrder(String token) {
        String tokenWithoutBearer = token.replaceAll("Bearer ", "");
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(tokenWithoutBearer)
                .when()
                .get(ORDER_PATH)
                .then();
    }
}
