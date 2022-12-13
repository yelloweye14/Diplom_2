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
    @DisplayName("Тест на авторизацию существующим пользователем")
    @Description("Проверка возможности авторизации существующим пользователем")
    public void loginExistenceUserTest() {
        Credentials credentials = Credentials.from(user);
        Boolean isOk = userClient.login(credentials).extract().path("success");
        assertTrue(isOk);
    }

    @Test
    @DisplayName("Тест на авторизацию несуществующим пользователем")
    @Description("Проверка невозможности авторизации несуществующим пользователем")
    public void loginWithWrongLoginPasswordTest() {
        Credentials credentials = Credentials.getWrongLoginPassword();
        String message = userClient.login(credentials).extract().path("message");
        assertEquals("email or password are incorrect", message);
    }

    @Test
    @DisplayName("Тест на авторизацию существующего пользователя c неправильным логином")
    @Description("Проверка невозможности авторизации существующим пользователем с неправильным логином")
    public void loginWithWrongLoginTest() {
        Credentials credentialsWithWrongLogin = Credentials.getWrongLogin(user);
        String message = userClient.login(credentialsWithWrongLogin).extract().path("message");
        assertEquals("email or password are incorrect", message);
    }
    @Test
    @DisplayName("Тест на авторизацию существующего пользователя без логина")
    @Description("Проверка невозможности авторизации существующим пользователем без заполненного логина")
    public void loginWithEmptyLoginTest() {
        Credentials credentialsWithEmptyLogin = Credentials.getEmptyLogin(user);
        String message = userClient.login(credentialsWithEmptyLogin).extract().path("message");
        assertEquals("email or password are incorrect", message);
    }

    @Test
    @DisplayName("Тест на авторизацию существующего пользователя с неправильным паролем")
    @Description("Проверка невозможности авторизации существующим пользователем с неправильным паролем")
    public void loginWithWrongPasswordTest() {
        Credentials credentialsWithWrongPassword = Credentials.getWrongPassword(user);
        String message = userClient.login(credentialsWithWrongPassword).extract().path("message");
        assertEquals("email or password are incorrect", message);
    }
    @Test
    @DisplayName("Тест на авторизацию существующего пользователя без пароля")
    @Description("Проверка невозможности авторизации существующим пользователем без заполненного пароля")
    public void loginWithEmptyPasswordTest() {
        Credentials credentialsWithEmptyPassword = Credentials.getEmptyPassword(user);
        String message = userClient.login(credentialsWithEmptyPassword).extract().path("message");
        assertEquals("email or password are incorrect", message);
    }
}
