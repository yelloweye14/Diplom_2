import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import User.User;
import User.UserClient;


import static org.junit.Assert.assertEquals;

public class UserTest {
    User user;
    UserClient userClient;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void delete() {
        userClient.delete(user);
    }

    @Test
    @DisplayName("Тест создания пользователя")
    public void userCreateTest() {
        userClient.create(user)
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Тест невозможности создания пользователя, который уже зарегистрирован")
    public void userCreateAlreadyExistsTest() {
        userClient.create(user);
        String userAlreadyExists = userClient.create(user)
                .assertThat()
                .statusCode(403)
                .extract().path("message");
        assertEquals("User already exists", userAlreadyExists);
    }

    @Test
    @DisplayName("Тест невозможности создания пользователя с незаполненным обязательным полем ввода пароля")
    public void userCreateWithoutPasswordTest() {
        userClient.create(user);
        User userWithoutPassword = User.getWithoutPassword();
        String expected = userClient.create(userWithoutPassword)
                .assertThat()
                .statusCode(403)
                .extract().path("message");
        assertEquals("Email, password and name are required fields", expected);
    }
}
