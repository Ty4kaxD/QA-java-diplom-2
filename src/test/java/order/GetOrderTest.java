package order;

import io.qameta.allure.junit4.DisplayName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pens.OrderApi;
import pens.UserApi;
import url.BaseUrl;
import user.User;


import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest {
    BaseUrl baseUrl = new BaseUrl();
    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();
    static String emailUser = "username" + new Random().nextInt(1000) + "@yandex.ru";
    static String passwordUser = "userpas" + new Random().nextInt(10000);
    static String nameUser = "user" + new Random().nextInt(1000);
    User user = new User(emailUser, passwordUser, nameUser);
    private String accessToken;

    private List<String> ingredients;
    private Order order;

    @Before
    public void setUp() {
        baseUrl.setUp();
        userApi.createUser(user);
        accessToken = userApi.loginUser(user).then().extract().path("accessToken");
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    public void testGetOrderAuthUser() {
        orderApi.getOrdersAuthUser(accessToken)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void testGetOrderNotAuthUser() {
        orderApi.getOrdersNotAuthUser()
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }


}