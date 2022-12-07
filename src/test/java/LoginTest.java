import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import User.User;
import User.UserClient;
import User.Credentials;

import static org.junit.Assert.*;

public class LoginTest {
    User user;
    UserClient userClient;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void delete() {
        userClient.delete(user);
    }

    @Test
    @DisplayName("Тест на логин существующим пользователем")
    @Description("Проверка возможности авторизации существующим пользователем")
    public void loginTest() {
        Credentials credentials = Credentials.from(user);
        Boolean isOk = userClient.login(credentials).extract().path("success");
        assertTrue(isOk);
    }

    @Test
    @DisplayName("Тест на логин несуществующим пользователем")
    @Description("Проверка невозможности авторизации с неверным логином и паролем")
    public void loginTestWithWrongLoginPassword() {
        Credentials credentials = Credentials.getWrongLoginPassword(user);
        String massage = userClient.login(credentials).extract().path("message");
        assertEquals("email or password are incorrect", massage);
    }
}
