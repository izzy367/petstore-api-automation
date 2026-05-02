package petstore.tests.store;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import petstore.base.BaseTest;
import petstore.models.request.Order;
import petstore.models.response.ApiResponse;
import petstore.services.StoreService;

/**
 * Scenario 1: Store API Tests
 *
 * სრული end-to-end სცენარი:
 * 1. POST შეკვეთის განთავსება
 * 2. GET იგივე შეკვეთის წამოღება
 * 3. DELETE შეკვეთის წაშლა
 * 4. DELETE ისევ იგივე — 404
 * 5. GET ისევ იგივე — 404
 *
 * Test order-ი დადგენილია priority-ით (TestNG default).
 * Tests share state-ს (orderId) კლასის field-ის საშუალებით.
 */
@Epic("Petstore API")
@Feature("Store Operations")
public class StoreScenarioTests extends BaseTest {

    /**
     * StoreService instance — ერთი მთელ კლასისთვის.
     * "POM" კლასი HTTP details-ს მართავს.
     */
    private final StoreService storeService = new StoreService();

    /**
     * Test data — order, რომელიც ჩვენ გავაგზავნით API-ში.
     * Unique ID — System.currentTimeMillis() — რომ ყოველ run-ზე სხვა ID იყოს.
     * (Petstore public sandbox — ID conflict-ი შესაძლებელია)
     */
    private static final Long ORDER_ID = System.currentTimeMillis();
    private static final Long PET_ID = 12345L;
    private static final Integer QUANTITY = 2;
    private static final String STATUS = "placed";

    /**
     * Test 1: POST /store/order — შეკვეთის განთავსება
     */
    @Test(priority = 1, description = "POST /store/order — place a new order")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ვამოწმებთ ახალი შეკვეთის წარმატებით განთავსებას — სტატუს კოდი 200, body parameters შესაბამისად")
    public void placeOrderTest() {
        // Arrange — Order POJO ვქმნით Lombok @Builder-ით
        Order order = Order.builder()
                .id(ORDER_ID)
                .petId(PET_ID)
                .quantity(QUANTITY)
                .status(STATUS)
                .complete(true)
                .build();

        // Act — POST request-ი StoreService-ით
        Response response = storeService.placeOrder(order);

        // Assert — status code-ი + body parameters
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        Order responseBody = response.as(Order.class);
        Assert.assertEquals(responseBody.getId(), ORDER_ID, "Returned id should match the sent id");
        Assert.assertEquals(responseBody.getPetId(), PET_ID, "Returned petId should match");
        Assert.assertEquals(responseBody.getQuantity(), QUANTITY, "Returned quantity should match");
        Assert.assertEquals(responseBody.getStatus(), STATUS, "Returned status should match");

        System.out.println("✅ Order placed successfully. Order ID: " + responseBody.getId());
    }

    /**
     * Test 2: GET /store/order/{id} — შეკვეთის წამოღება
     */
    @Test(priority = 2, description = "GET /store/order/{id} — find purchase order by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ვამოწმებთ რომ გაკეთებული შეკვეთა ხელმისაწვდომია GET-ით")
    public void getOrderByIdTest() {
        // Act
        Response response = storeService.getOrderById(ORDER_ID);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        Order responseBody = response.as(Order.class);
        Assert.assertEquals(responseBody.getId(), ORDER_ID, "Returned id should match");
        Assert.assertEquals(responseBody.getPetId(), PET_ID, "Returned petId should match");
        Assert.assertEquals(responseBody.getQuantity(), QUANTITY, "Returned quantity should match");
        Assert.assertEquals(responseBody.getStatus(), STATUS, "Returned status should match");

        System.out.println("✅ Order retrieved successfully. Order ID: " + responseBody.getId());
    }

    /**
     * Test 3: DELETE /store/order/{id} — შეკვეთის წაშლა
     */
    @Test(priority = 3, description = "DELETE /store/order/{id} — delete purchase order")
    @Severity(SeverityLevel.NORMAL)
    @Description("ვამოწმებთ შეკვეთის წარმატებით წაშლას")
    public void deleteOrderTest() {
        // Act
        Response response = storeService.deleteOrderById(ORDER_ID);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200 on successful delete");

        System.out.println("✅ Order deleted successfully. Order ID: " + ORDER_ID);
    }

    /**
     * Test 4: DELETE იგივე ID — 404 (ორჯერ წაშლის ცდა)
     */
    @Test(priority = 4, description = "DELETE again — should return 404 Order not found")
    @Severity(SeverityLevel.NORMAL)
    @Description("ვამოწმებთ რომ უკვე წაშლილი შეკვეთის ხელახლა წაშლა აბრუნებს 404")
    public void deleteAlreadyDeletedOrderTest() {
        // Act
        Response response = storeService.deleteOrderById(ORDER_ID);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 404, "Status code should be 404 for non-existent order");

        ApiResponse errorBody = response.as(ApiResponse.class);
        Assert.assertTrue(
                errorBody.getMessage().equalsIgnoreCase("Order not found"),
                "Error message should be 'Order not found' (case-insensitive). Actual: " + errorBody.getMessage()
        );

        System.out.println("✅ Verified: deleting non-existent order returns 404");
    }

    /**
     * Test 5: GET იგივე ID წაშლის შემდეგ — 404
     */
    @Test(priority = 5, description = "GET deleted order — should return 404")
    @Severity(SeverityLevel.NORMAL)
    @Description("ვამოწმებთ რომ წაშლილი შეკვეთის წამოღება აბრუნებს 404")
    public void getDeletedOrderTest() {
        // Act
        Response response = storeService.getOrderById(ORDER_ID);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 404, "Status code should be 404 for non-existent order");

        ApiResponse errorBody = response.as(ApiResponse.class);
        Assert.assertTrue(
                errorBody.getMessage().equalsIgnoreCase("Order not found"),
                "Error message should be 'Order not found' (case-insensitive). Actual: " + errorBody.getMessage()
        );

        System.out.println("✅ Verified: GET on deleted order returns 404");
    }
}