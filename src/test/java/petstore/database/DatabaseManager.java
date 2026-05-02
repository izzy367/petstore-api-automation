package petstore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * H2 in-memory database manager.
 *
 * რა არის H2:
 * - მსუბუქი SQL database, რომელიც memory-ში მუშაობს
 * - არ ჭირდება ცალკე installation (server, port და ა.შ.)
 * - JVM-ის გაჩერებისას მონაცემები ქრება
 *
 * რატომ ვიყენებთ ფინალური პროექტისთვის:
 * - SQL skill-ის დემონსტრაცია (ლექტორის ბონუს მოთხოვნა)
 * - ნებისმიერ კომპიუტერზე მუშაობს, ცალკე setup-ის გარეშე
 * - შემფასებელი უბრალოდ mvn test-ს უშვებს — DB თვითონ ცოცხლდება
 *
 * Connection string-ის ახსნა:
 * - "jdbc:h2:mem:" — in-memory mode
 * - "petstore_test" — DB-ის სახელი
 * - "DB_CLOSE_DELAY=-1" — DB არ იხურება როცა ბოლო connection წყდება
 *   (ამის გარეშე ყოველ ცალკეულ query-ის შემდეგ DB კვდება)
 */
public class DatabaseManager {

    private static final String JDBC_URL = "jdbc:h2:mem:petstore_test;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";              // H2-ის default user
    private static final String PASSWORD = "";            // H2-ის default password (ცარიელი)

    /**
     * აბრუნებს ახალ Connection-ს DB-სთან.
     * Try-with-resources-ში გამოიყენე რომ ავტომატურად დაიხუროს.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    /**
     * ქმნის ცხრილს და ავსებს test data-თი.
     * ეს მეთოდი გამოიძახება ერთხელ — @BeforeSuite-დან.
     */
    public static void initializeDatabase() {
        // SQL command-ები რომელიც გავუშვათ DB-ზე
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS test_users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    first_name VARCHAR(50),
                    last_name VARCHAR(50),
                    email VARCHAR(100),
                    password VARCHAR(50),
                    phone VARCHAR(20)
                )
                """;

        String insertDataSQL = """
                INSERT INTO test_users (username, first_name, last_name, email, password, phone) VALUES
                ('izzy_junior', 'Isidore', 'Junior', 'izzy.junior@test.com', 'Test1234!', '555111111'),
                ('izzy_mid', 'Isidore', 'Mid', 'izzy.mid@test.com', 'Test1234!', '555222222'),
                ('izzy_senior', 'Isidore', 'Senior', 'izzy.senior@test.com', 'Test1234!', '555333333')
                """;

        // Try-with-resources — Connection და Statement ავტომატურად იხურება
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);
            stmt.execute(insertDataSQL);

            System.out.println("✅ Database initialized: test_users table created with 3 records");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}