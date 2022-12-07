package User;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static Config.Config.BASE_URL;
import static io.restassured.RestAssured.given;


public class UserClient {
    private final String ROOT = "/auth/register";
    private final String LOGIN = "/auth/login ";
    private final String USER = "/auth/user ";
    private final String ORDERS = "/orders ";
    private final String INGREDIENTS = "/ingredients ";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given().log().all().header("Content-Type", "application/json").baseUri(BASE_URL).body(user).when().post(ROOT).then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(User user) {
        Credentials credentials = Credentials.from(user);
        String token = login(credentials).extract().path("accessToken");
        return given().log().all().header("Content-Type", "application/json").baseUri(BASE_URL).body(user).auth().oauth2(token.substring(7)).when().delete(USER).then().log().all();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(Credentials credentials) {
        return given().log().all().header("Content-Type", "application/json").baseUri(BASE_URL).body(credentials).when().post(LOGIN).then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse getDataUser(String token) {
        return given().log().all().header("Content-Type", "application/json").baseUri(BASE_URL).auth().oauth2(token.substring(7)).get(USER).then().log().all();
    }

    @Step("Изменение данных авторизованного пользователя")
    public ValidatableResponse changeDataUser(String token, String json) {
        return given().log().all().baseUri(BASE_URL)
                //.auth().oauth2(token.substring(7))
                .header("Content-Type", "application/json").auth().oauth2(token.substring(7)).body(json).when().patch(USER).then().log().all();
    }

    @Step("Изменение данных неавторизованного пользователя")
    public ValidatableResponse changeDataUserWithoutAuthorization(String json) {
        return given().log().all().baseUri(BASE_URL).header("Content-Type", "application/json").body(json).when().patch(USER).then().log().all();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String token, String json) {
        return given().log().all().header("Content-Type", "application/json").body(json).baseUri(BASE_URL).auth().oauth2(token.substring(7)).post(ORDERS).then().log().all();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuthorization(String json) {
        return given().log().all().header("Content-Type", "application/json").body(json).baseUri(BASE_URL).post(ORDERS).then().log().all();
    }

    @Step("Получение списка заказов с авторизацией")
    public ValidatableResponse getOrders(String token) {
        return given().log().all().header("Content-Type", "application/json").baseUri(BASE_URL).auth().oauth2(token.substring(7)).get(ORDERS).then().log().all();
    }

    @Step("Получение списка заказов без авторизации")
    public ValidatableResponse getOrdersWithoutAuth() {
        return given().log().all().header("Content-Type", "application/json").baseUri(BASE_URL).get(ORDERS).then().log().all();
    }
}
