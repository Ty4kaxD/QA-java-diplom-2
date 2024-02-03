package user;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pens.UserPens;
import url.BaseUrl;
import java.util.Random;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserTest {
    static String emailUser = "username" + new Random().nextInt(1000) + "@yandex.ru";
    static String passwordUser = "userpas" + new Random().nextInt(10000);
    static String nameUser = "user" + new Random().nextInt(1000);
    User user = new User(emailUser, passwordUser, nameUser);
    UserPens userPens = new UserPens();
    private String accessToken;
    private String updateEmail = "username" + new Random().nextInt(10000) + "@yandex.ru";
    private String updateName = "user" + new Random().nextInt(10000);

    @Before
    public void setUp() {
        BaseUrl.setUp();
        userPens.createUser(user);
        accessToken = userPens.loginUser(user)
                .then()
                .extract().path("accessToken");
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userPens.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Изменение email авторизованного пользователя")
    public void testUpdateEmailAuthUser() {
        user.setEmail(updateEmail);
        userPens.updateUser(user, accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(updateEmail))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    public void testUpdateNameAuthUser() {
        user.setName(updateName);
        userPens.updateUser(user, accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .body("user.name", equalTo(updateName))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение email неавторизованного пользователя")
    public void testUpdateEmailNotAuthUser() {
        accessToken = "";
        user.setEmail(updateEmail);
        userPens.updateUser(user, accessToken)
                .then().assertThat()
                .body("success", equalTo(false)).
                and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя")
    public void testUpdateNameNotAuthUser() {
        accessToken = "";
        user.setName(updateName);
        userPens.updateUser(user, accessToken)
                .then().assertThat()
                .body("success", equalTo(false)).
                and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }


}
