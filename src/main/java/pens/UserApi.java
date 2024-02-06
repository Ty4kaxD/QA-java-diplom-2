package pens;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.User;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;

public class UserApi {



    @Step("Создание пользователя")
    public Response createUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(REGISTER);
        return response;
    }



    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(LOGIN);
        return response;
    }

    @Step("Получение информации о пользователе")
    public Response getDataUser(String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(AUTHORIZATION);
        return response;
    }

    @Step("Обновление данных о пользователе")
    public Response updateUser(User user, String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .body(user)
                .patch(AUTHORIZATION);
        return response;
    }
    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken){
        Response response = given()
                .header("Authorization",accessToken)
                .when()
                .delete(AUTHORIZATION);
        return response;
    }

}
