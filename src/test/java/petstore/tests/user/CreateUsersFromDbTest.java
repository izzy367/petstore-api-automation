package petstore.tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import petstore.base.BaseTest;
import petstore.dataproviders.UserDataProvider;
import petstore.models.request.User;
import petstore.services.UserService;

/**
 * Test რომელიც DB-დან კითხულობს test data-ს და Petstore-ში ქმნის users-ს.
 *
 * Workflow:
 * 1. @BeforeSuite (BaseTest) — H2 DB ცოცხლდება, 3 records ჩაიყრება
 * 2. UserDataProvider — SQL query-ით კითხულობს users-ს
 * 3. ეს test method 3-ჯერ უშვება — თითო DB record-ით
 * 4. ყოველ ჯერზე — POST /user → user იქმნება, status 200
 *
 * ბონუს ფუნქციონალი:
 * - SQL DataProvider (database read)
 * - @DataProvider (parameterized testing)
 * - Lombok @Builder (User POJO)
 */
@Epic("Petstore API")
@Feature("User Operations — Database Driven")
public class CreateUsersFromDbTest extends BaseTest {

    private final UserService userService = new UserService();

    @Test(
            dataProvider = "usersFromDatabase",
            dataProviderClass = UserDataProvider.class,
            description = "POST /user — create user with data from H2 SQL database"
    )
    @Severity(SeverityLevel.NORMAL)
    @Description("ვამოწმებთ User-ის შექმნას H2 SQL database-დან წაკითხული test data-თი")
    public void createUserFromDatabaseTest(User user) {
        System.out.println("▶️ Creating user from DB data: " + user.getUsername());

        // Act
        Response response = userService.createUser(user);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200,
                "Status code should be 200 for: " + user.getUsername());

        // Cleanup — შექმნილი user წავშალოთ რომ Petstore-ში არ დაგროვდეს
        Response deleteResponse = userService.deleteUserByUsername(user.getUsername());
        Assert.assertEquals(deleteResponse.getStatusCode(), 200,
                "Cleanup delete should succeed for: " + user.getUsername());

        System.out.println("✅ User from DB created and cleaned up: " + user.getUsername());
    }
}