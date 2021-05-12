package order.management.Connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will make the connection to the database
 * we will have a single instance to this class
 */
public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3307/hwpt_3";
    private static final String USER = "root";
    private static final String PASS = "";

    private static final ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Th constroctor of connection factory class
     */
    private ConnectionFactory() {
        try{
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to create the connection to the database
     * @return the connection
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred during connection to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * method to receive the connection to the database
     * @return the connection
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Method to close the connection
     * @param connection
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred when closing the database");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to close the statement
     * @param statement
     */
    public static void close (Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred when closing the statement");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to close the resultSet
     * @param resultSet
     */
    public static void close (ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred when closing the result set");
                e.printStackTrace();
            }
        }
    }
}
