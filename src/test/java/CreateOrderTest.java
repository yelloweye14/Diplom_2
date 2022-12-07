import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import User.User;
import User.UserClient;
import User.Credentials;



import static org.junit.Assert.*;

public class CreateOrderTest {
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
    @DisplayName("Тест создания заказа авторизованным пользователем")
    @Description("Проверка возможности создания заказа с ингредиентами авторизованным пользователем, "+
            "возвращается \"Флюоресцентный бессмертный бургер\" и код ответа 200.")
    public void createOrderAuthorizedUserWithIngredients() {
        Credentials credentials = Credentials.from(user);
        String token = userClient.login(credentials).extract().path("accessToken");
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        userClient.createOrder(token, json).statusCode(200);
        String name = userClient.createOrder(token, json).extract().path("name");
        assertEquals("Неверный код ответа","Флюоресцентный бессмертный бургер", name);
    }

    @Test
    @DisplayName("Тест создания заказа неавторизованным пользователем")
    @Description("Проверка возможности создания заказа с ингредиентами неавторизованным пользователем, "+
            "возвращается \"Флюоресцентный бессмертный бургер\" и код ответа 200.")
    public void createOrderUnauthorizedUserWithIngredients() {
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        userClient.createOrderWithoutAuthorization(json).statusCode(200);
        String name = userClient.createOrderWithoutAuthorization(json).extract().path("name");
        assertEquals("Неверный код ответа","Флюоресцентный бессмертный бургер", name);
    }

    @Test
    @DisplayName("Тест создания заказа без ингредиентов авторизованным пользователем")
    @Description("Проверка невозможности создания заказа без ингредиентов авторизованным пользователем, "+
            "возвращается \"Ingredient ids must be provided\" и код ответа 400.")
    public void createOrderAuthorizedUserWithoutIngredients() {
        Credentials credentials = Credentials.from(user);
        String token = userClient.login(credentials).extract().path("accessToken");
        String json = "{\"ingredients\": []}";
        userClient.createOrder(token, json).statusCode(400);
        String massage = userClient.createOrder(token, json).extract().path("message");
        assertEquals("Неверный код ответа","Ingredient ids must be provided", massage);
    }

    @Test
    @DisplayName("Тест создания заказа без ингредиентов неавторизованным пользователем")
    @Description("Проверка невозможности создания заказа без ингредиентов неавторизованным пользователем, "+
            "возвращается \"Ingredient ids must be provided\" и код ответа 400.")
    public void createOrderUnauthorizedUserWithoutIngredients() {
        String json = "{\"ingredients\": []}";
        userClient.createOrderWithoutAuthorization(json).statusCode(400);
        String message = userClient.createOrderWithoutAuthorization(json).extract().path("message");
        assertEquals("Неверный код ответа","Ingredient ids must be provided", message);
    }

    @Test
    @DisplayName("Тест передачи хеша ингредиента")
    @Description("Проверка возврата кода ответа 500 при передече невалидного хеша ингридиента")
    public void createOrderWithWrongIngredients() {
        Credentials credentials = Credentials.from(user);
        String token = userClient.login(credentials).extract().path("accessToken");
        String json = "{\"ingredients\": [\"съедобно\",\"несъедобно\"]}";
        userClient.createOrder(token, json).statusCode(500);

    }
}
