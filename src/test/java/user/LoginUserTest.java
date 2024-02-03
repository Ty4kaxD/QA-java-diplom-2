package user;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pens.UserPens;
import url.BaseUrl;
import java.util.Random;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {
    static String emailUser = "username" + new Random().nextInt(1000) + "@yandex.ru";
    static String passwordUser = "userpas" + new Random().nextInt(10000);
    static String nameUser = "user" + new Random().nextInt(1000);
    User user = new User(emailUser, passwordUser, nameUser);
    UserPens userPens = new UserPens();
    private String accessToken;

    @Before
    public void setUp() {
        BaseUrl.setUp();
        userPens.createUser(user);
        accessToken = userPens.loginUser(user)
                .then()
                .extract()
                .path("accessToken");
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userPens.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя позитивный тест")
    public void testLoginUser() {
        userPens.loginUser(user)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    public void testLoginUserIncorrectEmail() {
        user.setEmail("123123qweqwe@mail.ru");
        userPens.loginUser(user)
                .then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void testLoginUserIncorrectPassword() {
        user.setPassword("1234567890");
        userPens.loginUser(user)
                .then()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }


}