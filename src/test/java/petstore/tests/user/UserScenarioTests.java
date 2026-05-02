package petstore.tests.user;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import petstore.base.BaseTest;
import petstore.models.request.User;
import petstore.models.response.ApiResponse;
import petstore.services.UserService;

/**
 * Scenario 2: User API Tests
 *
 * სრული end-to-end სცენარი User API-სთვის:
 * 1. POST    — Create user
 * 2. GET     — Get user by username (verify creation)
 * 3. PUT     — Update user phone
 * 4. GET     — Verify update
 * 5. GET     — Login
 * 6. GET     — Logout
 * 7. DELETE  — Delete user
 *
 * Tests share state-ს (USERNAME) კლასის field-ის საშუალებით.
 */
@Epic("Petstore API")
@Feature("User Operations")
public class UserScenarioTests extends BaseTest {

    /**
     * UserService instance — ერთი მთელ კლასისთვის.
     */
    private final UserService userService = new UserService();

    /**
     * Test data — unique username ყოველ run-ზე.
     * "izzy_user_" + currentTimeMillis — Petstore conflict-ის თავიდან აცილება.
     */
    private static final String USERNAME = "izzy_user_" + System.currentTimeMillis();
    private static final Long USER_ID = System.currentTimeMillis();
    private static final String FIRST_NAME = "Isidore";
    private static final String LAST_NAME = "Test";
    private static final String EMAIL = "izzy@petstore.test";
    private static final String PASSWORD = "Test1234!";
    private static final String ORIGINAL_PHONE = "555111111";
    private static final String UPDATED_PHONE = "555999999";   // Test 3-ში დავუაფდეითებთ
    private static final Integer USER_STATUS = 1;

    /**
     * Test 1: POST /user — ახალი მომხმარებლის შექმნა
     */
    @Test(priority = 1, description = "POST /user — create a new user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ვამოწმებთ ახალი მომხმარებლის შექმნას — სტატუს კოდი 200")
    public void createUserTest() {
        // Arrange — User POJO-ს ვქმნით Lombok @Builder-ით
        User user = User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phone(ORIGINAL_PHONE)
                .userStatus(USER_STATUS)
                .build();

        // Act
        Response response = userService.createUser(user);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        System.out.println("✅ User created successfully. Username: " + USERNAME);
    }

    /**
     * Test 2: GET /user/{username} — შექმნის verification
     * ვამოწმებთ რომ ყველა field-ი სწორად შეინახა.
     */
    @Test(priority = 2, description = "GET /user/{username} — verify user creation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ვამოწმებთ რომ შექმნილი მომხმარებლის ყველა მონაცემი სწორად შეინახა")
    public void getUserAfterCreationTest() {
        // Act
        Response response = userService.getUserByUsername(USERNAME);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        User responseBody = response.as(User.class);
        Assert.assertEquals(responseBody.getUsername(), USERNAME, "Username should match");
        Assert.assertEquals(responseBody.getFirstName(), FIRST_NAME, "FirstName should match");
        Assert.assertEquals(responseBody.getLastName(), LAST_NAME, "LastName should match");
        Assert.assertEquals(responseBody.getEmail(), EMAIL, "Email should match");
        Assert.assertEquals(responseBody.getPassword(), PASSWORD, "Password should match");
        Assert.assertEquals(responseBody.getPhone(), ORIGINAL_PHONE, "Phone should match original");
        Assert.assertEquals(responseBody.getUserStatus(), USER_STATUS, "UserStatus should match");

        System.out.println("✅ User retrieved successfully with all fields. Username: " + USERNAME);
    }

    /**
     * Test 3: PUT /user/{username} — phone-ის განახლება
     */
    @Test(priority = 3, description = "PUT /user/{username} — update user phone")
    @Severity(SeverityLevel.NORMAL)
    @Description("ვამოწმებთ მომხმარებლის phone field-ის განახლებას")
    public void updateUserTest() {
        // Arrange — იგივე user, ოღონდ ახალი phone
        User updatedUser = User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phone(UPDATED_PHONE)        // ← მხოლოდ ეს შეიცვალა
                .userStatus(USER_STATUS)
                .build();

        // Act
        Response response = userService.updateUser(USERNAME, updatedUser);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        System.out.println("✅ User updated successfully. New phone: " + UPDATED_PHONE);
    }

    /**
     * Test 4: GET /user/{username} — update-ის verification
     * ვამოწმებთ რომ phone რეალურად შეიცვალა.
     */
    @Test(priority = 4, description = "GET /user/{username} — verify phone was updated")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ვამოწმებთ რომ PUT-ის შემდეგ მართლაც დაბრუნდა ახალი phone number")
    public void getUserAfterUpdateTest() {
        // Act
        Response response = userService.getUserByUsername(USERNAME);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        User responseBody = response.as(User.class);
        Assert.assertEquals(responseBody.getPhone(), UPDATED_PHONE,
                "Phone should be updated to new value");

        System.out.println("✅ Verified: phone updated from " + ORIGINAL_PHONE + " to " + UPDATED_PHONE);
    }

    /**
     * Test 5: GET /user/login — სისტემაში შესვლა
     */
    @Test(priority = 5, description = "GET /user/login — login user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ვამოწმებთ მომხმარებლის სისტემაში შესვლას")
    public void loginUserTest() {
        // Act
        Response response = userService.loginUser(USERNAME, PASSWORD);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        ApiResponse responseBody = response.as(ApiResponse.class);
        Assert.assertNotNull(responseBody.getMessage(), "Message should not be null");
        Assert.assertFalse(responseBody.getMessage().isEmpty(), "Message should not be empty");

        System.out.println("✅ User logged in. Message: " + responseBody.getMessage());
    }

    /**
     * Test 6: GET /user/logout — სისტემიდან გასვლა
     */
    @Test(priority = 6, description = "GET /user/logout — logout user")
    @Severity(SeverityLevel.NORMAL)
    @Description("ვამოწმებთ მომხმარებლის სისტემიდან გასვლას")
    public void logoutUserTest() {
        // Act
        Response response = userService.logoutUser();

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        ApiResponse responseBody = response.as(ApiResponse.class);
        Assert.assertEquals(responseBody.getMessage(), "ok", "Logout message should be 'ok'");

        System.out.println("✅ User logged out successfully");
    }

    /**
     * Test 7: DELETE /user/{username} — მომხმარებლის წაშლა
     */
    @Test(priority = 7, description = "DELETE /user/{username} — delete user")
    @Severity(SeverityLevel.NORMAL)
    @Description("ვამოწმებთ მომხმარებლის წაშლას — სტატუს 200 + message = username")
    public void deleteUserTest() {
        // Act
        Response response = userService.deleteUserByUsername(USERNAME);

        // Assert
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        ApiResponse responseBody = response.as(ApiResponse.class);
        Assert.assertEquals(responseBody.getMessage(), USERNAME,
                "Delete response message should be the deleted username");

        System.out.println("✅ User deleted successfully. Username: " + USERNAME);
    }
}