import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import User.User;
import User.UserClient;
import User.Credentials;

import static org.junit.Assert.assertEquals;

public class GetOrdersTest {
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
    @DisplayName("Тест получения заказа авторизванным пользователем")
    @Description("Проверка возможности получения заказа авторизованным пользователем")
    public void getOrdersAuthorizedUser() {
        Credentials credentials = Credentials.from(user);
        String token = userClient.login(credentials).extract().path("accessToken");
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        int total = userClient.getOrders(token).extract().path("total");
        userClient.createOrder(token, json);
        userClient.getOrders(token);
        int newTotal = userClient.getOrders(token).extract().path("total");
        System.out.println(total);
        assertEquals(total + 1, newTotal);
    }

    @Test
   @DisplayName("Тест получения заказа неавторизванным пользователем")
   @Description("Проверка невозможности получения заказа авторизованным пользователем и возврата ошибки с текстом " +
            "You should be authorised")
    public void getOrdersUnauthorizedUser() {
        String message = userClient.getOrdersWithoutAuth().extract().path("message");
        assertEquals("You should be authorised", message);
    }
}
