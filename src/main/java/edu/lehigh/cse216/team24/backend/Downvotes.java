package edu.lehigh.cse216.team24.backend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Downvotes {

    private static String downvoteTableName = "DownvoteTable";
 
    private Database downvoteDB;

    // Parameterized constructor allowing us to pass in a table name
    public Downvotes(String n) {
        downvoteDB = Buzz.db;
        downvoteTableName = n;
    }

    public Downvotes() {
        downvoteDB = Buzz.db;
    }

    /***
     * Refresh our database connection everytime upstream connection is stale (in App.java)
     */
    void refreshConnection(){
        downvoteDB = Buzz.db;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int downvote_id, int user_id, int idea_id) {

    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;

    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (downvote_id SERIAL PRIMARY KEY, " +
                    "user_id INT NOT NULL, idea_id INT NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES UserTable(user_id), " +
                    "FOREIGN KEY(idea_id) REFERENCES IdeaTable(idea_id), " +
                    "CONSTRAINT one_dislike UNIQUE(user_id, idea_id));",
            downvoteTableName);

    /**
     * safely performs mCreateTable =
     * downvoteDB.mConnection.prepareStatement(SQL_CREATE_TABLE);
     * 
     * @return true if table is created, false otherwise.
     */
    private boolean init_mCreateTable() {
        try {
            mCreateTable = downvoteDB.mConnection.prepareStatement(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateTable");
            System.err.println("Using SQL: " + SQL_CREATE_TABLE);
            e.printStackTrace();
            downvoteDB.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Create table. If it already exists, this will print an error
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
    private static final String SQL_DROP_TABLE = String.format("DROP TABLE IF EXISTS %s CASCADE", downvoteTableName);

    /**
     * safely performs mDropTable = downvoteDB.mConnection.prepareStatement("DROP TABLE
     * tblData");
     * 
     * @return true if table is dropped, false otherwise.
     */
    private boolean init_mDropTable() {
        try {
            mDropTable = downvoteDB.mConnection.prepareStatement(SQL_DROP_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropTable");
            System.err.println("Using SQL: " + SQL_DROP_TABLE);
            e.printStackTrace();
            downvoteDB.disconnect();
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
    private PreparedStatement mInsertDownvote;

    /** the SQL for mInsertUpvote */
    private static final String SQL_INSERT_ONE = String
            .format("INSERT INTO %s (downvote_id, user_id, idea_id)" +
                    " VALUES (default, ?, ?)", downvoteTableName);

    /**
     * safely performs mInsertUpvote = downvoteDB.mConnection.prepareStatement("INSERT INTO
     * tblData VALUES (default, ?, ?)");
     * 
     * @return true if item is inserted into DB, false otherwise.
     */
    private boolean init_mInsertOne() {
        try {
            // Close existing statement if it exists
            if (mInsertDownvote != null) {
                mInsertDownvote.close();
            }
            // Create new prepared statement
            mInsertDownvote = downvoteDB.mConnection.prepareStatement(SQL_INSERT_ONE);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertDownvote");
            System.err.println("Using SQL: " + SQL_INSERT_ONE);
            e.printStackTrace();
            downvoteDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
    }

    /**
     * Insert a downvote into the database or if it already exists remove an upvote
     * 
     * @param user_id the user id to attach the upvote to
     * @param idea_id id of the idea to post comment under
     * @return The number of rows that were inserted
     */
    boolean insertDownvote(int user_id, int idea_id) {
        if (hasDownvote(user_id, idea_id)) {
            this.deleteDownvote(user_id, idea_id);
            return false;
        } else {
            try {
                return 1 == executeInsertDownvoteQuery(user_id, idea_id);
            } catch (SQLException e) {
                // If the prepared statement is invalid/closed, try to reinitialize it
                try {
                    // Clean up old statement if it exists
                    if (mInsertDownvote != null) {
                        mInsertDownvote.close();
                        mInsertDownvote = null;
                    }
                    // Reinitialize
                    if (init_mInsertOne()) {
                        return 1 == executeInsertDownvoteQuery(user_id, idea_id);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return false; // Return false to indicate none added
            }
        }
    }

    /**
     * @param user_id
     * @param idea_id
     * @return int
     * @throws SQLException
     */
    // Separate the query execution logic
    private int executeInsertDownvoteQuery(int user_id, int idea_id) throws SQLException {
        if (mInsertDownvote == null) {
            init_mInsertOne();
        }

        mInsertDownvote.setInt(1, user_id);
        mInsertDownvote.setInt(2, idea_id);
        System.out.println("Database operation: insertDownvote(int, int)");
        return mInsertDownvote.executeUpdate(); // Returns the number of rows inserted
    }

    ////////////////////////// DELETE UPVOTE //////////////////////////
    /** ps for deleting a row from tblData */
    private PreparedStatement mDeleteOne;

    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_ONE = String.format("DELETE FROM %s" +
            " WHERE user_id = ? AND idea_id = ?", downvoteTableName);

    /**
     * safely performs mDeleteOne = downvoteDB.mConnection.prepareStatement(SQL_DELETE_ONE);
     * 
     * @return true if row is deleted from DB, false otherwise.
     */
    private boolean init_mDeleteOne() {
        try {
            mDeleteOne = downvoteDB.mConnection.prepareStatement(SQL_DELETE_ONE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOne");
            System.err.println("Using SQL: " + SQL_DELETE_ONE);
            e.printStackTrace();
            downvoteDB.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Delete an downvote
     * 
     * @param user_id the user_id of the downvote to delete
     * @param idea_id the idea_id of the downvote to delete
     * @return All rows, as an ArrayList
     */
    int deleteDownvote(int user_id, int idea_id) {
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
            return 0; // return 0 to indicate no rows were updated
        }
    }

    
    /** Deletes a downvote from the downvote table
     * @param user_id
     * @param idea_id
     * @return int
     * @throws SQLException
     */
    // Separate the query execution logic
    private int executeDelete(int user_id, int idea_id) throws SQLException {
        if (mDeleteOne == null) {
            init_mDeleteOne();
        }
        mDeleteOne.setInt(1, user_id);
        mDeleteOne.setInt(2, idea_id);
        System.out.println("Database operation: removeDownvote(int, int)");
        return mDeleteOne.executeUpdate();
    }

    ////////////////////////// SELECT COUNT OF DOWNVOTES BY IDEA //////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mCountDislikesByIdea;

    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_COUNT = String.format("SELECT COUNT(DISTINCT user_id) AS downvotes" +
            " FROM %s WHERE idea_id = ?;", downvoteTableName);

    /**
     * safely performs mSelectAll = downvoteDB.mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectCount() {
        // return true on success, false otherwise
        try {
            mCountDislikesByIdea = downvoteDB.mConnection.prepareStatement(SQL_SELECT_COUNT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAll");
            System.err.println("Using SQL: " + SQL_SELECT_COUNT);
            e.printStackTrace();
            downvoteDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a count of all unique likes by idea_id
     * 
     * @param idea_id the id of the idea to count user downvotes for
     * @return All rows, as an ArrayList
     */
    int countDownvotes(int idea_id) {
        // First try to use existing prepared statement
        try {
            return executeCountDownvotesQuery(idea_id);
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (mCountDislikesByIdea != null) {
                    mCountDislikesByIdea.close();
                    mCountDislikesByIdea = null;
                }
                // Reinitialize
                if (init_mSelectCount()) {
                    return executeCountDownvotesQuery(idea_id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        }
    }

    
    /** 
     * @param idea_id
     * @return int
     * @throws SQLException
     */
    // Separate the query execution logic
    private int executeCountDownvotesQuery(int idea_id) throws SQLException {
        if (mCountDislikesByIdea == null) {
            init_mSelectCount();
        }
        mCountDislikesByIdea.setInt(1, idea_id);
        try (ResultSet rs = mCountDislikesByIdea.executeQuery()) {
            // Use if instead of while since we're only expecting one row
            if (rs.next()) {
                return rs.getInt("downvotes");
            }
            return 0; // Return 0 if no results found
        }
    }

    ////////////////////////// HAS DOWNVOTE ///////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mHasDownvote;

    /** the SQL for mHasDownvote */
    private static final String SQL_SELECT_DOWNVOTE_CHECK = String
            .format("SELECT COUNT(user_id) AS downvote" +
                    " FROM %s WHERE user_id = ? AND idea_id = ?;", downvoteTableName);

    /**
     * safely performs mSelectAll = downvoteDB.mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectDownvoteCheck() {
        // return true on success, false otherwise
        try {
            mHasDownvote = downvoteDB.mConnection.prepareStatement(SQL_SELECT_DOWNVOTE_CHECK);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mHasDownvote");
            System.err.println("Using SQL: " + SQL_SELECT_DOWNVOTE_CHECK);
            e.printStackTrace();
            downvoteDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    // Separate the query execution logic
    private boolean executeHasDownvoteQuery(int user_id, int idea_id) throws SQLException {
        if (mHasDownvote == null) {
            init_mSelectDownvoteCheck();
        }

        mHasDownvote.setInt(1, user_id);
        mHasDownvote.setInt(2, idea_id);
        int count = 0;

        try (ResultSet rs = mHasDownvote.executeQuery()) {
            if (rs.next()) { // if instead of while since you're only expecting one row
                count = rs.getInt("downvote");
            }
        }
        return count != 0;
    }

    /**
     * Query the database for a count of all unique likes by idea_id and user_id to
     * check
     * 
     * @param user_id the user id to count downvotes for
     * @param idea_id the id of the idea to count user dwonvotes for
     * @return boolean of whether user has upvote in table for idea already
     */
    boolean hasDownvote(int user_id, int idea_id) {
        // First try to use existing prepared statement
        try {
            return executeHasDownvoteQuery(user_id, idea_id);
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (mHasDownvote != null) {
                    mHasDownvote.close();
                    mHasDownvote = null;
                }
                // Reinitialize
                if (init_mSelectDownvoteCheck()) {
                    return executeHasDownvoteQuery(user_id, idea_id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return true; // your default error case
        }
    }

    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mSelectAllDownvotes;

    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_ALL = String.format("SELECT *" +
            " FROM %s;", downvoteTableName);

    /**
     * safely performs mSelectAll = downvoteDB.mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectAll() {
        // return true on success, false otherwise
        try {
            mSelectAllDownvotes = downvoteDB.mConnection.prepareStatement(SQL_SELECT_ALL);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllDownvotes");
            System.err.println("Using SQL: " + SQL_SELECT_ALL);
            e.printStackTrace();
            downvoteDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a list of all ID, idea, and number of downvotes
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
                if (mSelectAllDownvotes != null) {
                    mSelectAllDownvotes.close();
                    mSelectAllDownvotes = null;
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

    // Separate the query execution logic
    private ArrayList<RowData> executeSelectAllQuery() throws SQLException {
        if (mSelectAllDownvotes == null) {
            init_mSelectAll();
        }
        ArrayList<RowData> res = new ArrayList<>();
        try (ResultSet rs = mSelectAllDownvotes.executeQuery()) {
            while (rs.next()) {
                res.add(new RowData(
                        rs.getInt("downvote_id"),
                        rs.getInt("user_id"),
                        rs.getInt("idea_id")));
            }
        }
        return res;
    }

    // Method to call disconnect on database
    boolean disconnect() {
        return downvoteDB.disconnect();
    }

}
