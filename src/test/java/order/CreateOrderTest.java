package order;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pens.OrderPens;
import pens.UserPens;
import url.BaseUrl;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    UserPens userPens = new UserPens();
    OrderPens orderPens = new OrderPens();
    static String emailUser = "username" + new Random().nextInt(1000) + "@yandex.ru";
    static String passwordUser = "userpas" + new Random().nextInt(10000);
    static String nameUser = "user" + new Random().nextInt(1000);
    User user = new User(emailUser, passwordUser, nameUser);
    private String accessToken;
    private Order order;
    private List<String> ingredients;
    Random random = new Random();

    @Before
    public void setUp() {
        BaseUrl.setUp();
        userPens.createUser(user);
        accessToken = userPens.loginUser(user).then().extract().path("accessToken");
        Ingredient ingredientList = orderPens.getIngredientInfo();
        ingredients = new ArrayList<>();
        int randomIndex_one = random.nextInt(ingredientList.getData().size());
        int randomIndex_two = random.nextInt(ingredientList.getData().size());
        int randomIndex_tree = random.nextInt(ingredientList.getData().size());
        ingredients.add(ingredientList.getData().get(randomIndex_one).get_id());
        ingredients.add(ingredientList.getData().get(randomIndex_two).get_id());
        ingredients.add(ingredientList.getData().get(randomIndex_tree).get_id());
        order = new Order(ingredients);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userPens.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void testCreateOrderAuthUser() {
        orderPens.createOrderAuthUser(order, accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void testCreateOrderNotAuthUser() {
        orderPens.createOrderNotAuthUser(order)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void testCreateOrderNoIngredients() {
        ingredients.clear();
        orderPens.createOrderAuthUser(order, accessToken)
                .then().assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа c неверным хешем ингредиентов")
    public void testCreateOrderWithWrongHash() {
        ingredients.add("qwe123weqwrg3454365eytrey");
        orderPens.createOrderAuthUser(order, accessToken)
                .then().assertThat()
                .statusCode(500);
    }


}
