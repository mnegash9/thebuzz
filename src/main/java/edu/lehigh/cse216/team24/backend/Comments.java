package edu.lehigh.cse216.team24.backend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Comments {
    // Global variable to allow us to change name of the table in one place,
    // everywhere
    private static String commentTableName = "CommentTable";

    private Database commentDB;

    // Parameterized constructor allowing us to pass in a userTableName
    public Comments(String n) {
        commentDB = Buzz.db;
        commentTableName = n;
    }

    public Comments() {
        commentDB = Buzz.db;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int comment_id, String body, int idea_id, int user_id) {
    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;

    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (comment_id SERIAL PRIMARY KEY, " +
                    "body VARCHAR(512) NOT NULL, comment_link VARCHAR(512), idea_id INT NOT NULL, user_id INT NOT NULL, "
                    +
                    "FOREIGN KEY(idea_id) REFERENCES IdeaTable(idea_id), FOREIGN KEY (user_id) REFERENCES UserTable(user_id));",
            commentTableName);

    /**
     * safely performs mCreateTable =
     * commentDB.mConnection.prepareStatement(SQL_CREATE_TABLE);
     * 
     * @return true if table is created, false otherwise.
     */
    private boolean init_mCreateTable() {
        try {
            mCreateTable = commentDB.mConnection.prepareStatement(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateTable");
            System.err.println("Using SQL: " + SQL_CREATE_TABLE);
            e.printStackTrace();
            commentDB.disconnect();
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
    private static final String SQL_DROP_TABLE = String.format("DROP TABLE IF EXISTS %s CASCADE", commentTableName);

    /**
     * safely performs mDropTable = commentDB.mConnection.prepareStatement("DROP TABLE
     * tblData");
     * 
     * @return true if table is dropped, false otherwise.
     */
    private boolean init_mDropTable() {
        try {
            mDropTable = commentDB.mConnection.prepareStatement(SQL_DROP_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropTable");
            System.err.println("Using SQL: " + SQL_DROP_TABLE);
            e.printStackTrace();
            commentDB.disconnect();
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
    private PreparedStatement mInsertOne;

    /** the SQL for mInsertOne */
    private static final String SQL_INSERT_ONE = String
            .format("INSERT INTO %s (comment_id, body, idea_id, user_id)" +
                    " VALUES (default, ?, ?, ?);", commentTableName);;

    /**
     * safely performs mInsertOne = commentDB.mConnection.prepareStatement("INSERT INTO
     * tblData VALUES (default, ?, ?)");
     * 
     * @return true if item is inserted into DB, false otherwise.
     */
    private boolean init_mInsertOne() {
        try {
            // Close existing statement if it exists
            if (mInsertOne != null) {
                mInsertOne.close();
            }
            // Create new prepared statement
            mInsertOne = commentDB.mConnection.prepareStatement(SQL_INSERT_ONE, Statement.RETURN_GENERATED_KEYS);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            System.err.println("Using SQL: " + SQL_INSERT_ONE);
            e.printStackTrace();
            commentDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
    }

    /**
     * Insert a row into the database
     * 
     * @param body    the body of the comment
     * @param idea_id id of the idea to post comment under
     * @param user_id the id of the user posting comment
     * @return The id of the row inserted, 0 if unsuccessful
     */
    int insertComment(String body, int idea_id, int user_id) {
        if (mInsertOne == null) // not yet initialized, do lazy init
            init_mInsertOne(); // lazy init
        int last_inserted_id = 0;
        try {
            System.out.println("Database operation: insertComment(String, Int, Int)");
            mInsertOne.setString(1, body);
            mInsertOne.setInt(2, idea_id);
            mInsertOne.setInt(3, user_id);
            mInsertOne.executeUpdate();

            // get the row for last inserted idea
            ResultSet rs = mInsertOne.getGeneratedKeys();
            if (rs.next()) {
                last_inserted_id = rs.getInt(1);
            }
            return last_inserted_id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return last_inserted_id;
    }

    ////////////////////////// UPDATE COMMENT //////////////////////////
    /** ps to replace the idea in tabledata for specified row with given value */
    private PreparedStatement mUpdateComment;
    /** the SQL for mUpdateComment */
    private static final String SQL_UPDATE_NOTE = String.format("UPDATE %s" +
            " SET body = ?" +
            " WHERE comment_id = ?", commentTableName);

    /**
     * safely performs an update of the user's note in database using prepared
     * statement above
     * 
     * @return true if row is updated in DB, false otherwise.
     */
    private boolean init_mUpdateNote() {
        // return true on success, false otherwise
        try {
            mUpdateComment = commentDB.mConnection.prepareStatement(SQL_UPDATE_NOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateComment");
            System.err.println("Using SQL: " + SQL_UPDATE_NOTE);
            e.printStackTrace();
            commentDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the commment for comment id in the database
     * 
     * @param id   The comment id of the comment to update
     * @param body The new body of the comment
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateComment(int id, String body) {
        if (mUpdateComment == null)
            init_mUpdateNote();
        int res = -1;
        try {
            System.out.println("Database operation: updateComment(int id, String body)");
            mUpdateComment.setString(1, body);
            mUpdateComment.setInt(2, id);
            res = mUpdateComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// ADD LINK //////////////////////////
    /** ps to replace the status of an idea for specified row with given value */
    private PreparedStatement mAddLink;
    /** the SQL for mAddLink */
    private static final String SQL_ADD_LINK = String.format("UPDATE %s" +
            " SET comment_link = ?" +
            " WHERE comment_id = ?", commentTableName);

    /**
     * safely performs an update of the user's status in database using prepared
     * statement above
     * 
     * @return true if row is updated in DB, false otherwise.
     */
    private boolean init_mAddLink() {
        // return true on success, false otherwise
        try {
            mAddLink = commentDB.mConnection.prepareStatement(SQL_ADD_LINK);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mAddLink");
            System.err.println("Using SQL: " + SQL_ADD_LINK);
            e.printStackTrace();
            commentDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the link for a comment in the database
     *
     * @param comment_id The commemnt id of the comment to update
     * @param link       The link to add to the database
     * @return The number of rows that were updated. 0 indicates no rows updated or
     *         an error.
     */
    int addLink(int comment_id, String link) {
        try {
            return executeUpdateQuery(comment_id, link);
        } catch (SQLException e) {
            try {
                // Clean up old statement if it exists
                if (mAddLink != null) {
                    mAddLink.close();
                    mAddLink = null;
                }
                // Reinitialize and retry
                if (init_mAddLink()) {
                    return executeUpdateQuery(comment_id, link);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0; // Return 0 instead of -1 to be consistent with JDBC standard
        }
    }

    /**
     * Execute the update status query with the given parameters
     *
     * @param idea_id     The idea id of the idea to update
     * @param idea_active The new status of the idea
     * @return The number of rows that were updated
     * @throws SQLException if there's an error executing the query
     */
    private int executeUpdateQuery(int comment_id, String link) throws SQLException {
        if (mAddLink == null) {
            if (!init_mAddLink()) {
                return 0;
            }
        }

        mAddLink.setString(1, link);
        mAddLink.setInt(2, comment_id);
        return mAddLink.executeUpdate();
    }

    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mSelectAll;

    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_ALL_TBLDATA = String.format("SELECT *" +
            " FROM %s;", commentTableName);

    /**
     * safely performs mSelectAll = commentDB.mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectAll() {
        // return true on success, false otherwise
        try {
            mSelectAll = commentDB.mConnection.prepareStatement(SQL_SELECT_ALL_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAll");
            System.err.println("Using SQL: " + SQL_SELECT_ALL_TBLDATA);
            e.printStackTrace();
            commentDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a list of all ID, idea, and number of likes
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        if (mSelectAll == null) // not yet initialized, do lazy init
            init_mSelectAll(); // lazy init
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            System.out.println("Database operation: selectAll()");
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                int comment_id = rs.getInt("comment_id");
                String body = rs.getString("body");
                int idea_id = rs.getInt("idea_id");
                int user_id = rs.getInt("user_id");
                RowData data = new RowData(comment_id, body, idea_id, user_id);
                res.add(data);
            }
            rs.close(); // remember to close the result set
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ////////////////////////// SELECT ONE //////////////////////////
    private PreparedStatement mSelectOne;

    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_ONE = String.format("SELECT *" +
            " FROM %s" +
            " WHERE idea_id=? ;", commentTableName);

    /**
     * safely performs mSelectOne = commentDB.mConnection.prepareStatement("SELECT * from
     * tblData WHERE id=?");
     * 
     * @return true if one row is selected, false otherwise.
     */
    private boolean init_mSelectOne() {
        try {
            mSelectOne = commentDB.mConnection.prepareStatement(SQL_SELECT_ONE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            System.err.println("Using SQL: " + SQL_SELECT_ONE);
            e.printStackTrace();
            commentDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int row_id) {
        try {
            return executeSelectOneQuery(row_id);
        } catch (SQLException e) {
            try {
                if (mSelectOne != null) {
                    mSelectOne.close();
                    mSelectOne = null;
                }
                if (init_mSelectOne()) {
                    return executeSelectOneQuery(row_id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null; // or consider returning an Optional<RowData>
        }
    }

    
    /** 
     * @param row_id
     * @return RowData
     * @throws SQLException
     */
    private RowData executeSelectOneQuery(int row_id) throws SQLException {
        if (mSelectOne == null) {
            init_mSelectOne();
        }

        mSelectOne.setInt(1, row_id);

        try (ResultSet rs = mSelectOne.executeQuery()) {
            if (rs.next()) {
                return new RowData(
                        rs.getInt("comment_id"),
                        rs.getString("body"),
                        rs.getInt("idea_id"),
                        rs.getInt("user_id"));
            }
            return null; // or consider returning Optional.empty()
        }
    }

    ////////////////////////// SELECT COMMENT //////////////////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mSelectComments;

    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_COMMENTS = String.format("SELECT *" +
            " FROM %s WHERE idea_id=?;", commentTableName);

    /**
     * safely performs mSelectAll = commentDB.mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectComments() {
        // return true on success, false otherwise
        try {
            mSelectComments = commentDB.mConnection.prepareStatement(SQL_SELECT_COMMENTS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAll");
            System.err.println("Using SQL: " + SQL_SELECT_COMMENTS);
            e.printStackTrace();
            commentDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a list of all ID, idea, and number of likes
     * 
     * @param id the idea id of the database to search up
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectComments(int id) {
        if (mSelectAll == null) // not yet initialized, do lazy init
            init_mSelectComments(); // lazy init

        ArrayList<RowData> res = new ArrayList<RowData>();

        try {
            mSelectComments.setInt(1, id); // int id to set values
            System.out.println("Database operation: selectComments()");
            ResultSet rs = mSelectComments.executeQuery();
            while (rs.next()) {
                int comment_id = rs.getInt("comment_id");
                String body = rs.getString("body");
                int idea_id = rs.getInt("idea_id");
                int user_id = rs.getInt("user_id");
                RowData data = new RowData(comment_id, body, idea_id, user_id);
                res.add(data);
            }
            rs.close(); // remember to close the result set
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Disconnect using database connection
     * 
     * @return boolean true if connection was closed cleanly, false if not
     */
    // Method to call disconnect on database
    boolean disconnect() {
        return commentDB.disconnect();
    }

}
