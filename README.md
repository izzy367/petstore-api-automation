# Petstore API Automation

Automated API tests for the [Swagger Petstore API](https://petstore.swagger.io/) using REST Assured, TestNG, Lombok, and Allure reporting.

## 🛠 Tech Stack

- **Java 25** — programming language
- **Maven** — build & dependency management
- **TestNG 7.11.0** — test framework
- **REST Assured 5.5.1** — API testing library
- **Lombok 1.18.42** — boilerplate reduction (`@Data`, `@Builder`, `@Getter`)
- **Jackson 2.18.2** — JSON serialization/deserialization
- **Allure 2.29.0** — test reporting
- **H2 Database 2.3.232** — in-memory SQL database for DataProvider
- **AspectJ Weaver** — runtime support for Allure annotations

## 📁 Project Structure

```
petstore-api-automation/
├── src/
│   ├── main/java/petstore/
│   │   └── models/
│   │       ├── request/
│   │       │   ├── Order.java          // POJO with @Data, @Builder
│   │       │   └── User.java           // POJO with @Data, @Builder
│   │       └── response/
│   │           └── ApiResponse.java    // Generic API response
│   └── test/
│       ├── java/petstore/
│       │   ├── base/
│       │   │   └── BaseTest.java       // RestAssured + Allure + DB setup
│       │   ├── database/
│       │   │   └── DatabaseManager.java // H2 in-memory DB
│       │   ├── dataproviders/
│       │   │   ├── NegativeOrderDataProvider.java
│       │   │   └── UserDataProvider.java   // Reads from H2 SQL DB
│       │   ├── services/
│       │   │   ├── StoreService.java   // Store API client (POM pattern)
│       │   │   └── UserService.java    // User API client (POM pattern)
│       │   └── tests/
│       │       ├── store/
│       │       │   └── StoreScenarioTests.java   // 5 chained tests
│       │       ├── user/
│       │       │   ├── UserScenarioTests.java        // 7 chained tests
│       │       │   └── CreateUsersFromDbTest.java   // 3 DB-driven tests
│       │       └── negative/
│       │           └── NegativeStoreTests.java   // 3 DataProvider tests
│       └── resources/
│           ├── allure.properties
│           ├── categories.json
│           └── environment.properties
├── pom.xml
├── testng.xml
└── README.md
```

## ✅ Test Cases

Total: **18 tests** across 3 test scenarios.

### Scenario 1: Store API (5 tests)

End-to-end workflow on `/store/order` endpoint:

1. **POST `/store/order`** — Place a new order, expect status 200
2. **GET `/store/order/{id}`** — Retrieve the order, verify all fields
3. **DELETE `/store/order/{id}`** — Delete the order, expect status 200
4. **DELETE again** — Try deleting same order, expect 404 + "Order not found"
5. **GET deleted order** — Try retrieving deleted order, expect 404 + "Order not found"

### Scenario 2: User API (7 tests)

End-to-end workflow on `/user` endpoint:

1. **POST `/user`** — Create a new user
2. **GET `/user/{username}`** — Verify user creation, all fields match
3. **PUT `/user/{username}`** — Update user phone number
4. **GET `/user/{username}`** — Verify phone was updated
5. **GET `/user/login`** — Login user, verify message is non-empty
6. **GET `/user/logout`** — Logout user, expect message "ok"
7. **DELETE `/user/{username}`** — Delete user, expect message = username

### Database-Driven User Tests (3 tests)

Reads test users from H2 in-memory SQL database via `@DataProvider`:

- Test data: 3 users (junior, mid, senior) loaded from `test_users` table
- Each test creates the user in Petstore, then deletes for cleanup

### Negative Tests: Store API (3 tests)

Single `@Test` method runs 3 times via `@DataProvider`. Tests `POST /store/order` with invalid data:

1. Invalid `id` (string instead of Long)
2. Invalid `petId` (string instead of Long)
3. Invalid `quantity` (string instead of Integer)

Expected: status 500 + message "something bad happened"

## 🎯 Bonus Features Implemented

Per project requirements, the following bonus features are implemented:

- ✅ **`@DataProvider`** — used in negative tests + DB-driven user tests
- ✅ **Lombok** — `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` on POJOs
- ✅ **Database read** — H2 in-memory SQL database with real JDBC queries (`CREATE TABLE`, `INSERT`, `SELECT`)

## 🚀 How to Run

### Prerequisites

- **Java 25** installed and configured
- **Maven 3.6+** installed

### Run all tests

```bash
mvn clean test
```

This will:
1. Compile all source files
2. Initialize H2 in-memory database
3. Execute all 18 tests via TestNG suite (`testng.xml`)
4. Generate `allure-results/` folder with test data

## 📊 How to View Allure Report

### Prerequisites

- **Allure CLI** installed → [installation guide](https://allurereport.org/docs/install/)

### Generate and view report

After running `mvn clean test`, execute:

```bash
allure serve allure-results
```

This will start a local web server and open the report in your default browser.

### Report Sections

- **Overview** — summary of all tests + Environment info
- **Behaviors** — tests grouped by Epic → Feature → Story
- **Suites** — tests grouped by suite (TestNG structure)
- **Categories** — failure categorization (Product defects, Test defects, etc.)

## 👤 Author

**Isidore** — [izzy367](https://github.com/izzy367)

Final project for Smart Academy Software Testing course.