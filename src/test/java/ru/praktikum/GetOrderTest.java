package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private ValidatableResponse response;
    private String token;

    @Before
    public void setUp() {
        User user = new User().userGenerator();
        userClient = new UserClient();
        orderClient = new OrderClient();
        token = userClient.createUser(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Получение заказов авторизованным пользователем")
    public void getOrderAuthUserTest() {
        response = orderClient.getOrder(token);
        response.statusCode(SC_OK).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов не авторизованным пользователем")
    public void getOrderNotAuthUserTest() {
        response = orderClient.getOrder("");
        response.statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }


    @After
    public void tearDown() {
        ValidatableResponse responseDeleteUser = userClient.deleteUser(token);
        responseDeleteUser.statusCode(SC_ACCEPTED).and().assertThat().body("message", equalTo("User successfully removed"));
    }

}
