import Model.User;
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

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BeforeTest {
    private UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;

    @Before
    public void SetUp() {
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@ya.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.createUser(user);
        accessToken = userSteps.getToken(user);
    }

    @Test
    @DisplayName("Авторизация пользователя под существющим логином")
    @Description("Авторизация выполнена. Успешный ответ")
    public void authLoginUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Response response = userSteps.userLogin(user);
        response.then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация пользователя c некорректным логином (email)")
    @Description("Авторизация не выполнена. Ошибка 401")
    public void authIncorrectLoginUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        String login = user.getEmail();
        user.setEmail(login + "incorrect");
        Response response = userSteps.userLogin(user);
        response.then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя c некорректным паролем")
    @Description("Авторизация не выполнена. Ошибка 401")
    public void authIncorrectPasswordUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        String password = user.getPassword();
        user.setPassword(password + "incorrect");
        Response response = userSteps.userLogin(user);
        response.then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void deleteUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
