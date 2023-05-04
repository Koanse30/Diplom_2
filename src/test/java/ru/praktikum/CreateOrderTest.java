package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTest {

    private UserClient userClient;
    private ValidatableResponse response;
    private OrderClient orderClient;
    private String token;
    private List<String> ingredientsList;
    private Ingredients ingredients;

    @Before
    public void setUp() {
        User user = new User().userGenerator();
        userClient = new UserClient();
        response = userClient.createUser(user);
        token = response.extract().path("accessToken");
        orderClient = new OrderClient();
        ingredientsList = orderClient.getIngredients().extract().path("data._id");
        ingredients = new Ingredients();
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизованным пользователем")
    public void createOrderWithIngredientsTest() {
        ingredients.setIngredients(List.of(ingredientsList.get(0), ingredientsList.get(1)));
        ValidatableResponse responseOrder = orderClient.createOrder(ingredients, token);
        responseOrder.statusCode(SC_OK).and().body("order.status", equalTo("done"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    public void createOrderWithoutIngredientsTest() {
        ValidatableResponse responseOrder = orderClient.createOrder(ingredients, token);
        responseOrder.statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами не авторизованным пользователем")
    public void createOrderWithIngredientsWithoutAuthTest() {
        ingredients.setIngredients(List.of(ingredientsList.get(0), ingredientsList.get(1)));
        ValidatableResponse responseOrder = orderClient.createOrder(ingredients, "");
        responseOrder.statusCode(SC_OK).and().body("order.status", nullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов не авторизованным пользователем")
    public void createOrderWithoutIngredientsWithoutAuthTest() {
        ValidatableResponse responseOrder = orderClient.createOrder(ingredients, "");
        responseOrder.statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиента авторизованным пользователем")
    public void createOrderWithIncorrectHashIngredientTest() {
        ingredients.setIngredients(List.of(RandomStringUtils.randomAlphanumeric(23).toLowerCase()));
        ValidatableResponse responseOrder = orderClient.createOrder(ingredients, token);
        responseOrder.statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        if(response.extract().path("accessToken") != null) {
            ValidatableResponse responseDeleteUser = userClient.deleteUser(token);
            responseDeleteUser.statusCode(SC_ACCEPTED).and().assertThat().body("message", equalTo("User successfully removed"));
        }
    }
}
