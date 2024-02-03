package pens;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.User;

import static io.restassured.RestAssured.given;

public class UserPens {

    @Step("Создание пользователя")
    public Response createUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
        return response;
    }



    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/login");
        return response;
    }

    @Step("Получение информации о пользователе")
    public Response getDataUser(String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get("/api/auth/user");
        return response;
    }

    @Step("Обновление данных о пользователе")
    public Response updateUser(User user, String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .body(user)
                .patch("/api/auth/user");
        return response;
    }
    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken){
        Response response = given()
                .header("Authorization",accessToken)
                .when()
                .delete("/api/auth/user");
        return response;
    }

}
