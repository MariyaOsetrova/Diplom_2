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

public class GetOrderUserTest extends BeforeTest {
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
    @DisplayName("Получение данных о заказе с авторизацией")
    @Description("Данные о заказе получены")
    public void getOrderAuthUserIngridient() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Ingredients ingredients = orderSteps.getIngridients();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(5).get_id());
        ingredient.add(ingredients.getData().get(8).get_id());
        orderSteps.createOrderAuth(accessToken, order);
        Response response = orderSteps.getOrder(accessToken);
        response.then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение данных о заказе без авторизации")
    @Description("Данные о заказе не получены. Ошибка 401")
    public void getOrderIngridientNotAuthUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Response response = orderSteps.getOrder("");
        response.then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
