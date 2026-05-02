package petstore.services;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import petstore.models.request.User;

import static io.restassured.RestAssured.given;

/**
 * UserService — Petstore User API-ის "POM" კლასი.
 * ყველა User API call ცხოვრობს აქ.
 */
public class UserService {

    private static final String USER_ENDPOINT = "/user";
    private static final String USER_BY_USERNAME = "/user/{username}";
    private static final String USER_LOGIN = "/user/login";
    private static final String USER_LOGOUT = "/user/logout";

    /**
     * POST /user — ახალი მომხმარებლის შექმნა.
     */
    @Step("Create a new user: {user}")
    public Response createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(USER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * GET /user/{username} — მომხმარებლის წამოღება username-ით.
     */
    @Step("Get user by username: {username}")
    public Response getUserByUsername(String username) {
        return given()
                .pathParam("username", username)
                .when()
                .get(USER_BY_USERNAME)
                .then()
                .extract()
                .response();
    }

    /**
     * PUT /user/{username} — მომხმარებლის მონაცემების განახლება.
     */
    @Step("Update user: {username}")
    public Response updateUser(String username, User updatedUser) {
        return given()
                .contentType(ContentType.JSON)
                .pathParam("username", username)
                .body(updatedUser)
                .when()
                .put(USER_BY_USERNAME)
                .then()
                .extract()
                .response();
    }

    /**
     * GET /user/login — სისტემაში შესვლა.
     */
    @Step("Login user: {username}")
    public Response loginUser(String username, String password) {
        return given()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get(USER_LOGIN)
                .then()
                .extract()
                .response();
    }

    /**
     * GET /user/logout — სისტემიდან გასვლა.
     */
    @Step("Logout current user")
    public Response logoutUser() {
        return given()
                .when()
                .get(USER_LOGOUT)
                .then()
                .extract()
                .response();
    }

    /**
     * DELETE /user/{username} — მომხმარებლის წაშლა.
     */
    @Step("Delete user by username: {username}")
    public Response deleteUserByUsername(String username) {
        return given()
                .pathParam("username", username)
                .when()
                .delete(USER_BY_USERNAME)
                .then()
                .extract()
                .response();
    }
}