package edu.lehigh.cse216.team24.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Database has all our logic for connecting to and interacting with a database.
 * 
 */
public class Database {

    /** Connection to db. An open connection if non-null, null otherwise */
    public Connection mConnection;

    /**
     * The Database constructor is private: we only create Database objects
     * through one or more static getDatabase() methods.
     */
    private Database() {
    }

    /**
     * Uses the presence of environment variables to invoke the correct
     * overloaded `getDatabase` method
     * 
     * @return a valid Database object with active connection on success, null
     *         otherwise
     */
    static Database getDatabase() {
        // get the Postgres configuration from environment variables;
        // you could name them almost anything
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        String dbname = env.get("POSTGRES_DBNAME");
        String dbUri = env.get("DATABASE_URI");

        System.out.printf("Using the following environment variables:%n%s%n", "-".repeat(45));
        System.out.printf("POSTGRES_IP=%s%n", ip);
        System.out.printf("POSTGRES_PORT=%s%n", port);
        System.out.printf("POSTGRES_USER=%s%n", user);
        System.out.printf("POSTGRES_PASS=%s%n", "omitted");
        System.out.printf("POSTGRES_DBNAME=%s%n", dbname);
        System.out.printf("DATABASE_URI=%s%n", dbUri);

        if (dbUri != null && dbUri.length() > 0) {
            return Database.getDatabase(dbUri);
        } else if (ip != null && port != null && dbname != null && user != null && pass != null) {
            return Database.getDatabase(ip, port, dbname, user, pass);
        }
        // else insufficient information to connect
        System.err.println("Insufficient information to connect to database.");
        return null;
    }

    /**
     * Uses dbUri to create a connection to a database, and stores it in the
     * returned Database object
     * 
     * @param dbUri the connection string for the database
     * @return null upon connection failure, otherwise a valid Database with open
     *         connection
     */
    static Database getDatabase(String dbUri) {
        // Connect to the database or fail
        if (dbUri != null && dbUri.length() > 0) {
            // DATABASE_URI appears to be set
            Database returned_val = new Database();
            System.out.println("Attempting to use provided DATABASE_URI env var.");
            try {
                returned_val.mConnection = DriverManager.getConnection(dbUri);
                if (returned_val.mConnection == null) {
                    System.err.println("Error: DriverManager.getConnection() returned a null object");
                    return null; // we return null to indicate failure; callers should know this
                } else {
                    System.out.println(" ... successfully connected");
                }
            } catch (SQLException e) {
                System.err.println("Error: DriverManager.getConnection() threw a SQLException");
                e.printStackTrace();
                return null; // we return null to indicate failure; callers should know this
            }
            return returned_val; // the valid object with active connection
        }
        return null;
    }

    /**
     * Uses params to manually construct string for connecting to a database,
     * and stores it in the returned Database object
     * 
     * @param ip     not strictly an ip, can also be a host name
     * @param port   default tends to be 5432 or something like it
     * @param dbname generally should be non null
     * @param user   generally required
     * @param pass   generally required
     * @return null upon connection failure, otherwise a valid Database with open
     *         connection
     */
    static Database getDatabase(String ip, String port, String dbname, String user, String pass) {
        if (ip != null && port != null &&
                dbname != null &&
                user != null && pass != null) {
            // POSTGRES_* variables appear to be set
            Database returned_val = new Database();
            System.out.println("Attempting to use provided POSTGRES_{IP, PORT, USER, PASS, DBNAME} env var.");
            System.out.println("Connecting to " + ip + ":" + port);
            try {
                // Open a connection, fail if we cannot get one
                returned_val.mConnection = DriverManager
                        .getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + dbname, user, pass);
                if (returned_val.mConnection == null) {
                    System.out.println("\n\tError: DriverManager.getConnection() returned a null object");
                    return null; // we return null to indicate failure; callers should know this
                } else {
                    System.out.println(" ... successfully connected");
                }
            } catch (SQLException e) {
                System.out.println("\n\tError: DriverManager.getConnection() threw a SQLException");
                e.printStackTrace();
                return null; // we return null to indicate failure; callers should know this
            }
            return returned_val; // the valid object with active connection
        }
        return null;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
     * 
     * @return true if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection != null) {
            // for this simple example, we disconnect from the db when done
            System.out.print("Disconnecting from database.");
            try {
                mConnection.close();
            } catch (SQLException e) {
                System.err.println("\n\tError: close() threw a SQLException");
                e.printStackTrace();
                mConnection = null; // set it to null rather than leave broken
                return false;
            }
            System.out.println(" ...  connection successfully closed");
            mConnection = null; // connection is gone, so null this out
            return true;
        }
        // else connection was already null
        System.err.print("Unable to close connection: Connection was null");
        return false;
    }

}
