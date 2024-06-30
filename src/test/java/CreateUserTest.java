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


public class CreateUserTest extends BeforeTest {
    private UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;

    @Before
    public void SetUp() {
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@ya.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Пользователь создан. Успешный ответ")
    public void createUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Response response = userSteps.createUser(user);
        response.then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
        ;
        accessToken = userSteps.getToken(user);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Пользователь не создан. Ошибка 403 Forbidden")
    public void createDoubleUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        userSteps.createUser(user);
        Response response = userSteps.createUser(user);
        response.then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("message", equalTo("User already exists"));
        accessToken = userSteps.getToken(user);
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля email")
    @Description("Пользователь не создан. 403 Forbidden")
    public void createUserWithoutEmail() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user.getEmail();
        user.setEmail("");
        Response response = userSteps.createUser(user);
        response.then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля password")
    @Description("Пользователь не создан. 403 Forbidden")
    public void createUserWithoutPassword() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user.getPassword();
        user.setPassword("");
        Response response = userSteps.createUser(user);
        response.then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля name")
    @Description("Пользователь не создан. 403 Forbidden")
    public void createUserWithoutName() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user.getName();
        user.setName("");
        Response response = userSteps.createUser(user);
        response.then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void deleteUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
