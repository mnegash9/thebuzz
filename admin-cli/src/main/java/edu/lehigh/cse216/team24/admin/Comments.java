package edu.lehigh.cse216.team24.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Comments {
    // Global variable to allow us to change name of the table in one place,
    // everywhere
    private static String commentTableName = "CommentTable";

    /**
     * Connection to db. An open connection if non-null, null otherwise. And
     * database object which encloses the new connection passed from Database class
     */
    private Connection mConnection;
    private Database commentDB;

    // Parameterized constructor allowing us to pass in a commentTableName
    public Comments(String n) {
        commentDB = Database.getDatabase();
        mConnection = commentDB.mConnection;
        commentTableName = n;
    }

    public Comments() {
        commentDB = Database.getDatabase();
        mConnection = commentDB.mConnection;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int comment_id, String body, String comment_link, int idea_id, int user_id) {
    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;

    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (comment_id SERIAL PRIMARY KEY, " +
                    "body VARCHAR(512) NOT NULL, comment_link VARCHAR(512), idea_id INT NOT NULL, user_id INT NOT NULL, " +
                    "FOREIGN KEY(idea_id) REFERENCES IdeaTable(idea_id), FOREIGN KEY (user_id) REFERENCES UserTable(user_id));",
            commentTableName);

    /**
     * safely performs mCreateTable =
     * mConnection.prepareStatement(SQL_CREATE_TABLE);
     * 
     * @return true if table is created, false otherwise.
     */
    private boolean init_mCreateTable() {
        try {
            mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE);
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
     * safely performs mDropTable = mConnection.prepareStatement("DROP TABLE
     * tblData");
     * 
     * @return true if table is dropped, false otherwise.
     */
    private boolean init_mDropTable() {
        try {
            mDropTable = mConnection.prepareStatement(SQL_DROP_TABLE);
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
     * ps to instert into commentmediatable a new row with next auto-gen id and the four given
     * values
     */
    private PreparedStatement mInsertOne;

    /** the SQL for mInsertOne */
    private static final String SQL_INSERT_ONE = String
            .format("INSERT INTO %s (comment_id, body, comment_link, idea_id, user_id)" +
                    " VALUES (default, ?, ?, ?, ?);", commentTableName);;

    /**
     * safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO
     * commentmediatable VALUES (default, ?, ?, ?, ?)");
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
            mInsertOne = mConnection.prepareStatement(SQL_INSERT_ONE);
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
     * @param body = the body of the comment
     * @param comment_link = a link option in the comment
     * @param idea_id = id of the idea to post comment under
     * @param user_id = the id of the user posting comment
     * @return The number of rows that were inserted
     */
    int insertComment(String body, String comment_link, int idea_id, int user_id) {
        if (mInsertOne == null) // not yet initialized, do lazy init
            init_mInsertOne(); // lazy init
        int count = 0;
        try {
            System.out.println("Database operation: insertComment(String, Int)");
            mInsertOne.setString(1, body);
            mInsertOne.setString(2, comment_link);
            mInsertOne.setInt(3, idea_id);
            mInsertOne.setInt(4, user_id);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    ////////////////////////// UPDATE COMMENT //////////////////////////
    /** ps to replace the comment in commenttable for specified row with given value */
    private PreparedStatement mUpdateComment;
    /** the SQL for mUpdateComment */
    private static final String SQL_UPDATE_NOTE = String.format("UPDATE %s" +
            " SET body = ?, link = ?" +
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
            mUpdateComment = mConnection.prepareStatement(SQL_UPDATE_NOTE);
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
     * @param id = The comment id of the comment to update
     * @param body = The new body of the comment
     * @pafam comment_link = The new comment link attached to the comment
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateComment(int id, String body, String comment_link) {
        if (mUpdateComment == null)
            init_mUpdateNote();
        int res = -1;
        try {
            System.out.println("Database operation: updateComment(int id, String body)");
            mUpdateComment.setString(1, body);
            mUpdateComment.setString(2, comment_link);
            mUpdateComment.setInt(3, id);
            res = mUpdateComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from commenttable */
    private PreparedStatement mSelectAll;

    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_ALL_TBLDATA = String.format("SELECT *" +
            " FROM %s;", commentTableName);

    /**
     * safely performs mSelectAll = mConnection.prepareStatement("comment_id, body, comment_link, idea_id, user_id
     * FROM commenttable");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectAll() {
        // return true on success, false otherwise
        try {
            mSelectAll = mConnection.prepareStatement(SQL_SELECT_ALL_TBLDATA);
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
     * Query the database for a list of all comment_id, body, comment_link, idea_id, and user_id
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
                String comment_link = rs.getString("comment_link");
                int idea_id = rs.getInt("idea_id");
                int user_id = rs.getInt("user_id");
                RowData data = new RowData(comment_id, body, comment_link, idea_id, user_id);
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
     * @return boolean
     */
    // Method to call disconnect on database
    boolean disconnect() {
        return commentDB.disconnect();
    }

}
