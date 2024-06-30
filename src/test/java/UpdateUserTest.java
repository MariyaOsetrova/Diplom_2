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

public class UpdateUserTest extends BeforeTest {
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
    @DisplayName("Изменение email с авторизацией")
    @Description("Email изменен.  Успешный ответ")
    public void updateEmailUserAuth() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        userSteps.userLogin(user);
        String newEmale = user.getEmail();
        user.setEmail("new" + newEmale);
        Response response = userSteps.updateAuthUser(accessToken, user);
        response.then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение password с авторизацией")
    @Description("Password изменен.  Успешный ответ")
    public void updatePasswordUserAuth() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        userSteps.userLogin(user);
        String newPassword = user.getPassword();
        user.setPassword("new" + newPassword);
        Response response = userSteps.updateAuthUser(accessToken, user);
        response.then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение name с авторизацией")
    @Description("Name изменен.  Успешный ответ")
    public void updateNameUserAuth() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        userSteps.userLogin(user);
        String newName = user.getName();
        user.setName("new" + newName);
        Response response = userSteps.updateAuthUser(accessToken, user);
        response.then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение name без авторизации")
    @Description("Name не изменен.  Ошибка 401")
    public void updateNameUserNotAuth() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        String newName = user.getName();
        user.setName("new" + newName);
        Response response = userSteps.updateNotAuthUser(user);
        response.then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение Password без авторизации")
    @Description("Password не изменен.  Ошибка 401")
    public void updatePasswordUserNotAuth() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        String newPassword = user.getPassword();
        user.setPassword("new" + newPassword);
        Response response = userSteps.updateNotAuthUser(user);
        response.then()
                .statusCode(401)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение Email без авторизации")
    @Description("Email не изменен.  Ошибка 401")
    public void updateEmailUserNotAuth() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        String newEmail = user.getEmail();
        user.setEmail("new" + newEmail);
        Response response = userSteps.updateNotAuthUser(user);
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
