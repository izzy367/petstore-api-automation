package petstore.dataproviders;

import org.testng.annotations.DataProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * DataProvider for Negative Store API tests.
 *
 * აბრუნებს 3 ცალკე scenario:
 * 1. Invalid id (string ნაცვლად Long-ისა)
 * 2. Invalid petId (string ნაცვლად Long-ისა)
 * 3. Invalid quantity (string ნაცვლად Integer-ისა)
 *
 * ყოველი row არის ცალცალკე test run.
 * Test method ერთხელ წერია, მაგრამ TestNG-ი მას სამჯერ გაუშვებს —
 * ყოველ ჯერზე სხვა row-ის parameters-ით.
 *
 * @DataProvider name="invalidOrderData" — ეს name-ი ცხადდება Test-ში:
 *   @Test(dataProvider = "invalidOrderData", dataProviderClass = ...class)
 */
public class NegativeOrderDataProvider {

    /**
     * აბრუნებს Object[][] — 3 row, თითო row-ში:
     *   - body: invalid order data (Map)
     *   - testCaseName: ცხადი სახელი ლოგინგისთვის
     */
    @DataProvider(name = "invalidOrderData")
    public static Object[][] invalidOrderData() {
        return new Object[][]{
                { buildInvalidIdBody(),       "Invalid id (string instead of Long)" },
                { buildInvalidPetIdBody(),    "Invalid petId (string instead of Long)" },
                { buildInvalidQuantityBody(), "Invalid quantity (string instead of Integer)" }
        };
    }

    /**
     * Body 1: id-ში string ("test"), დანარჩენი valid.
     */
    private static Map<String, Object> buildInvalidIdBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", "test");           // INVALID — String ნაცვლად Long-ისა
        body.put("petId", 12345);
        body.put("quantity", 2);
        body.put("status", "placed");
        body.put("complete", true);
        return body;
    }

    /**
     * Body 2: petId-ში string ("test"), დანარჩენი valid.
     */
    private static Map<String, Object> buildInvalidPetIdBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", 1L);
        body.put("petId", "test");        // INVALID
        body.put("quantity", 2);
        body.put("status", "placed");
        body.put("complete", true);
        return body;
    }

    /**
     * Body 3: quantity-ში string ("test"), დანარჩენი valid.
     */
    private static Map<String, Object> buildInvalidQuantityBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", 1L);
        body.put("petId", 12345);
        body.put("quantity", "test");     // INVALID
        body.put("status", "placed");
        body.put("complete", true);
        return body;
    }
}