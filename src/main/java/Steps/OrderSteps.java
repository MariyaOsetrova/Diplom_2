package Steps;

import Model.Ingredients;
import Model.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static Utils.EndPoints.INGREDIENT;
import static Utils.EndPoints.ORDER;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создание заказа")
    public Response createOrderAuth(String token, Order order) {
        return given()
                .header("authorization", token)
                .body(order)
                .post(ORDER);

    }

    @Step("Получение информации по ингридиентам")
    public Ingredients getIngridients() {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .get(INGREDIENT)
                .body()
                .as(Ingredients.class);

    }

    @Step("Получение информации о заказе")
    public Response getOrder(String token) {
        return given()
                .header("authorization", token)
                .get(ORDER);

    }
}
