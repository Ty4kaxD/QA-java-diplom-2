package user;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pens.UserApi;
import url.BaseUrl;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

public class RegistrationUserTest {
    static String emailUser = "username" + new Random().nextInt(1000) + "@yandex.ru";
    static String passwordUser = "userpas" + new Random().nextInt(10000);
    static String nameUser = "user" + new Random().nextInt(1000);
    User user = new User(emailUser, passwordUser, nameUser);
    UserApi userApi = new UserApi();
    private String accessToken;

    @Before
    public void setUp() {
        BaseUrl.setUp();
    }

    @After
    public void deleteUser() {
        accessToken = userApi.loginUser(user)
                .then()
                .extract().path("accessToken");
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Регистрация пользователя")
    public void testCreateUser() {
        userApi.createUser(user)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Регистрация пользователя с данными ранее зарегистрированного пользователя")
    public void testCreateDuplicateUser() {
        userApi.createUser(user);
        userApi.createUser(user)
                .then()
                .assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Регистрация пользователя без email")
    public void testCreateUserWithoutEmail() {
        user.setEmail("");
        userApi.createUser(user)
                .then()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    public void testCreateUserWithoutName() {
        user.setName("");
        userApi.createUser(user)
                .then()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);

    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    public void testCreateUserWithoutPassword() {
        user.setPassword("");
        userApi.createUser(user)
                .then()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);

    }


}
