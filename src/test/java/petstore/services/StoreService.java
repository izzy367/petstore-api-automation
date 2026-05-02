package petstore.services;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import petstore.models.request.Order;

import static io.restassured.RestAssured.given;

/**
 * StoreService — Petstore Store API-ის "POM" კლასი.
 * ყველა Store API call (POST/GET/DELETE order) ცხოვრობს აქ.
 * ტესტები აქედან გამოიძახებენ მეთოდებს, არ წერენ HTTP details-ს.
 */
public class StoreService {

    private static final String STORE_ORDER_ENDPOINT = "/store/order";
    private static final String STORE_ORDER_BY_ID = "/store/order/{orderId}";

    /**
     * POST /store/order — ახალი შეკვეთის განთავსება.
     */
    @Step("Place an order: {order}")
    public Response placeOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(STORE_ORDER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * GET /store/order/{orderId} — შეკვეთის წამოღება ID-ით.
     */
    @Step("Get order by ID: {orderId}")
    public Response getOrderById(Long orderId) {
        return given()
                .pathParam("orderId", orderId)
                .when()
                .get(STORE_ORDER_BY_ID)
                .then()
                .extract()
                .response();
    }

    /**
     * DELETE /store/order/{orderId} — შეკვეთის წაშლა.
     */
    @Step("Delete order by ID: {orderId}")
    public Response deleteOrderById(Long orderId) {
        return given()
                .pathParam("orderId", orderId)
                .when()
                .delete(STORE_ORDER_BY_ID)
                .then()
                .extract()
                .response();
    }
}