package petstore.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;
import petstore.database.DatabaseManager;

/**
 * BaseTest — ყველა ტესტ კლასის parent.
 * ერთხელ აყენებს RestAssured + Allure-ის კონფიგურაციას,
 * და ცოცხალს ხდის H2 in-memory database-ს test data-სთვის.
 */
public class BaseTest {

    protected static final String BASE_URI = "https://petstore.swagger.io/v2";

    @BeforeSuite
    public void setUp() {
        // 1. RestAssured base URI
        RestAssured.baseURI = BASE_URI;

        // 2. Allure + console logging filters
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );

        // 3. H2 database initialization — test data ჩატვირთვა
        DatabaseManager.initializeDatabase();

        System.out.println("✅ BaseTest setup completed. Base URI: " + BASE_URI);
    }
}