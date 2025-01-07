package DALManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;

    // Database connection details
    private final String url = "jdbc:mysql://localhost:3306/employee_management?connectTimeout=5000"; // Added connection timeout
    private final String username = "root";
    private final String password = "BasitIqbal@050";

    // Private constructor to enforce Singleton pattern
    private DatabaseManager() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("The connection is successful with the username: " + username);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found! Ensure the MySQL connector JAR is in the classpath.");
            e.printStackTrace();
            throw new RuntimeException("JDBC Driver not found!", e);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database! Please check the URL, username, or password.");
            e.printStackTrace();
            throw new RuntimeException("Database connection error!", e);
        }
    }

    // Public method to get the Singleton instance
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    // Method to get the database connection
    public Connection getConnection() {
        return connection;
    }

    // Method to close the database connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                instance = null; // Reset the Singleton instance
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Failed to close the database connection.");
                e.printStackTrace();
            }
        }
    }

    // Main method for testing the connection
    public static void main(String[] args) {
        try {
            // Test the DatabaseManager
            DatabaseManager dbManager = DatabaseManager.getInstance();
            System.out.println("Database connection established.");
            
            // Close the connection
            dbManager.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
