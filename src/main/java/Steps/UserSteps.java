package Steps;

import Model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static Utils.EndPoints.*;
import static io.restassured.RestAssured.given;

public class UserSteps {
    @Step("Создание уникального пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .post(USE_REG);

    }

    @Step("Логин пользователя")
    public Response userLogin(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN);

    }

    @Step("Получение токена")
    public String getToken(User user) {
        return userLogin(user)
                .then()
                .extract()
                .path("accessToken");

    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .when()
                .delete(USER);

    }

    @Step("Изменение данных у пользователя с авторизацией")
    public Response updateAuthUser(String token, User user) {
        return given()
                .header("authorization", token)
                .body(user)
                .patch(USER);
    }

    @Step("Изменение данных у пользователя без авторизации")
    public Response updateNotAuthUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(USER);
    }
}
