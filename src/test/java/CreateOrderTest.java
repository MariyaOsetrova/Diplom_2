import Model.Ingredients;
import Model.Order;
import Model.User;
import Steps.OrderSteps;
import Steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest extends BeforeTest {

    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();
    private User user;
    private Order order;
    private List<String> ingredient;
    private String accessToken;

    @Before
    public void SetUp() {
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@ya.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.createUser(user);
        accessToken = userSteps.getToken(user);
        ingredient = new ArrayList<>();
        order = new Order(ingredient);

    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингридиентами")
    @Description("Заказ с ингридиентами создан")
    public void createOrderAuthUserIngridient() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Ingredients ingredients = orderSteps.getIngridients();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(5).get_id());
        ingredient.add(ingredients.getData().get(8).get_id());
        Response response = orderSteps.createOrderAuth(accessToken, order);
        response.then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингридиентов")
    @Description("Заказ с ингридиентами не создан. Ошибка 400")
    public void createOrderAuthUserNotIngridient() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Response response = orderSteps.createOrderAuth(accessToken, order);
        response.then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией c неверным хешем")
    @Description("Заказ с ингридиентами не создан. Ошибка 400")
    public void createOrderAuthUserIncorrectHash() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Ingredients ingredients = orderSteps.getIngridients();
        ingredient.add(ingredients.getData().get(1).get_id() + "1");
        ingredient.add(ingredients.getData().get(5).get_id() + "2");
        ingredient.add(ingredients.getData().get(8).get_id() + "3");
        Response response = orderSteps.createOrderAuth(accessToken, order);
        response.then()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с игридиантами")
    @Description("Заказ создан")
    public void createOrderIngridientNotAuthUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Ingredients ingredients = orderSteps.getIngridients();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(5).get_id());
        ingredient.add(ingredients.getData().get(8).get_id());
        Response response = orderSteps.createOrderAuth("", order);
        response.then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @After
    public void deleteUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}