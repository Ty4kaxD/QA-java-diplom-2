package pens;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import order.Ingredient;
import order.Order;

import static io.restassured.RestAssured.given;

public class OrderPens {
    @Step("Получение ингредиентjd")
    public Ingredient getIngredientInfo(){
        return given()
                .header("Content-type", "application/json")
                .get("/api/ingredients")
                .as(Ingredient.class);
    }

    @Step("Создание заказа авторизованным пользователем")
    public Response createOrderAuthUser(Order order, String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");
        return response;
    }

    @Step("Создание заказа неавторизованным пользователем")
    public Response createOrderNotAuthUser(Order order) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/orders");
        return response;
    }

    @Step("Получение заказов авторизованным пользователем")
    public Response getOrdersAuthUser(String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .get("/api/orders");
        return response;
    }

    @Step("Получение заказов неавторизованным пользователем")
    public Response getOrdersNotAuthUser() {
        Response response = given()
                .header("Content-type", "application/json")
                .get("/api/orders");
        return response;
    }
}
