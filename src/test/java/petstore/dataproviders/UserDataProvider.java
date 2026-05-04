package petstore.dataproviders;

import org.testng.annotations.DataProvider;
import petstore.database.DatabaseManager;
import petstore.models.request.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DataProvider — H2 SQL database-დან ცხადი test users-ის წამოღება.
 *
 * Workflow:
 * 1. Connection ვაყენებთ DatabaseManager-ით
 * 2. SELECT query გავრიდებთ test_users ცხრილზე
 * 3. ცარიელი ResultSet-ი → User POJO objects ცხადად
 * 4. Object[][] აბრუნდება — DataProvider format-ი
 *
 * @DataProvider name="usersFromDatabase" — ცხადი ცილი ცხადი ცარიელი:
 *   @Test(dataProvider = "usersFromDatabase", dataProviderClass = ...class)
 */
public class UserDataProvider {

    @DataProvider(name = "usersFromDatabase")
    public static Object[][] usersFromDatabase() throws SQLException {
        // SQL query — ცხადი ცარიელი ცილი ცილი ცარიელი
        String selectQuery = "SELECT username, first_name, last_name, email, password, phone FROM test_users";

        List<User> users = new ArrayList<>();

        // Try-with-resources — ცარიელი ცილი ცარიელი ცილი
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {

            // ResultSet-ის ცარიელი row-ები → User POJO objects
            while (rs.next()) {
                User user = User.builder()
                        .id(System.currentTimeMillis() + users.size())  // unique ID
                        .username(rs.getString("username") + "_" + System.currentTimeMillis())  // unique username
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .phone(rs.getString("phone"))
                        .userStatus(1)
                        .build();
                users.add(user);
            }
        }

        // List<User> → Object[][] (DataProvider format)
        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i);
        }

        System.out.println("✅ DataProvider loaded " + users.size() + " users from H2 database");
        return data;
    }
}