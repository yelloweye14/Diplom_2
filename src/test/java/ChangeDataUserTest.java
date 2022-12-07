import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import User.User;
import User.UserClient;
import User.Credentials;



public class ChangeDataUserTest {
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
    @DisplayName("Тест изменения пользовательских данных авторизованным пользователем")
    @Description("Проверка возможности изменения пользовательских данных авторизованным пользователем")
    public void changeDataAuthorizedUser() {
        Credentials credentials = Credentials.from(user);
        String token = userClient.login(credentials)
                .extract().path("accessToken");
        String email = userClient.login(credentials)
                .extract().path("user.email");
        String name = userClient.login(credentials)
                .extract().path("user.name");
        userClient.getDataUser(token);
        String json = "{\"email\": \"" + RandomStringUtils.randomAlphabetic(6) + "@ru.ru\", \"name\": \"" + RandomStringUtils.randomAlphabetic(6) + "\"}";
        String newName = userClient.changeDataUser(token, json)
                .extract().body().path("user.name");
        String newEmail = userClient.changeDataUser(token, json)
                .extract().body().path("user.email");
        Assert.assertNotEquals(name, newName);
        Assert.assertNotEquals(email, newEmail);
        String jsonNew = "{\"email\": \"" + email + "\", \"name\": \"" + name + "\"}";
        userClient.changeDataUser(token, jsonNew);
    }

    @Test
    @DisplayName("Тест изменения пользовательских данных неавторизованным пользователем")
    @Description("Проверка невозможности изменения пользовательских данных неавторизованным пользователем")
    public void changeDataUnauthorizedUser() {
        Credentials credentials = Credentials.from(user);
        userClient.login(credentials);
        String json = "{\"email\": \"" + RandomStringUtils.randomAlphabetic(6) + "@ya.ru\", \"name\": \"" + RandomStringUtils.randomAlphabetic(6) + "\"}";
        userClient.changeDataUserWithoutAuthorization(json)
                .statusCode(401);
        String message = userClient.changeDataUserWithoutAuthorization(json)
                .extract().body().path("message");
        Assert.assertEquals("You should be authorised", message);
    }
}
