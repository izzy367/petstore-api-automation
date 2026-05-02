package petstore.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;

/**
 * BaseTest — ყველა ტესტ კლასის parent.
 * აყენებს RestAssured-ის კონფიგურაციას ერთხელ მთელი test suite-ისთვის.
 */
public class BaseTest {

    protected static final String BASE_URI = "https://petstore.swagger.io/v2";

    @BeforeSuite
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );

        System.out.println("✅ BaseTest setup completed. Base URI: " + BASE_URI);
    }
}