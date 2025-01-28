package edu.lehigh.cse216.team24.backend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** 
 * The Upvote class manages upvote-related database operations including creation, 
 * reading, updating, and deleting ideas from the database.
 */
public class Upvotes {
    // Global variable to allow us to change name of the table in one place,
    // everywhere
    private static String upvoteTableName = "UpvoteTable";
    
    private Database upvoteDB;

    // Parameterized constructor allowing us to pass in a table name
    public Upvotes(String n) {
        upvoteDB = Buzz.db;
        upvoteTableName = n;
    }

    public Upvotes() {
        upvoteDB = Buzz.db;
    }

    /***
     * Refresh our database connection everytime upstream connection is stale (in App.java)
     */
    void refreshConnection(){
        upvoteDB = Buzz.db;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int upvote_id, int user_id, int idea_id) {

    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;

    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (upvote_id SERIAL PRIMARY KEY, " +
                    "user_id INT NOT NULL, idea_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES UserTable(user_id), "
                    +
                    "FOREIGN KEY(idea_id) REFERENCES IdeaTable(idea_id), CONSTRAINT one_like UNIQUE(user_id, idea_id));",
            upvoteTableName);

    /**
     * safely performs mCreateTable =
     * upvoteDB.mConnection.prepareStatement(SQL_CREATE_TABLE);
     * 
     * @return true if table is created, false otherwise.
     */
    private boolean init_mCreateTable() {
        try {
            mCreateTable = upvoteDB.mConnection.prepareStatement(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateTable");
            System.err.println("Using SQL: " + SQL_CREATE_TABLE);
            e.printStackTrace();
            upvoteDB.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createTable() {
        if (mCreateTable == null)
            init_mCreateTable();
        try {
            System.out.println("Database operation: createTable()");
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////// DROP TABLE //////////////////////////
    /** ps to delete table tbldata from the database */
    private PreparedStatement mDropTable;

    /** the SQL for mDropTable */
    private static final String SQL_DROP_TABLE = String.format("DROP TABLE IF EXISTS %s CASCADE", upvoteTableName);

    /**
     * safely performs mDropTable = upvoteDB.mConnection.prepareStatement("DROP TABLE
     * tblData");
     * 
     * @return true if table is dropped, false otherwise.
     */
    private boolean init_mDropTable() {
        try {
            mDropTable = upvoteDB.mConnection.prepareStatement(SQL_DROP_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropTable");
            System.err.println("Using SQL: " + SQL_DROP_TABLE);
            e.printStackTrace();
            upvoteDB.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        if (mDropTable == null)
            init_mDropTable();
        try {
            System.out.println("Database operation: dropTable()");
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////// INSERT //////////////////////////
    /**
     * ps to instert into tbldata a new row with next auto-gen id and the two given
     * values
     */
    private PreparedStatement mInsertUpvote;

    /** the SQL for mInsertUpvote */
    private static final String SQL_INSERT_ONE = String
            .format("INSERT INTO %s (upvote_id, user_id, idea_id)" +
                    " VALUES (default, ?, ?)", upvoteTableName);

    /**
     * safely performs mInsertUpvote = upvoteDB.mConnection.prepareStatement("INSERT INTO
     * tblData VALUES (default, ?, ?)");
     * 
     * @return true if item is inserted into DB, false otherwise.
     */
    private boolean init_mInsertOne() {
        try {
            mInsertUpvote = upvoteDB.mConnection.prepareStatement(SQL_INSERT_ONE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertUpvote");
            System.err.println("Using SQL: " + SQL_INSERT_ONE);
            e.printStackTrace();
            upvoteDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert an upvote into the database or if it already exists remove an upvote
     * 
     * @param user_id the user id to attach the upvote to
     * @param idea_id id of the idea to post comment under
     * @return The number of rows that were inserted
     */
    boolean insertUpvote(int user_id, int idea_id) {
        // First try to use existing prepared statement
        if (hasUpvote(user_id, idea_id)) {
            this.deleteUpvote(user_id, idea_id);
            return false;
        } else {
            try {
                return executeInsertUpvoteQuery(user_id, idea_id);
            } catch (SQLException e) {
                // If the prepared statement is invalid/closed, try to reinitialize it
                try {
                    // Clean up old statement if it exists
                    if (mInsertUpvote != null) {
                        mInsertUpvote.close();
                        mInsertUpvote = null;
                    }
                    // Reinitialize
                    if (init_mInsertOne()) {
                        return executeInsertUpvoteQuery(user_id, idea_id);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return false; // Return false to indicate no rows were inserted
            }
        }
    }

    /**
     * @param user_id
     * @param idea_id
     * @return boolean
     * @throws SQLException
     */
    // Separate the query execution logic
    private boolean executeInsertUpvoteQuery(int user_id, int idea_id) throws SQLException {
        if (mInsertUpvote == null) {
            init_mInsertOne();
        }

        mInsertUpvote.setInt(1, user_id);
        mInsertUpvote.setInt(2, idea_id);
        System.out.println("Database operation: insertUpvote(int, int)");
        return 1 == mInsertUpvote.executeUpdate(); // If 1 row inserted return true
    }

    ////////////////////////// DELETE UPVOTE //////////////////////////
    /** ps for deleting a row from tblData */
    private PreparedStatement mDeleteOne;

    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_ONE = String.format("DELETE FROM %s" +
            " WHERE user_id = ? AND idea_id = ?", upvoteTableName);

    /**
     * safely performs mDeleteOne = upvoteDB.mConnection.prepareStatement(SQL_DELETE_ONE);
     * 
     * @return true if row is deleted from DB, false otherwise.
     */
    private boolean init_mDeleteOne() {
        try {
            // Close existing statement if it exists
            if (mDeleteOne != null) {
                mDeleteOne.close();
            }
            // Create new prepared statement
            mDeleteOne = upvoteDB.mConnection.prepareStatement(SQL_DELETE_ONE);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOne");
            System.err.println("Using SQL: " + SQL_DELETE_ONE);
            e.printStackTrace();
            upvoteDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
    }

    /**
     * Delete an upvote
     * 
     * @param user_id the user_id of the upvote to delete
     * @param idea_id the idea_id of the upvote to delete
     * @return All rows, as an ArrayList
     */
    boolean deleteUpvote(int user_id, int idea_id) {
        // First try to use existing prepared statement
        try {
            return executeDelete(user_id, idea_id);
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (mDeleteOne != null) {
                    mDeleteOne.close();
                    mDeleteOne = null;
                }
                // Reinitialize
                if (init_mDeleteOne()) {
                    return executeDelete(user_id, idea_id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Function to execute delete query from user table
     * 
     * @param user_id
     * @param idea_id
     * @return boolean
     * @throws SQLException
     */
    // Separate the query execution logic
    private boolean executeDelete(int user_id, int idea_id) throws SQLException {
        if (mDeleteOne == null) {
            init_mDeleteOne();
        }
        mDeleteOne.setInt(1, user_id);
        mDeleteOne.setInt(2, idea_id);
        System.out.println("Database operation: removeUpvote(int, int)");
        return mDeleteOne.execute();
    }

    ////////////////////////// HAS UPVOTE ///////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mHasUpvote;

    /** the SQL for mHasUpvote */
    private static final String SQL_SELECT_UPVOTE_CHECK = String
            .format("SELECT COUNT(user_id) AS upvote" +
                    " FROM %s WHERE user_id = ? AND idea_id = ?;", upvoteTableName);

    /**
     * safely performs mSelectAll = upvoteDB.mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectUpvoteCheck() {
        // return true on success, false otherwise
        try {
            // Close existing statement if it exists
            if (mHasUpvote != null) {
                mHasUpvote.close();
            }
            // Create new prepared statement
            mHasUpvote = upvoteDB.mConnection.prepareStatement(SQL_SELECT_UPVOTE_CHECK);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mHasUpvote");
            System.err.println("Using SQL: " + SQL_SELECT_UPVOTE_CHECK);
            e.printStackTrace();
            upvoteDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
    }

    /**
     * Query the database for a count of all unique likes by idea_id and user_id to
     * check
     * 
     * @param user_id the user id to count upvotes for
     * @param idea_id the id of the idea to count user upvotes for
     * @return boolean of whether user has upvote in table for idea already
     */
    boolean hasUpvote(int user_id, int idea_id) {
        // First try to use existing prepared statement
        try {
            return executeHasUpvoteQuery(user_id, idea_id);
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (mHasUpvote != null) {
                    mHasUpvote.close();
                    mHasUpvote = null;
                }
                // Reinitialize
                if (init_mSelectUpvoteCheck()) {
                    return executeHasUpvoteQuery(user_id, idea_id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return true; // your default error case
        }
    }

    /**
     * Function to execute has upvote query to search datgabase if user id and idea
     * id has an upvote
     * 
     * @param user_id
     * @param idea_id
     * @return boolean
     * @throws SQLException
     */
    // Separate the query execution logic
    private boolean executeHasUpvoteQuery(int user_id, int idea_id) throws SQLException {
        if (mHasUpvote == null) {
            init_mSelectUpvoteCheck();
        }

        mHasUpvote.setInt(1, user_id);
        mHasUpvote.setInt(2, idea_id);
        int count = 0;

        try (ResultSet rs = mHasUpvote.executeQuery()) {
            if (rs.next()) { // if instead of while since you're only expecting one row
                count = rs.getInt("upvote");
            }
        }
        return count != 0;
    }

    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mSelectAllUpvotes;

    /** the SQL for mSelectAllUpvotes */
    private static final String SQL_SELECT_ALL = String.format("SELECT *" +
            " FROM %s;", upvoteTableName);

    /**
     * safely performs mSelectAll = upvoteDB.mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectAll() {
        // return true on success, false otherwise
        try {
            mSelectAllUpvotes = upvoteDB.mConnection.prepareStatement(SQL_SELECT_ALL);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllUpvotes");
            System.err.println("Using SQL: " + SQL_SELECT_ALL);
            e.printStackTrace();
            upvoteDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a list of all ID, idea, and number of upvtoes
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        // First try to use existing prepared statement
        try {
            return executeSelectAllQuery();
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (mSelectAllUpvotes != null) {
                    mSelectAllUpvotes.close();
                    mSelectAllUpvotes = null;
                }
                // Reinitialize
                if (init_mSelectAll()) {
                    return executeSelectAllQuery();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return new ArrayList<>(); // Return empty list instead of null
        }
    }

    
    /** 
     * @return ArrayList<RowData>
     * @throws SQLException
     */
    // Separate the query execution logic
    private ArrayList<RowData> executeSelectAllQuery() throws SQLException {
        if (mSelectAllUpvotes == null) {
            init_mSelectAll();
        }
        ArrayList<RowData> res = new ArrayList<>();
        try (ResultSet rs = mSelectAllUpvotes.executeQuery()) {
            while (rs.next()) {
                res.add(new RowData(
                        rs.getInt("upvote_id"),
                        rs.getInt("user_id"),
                        rs.getInt("idea_id")));
            }
        }
        return res;
    }

    
    /** 
     * @return boolean
     */
    // Method to call disconnect on database
    boolean disconnect() {
        return upvoteDB.disconnect();
    }

}
