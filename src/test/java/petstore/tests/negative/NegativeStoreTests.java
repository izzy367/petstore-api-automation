package petstore.tests.negative;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import petstore.base.BaseTest;
import petstore.dataproviders.NegativeOrderDataProvider;
import petstore.models.response.ApiResponse;
import petstore.services.StoreService;

import java.util.Map;

/**
 * Negative tests for Petstore Store API.
 *
 * ფინალური მოთხოვნა:
 * - Invalid id, petId, quantity — სამივეზე ცალცალკე test
 * - მოლოდინი: 500 + message "something bad happened"
 *
 * Implementation:
 * - ერთი @Test მეთოდი + @DataProvider — 3 run-ი ცალცალკე scenario-ით
 * - DataProvider აბრუნებს Map<String, Object> body-ის (ფლექსიბილური type-ებისთვის)
 */
@Epic("Petstore API")
@Feature("Negative — Store Operations")
public class NegativeStoreTests extends BaseTest {

    private final StoreService storeService = new StoreService();

    /**
     * ერთი @Test მეთოდი — TestNG-ი მას 3-ჯერ გაუშვებს DataProvider-ის თითო row-ით.
     *
     * @param body          invalid request body Map-ად
     * @param testCaseName  ცხადი სახელი ლოგინგისთვის
     */
    @Test(
            dataProvider = "invalidOrderData",
            dataProviderClass = NegativeOrderDataProvider.class,
            description = "POST /store/order with invalid data — should return 500"
    )
    @Severity(SeverityLevel.NORMAL)
    @Description("ვამოწმებთ რომ invalid type-ის ველზე API აბრუნებს 500 და message 'something bad happened'")
    public void placeOrderWithInvalidDataTest(Map<String, Object> body, String testCaseName) {
        System.out.println("▶️ Running negative test: " + testCaseName);

        // Act
        Response response = storeService.placeOrderWithRawBody(body);

        // Assert — status code 500
        Assert.assertEquals(response.getStatusCode(), 500,
                "Status code should be 500 for invalid data. Test case: " + testCaseName);

        // Assert — error message
        ApiResponse errorBody = response.as(ApiResponse.class);
        Assert.assertTrue(
                errorBody.getMessage().equalsIgnoreCase("something bad happened"),
                "Error message should be 'something bad happened' (case-insensitive). " +
                        "Actual: " + errorBody.getMessage() + ". Test case: " + testCaseName
        );

        System.out.println("✅ Negative test passed: " + testCaseName);
    }
}