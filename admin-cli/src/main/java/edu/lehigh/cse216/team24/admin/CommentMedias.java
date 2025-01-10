package edu.lehigh.cse216.team24.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CommentMedias {
    // Global variable to allow us to change name of the table in one place,
    // everywhere
    private static String commentMediaTableName = "CommentMediaTable";

    /**
     * Connection to db. An open connection if non-null, null otherwise. And
     * database object which encloses the new connection passed from Database class
     */
    private Connection mConnection;
    private Database commentMediaDB;

    // Parameterized constructor allowing us to pass in a commentMediaTableName
    public CommentMedias(String n) {
        commentMediaDB = Database.getDatabase();
        mConnection = commentMediaDB.mConnection;
        commentMediaTableName = n;
    }

    public CommentMedias() {
        commentMediaDB = Database.getDatabase();
        mConnection = commentMediaDB.mConnection;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int comment_media_id, int comment_id, String file_name, String mime_type, String drive_file_id, Timestamp access_time, boolean validCommentMedia) {
    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;

    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (comment_media_id SERIAL PRIMARY KEY, " +
                    "comment_id INT NOT NULL, file_name VARCHAR(512) NOT NULL, mime_type VARCHAR(512) NOT NULL, drive_file_id VARCHAR(512) NOT NULL, access_time TIMESTAMP NOT NULL, " +
                    "validCommentMedia BOOLEAN DEFAULT true, FOREIGN KEY (comment_id) REFERENCES CommentTable(comment_id));",
                    commentMediaTableName);

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
            commentMediaDB.disconnect();
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
    private static final String SQL_DROP_TABLE = String.format("DROP TABLE IF EXISTS %s CASCADE", commentMediaTableName);

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
            commentMediaDB.disconnect();
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
            .format("INSERT INTO %s (comment_media_id, comment_id, file_name, mime_type, drive_file_id, access_time, validCommentMedia)" +
                    " VALUES (default, ?, ?, ?, ?, ?, default);", commentMediaTableName);;

    /**
     * safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO
     * commentmediatable VALUES (default, ?, ?, ?, ?, ?, default)");
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
            commentMediaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
    }

    /**
     * Insert a row into the database
     * 
     * @param body    the body of the comment
     * @param idea_id id of the idea to post comment under
     * @param user_id the id of the user posting comment
     * @return The number of rows that were inserted
     */
    int insertCommentMedia(int comment_id, String file_name, String mime_type, String drive_file_id, Timestamp access_time) {
        if (mInsertOne == null) // not yet initialized, do lazy init
            init_mInsertOne(); // lazy init
        int count = 0;
        try {
            System.out.println("Database operation: insertCommentMedia(String, Int)");
            mInsertOne.setInt(1, comment_id);
            mInsertOne.setString(2, file_name);
            mInsertOne.setString(3, mime_type);
            mInsertOne.setString(4, drive_file_id);
            mInsertOne.setTimestamp(5, access_time);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    ////////////////////////// UPDATE COMMENT MEDIA //////////////////////////
    /** ps to replace the idea in tabledata for specified row with given value */
    private PreparedStatement mUpdateCommentMedia;
    /** the SQL for mUpdateComment */
    private static final String SQL_UPDATE_NOTE = String.format("UPDATE %s" +
            " SET file_name = ?, mime_type = ?, drive_file_id = ?, access_time = ?" +
            " WHERE comment_media_id = ?", commentMediaTableName);

    /**
     * safely performs an update of the user's note in database using prepared
     * statement above
     * 
     * @return true if row is updated in DB, false otherwise.
     */
    private boolean init_mUpdateNote() {
        // return true on success, false otherwise
        try {
            mUpdateCommentMedia = mConnection.prepareStatement(SQL_UPDATE_NOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateCommentMedia");
            System.err.println("Using SQL: " + SQL_UPDATE_NOTE);
            e.printStackTrace();
            commentMediaDB.disconnect(); // @TODO is disconnecting on exception what we want?
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
    int mUpdateCommentMedia(int comment_media_id, String file_name, String mime_type, String drive_file_id, Timestamp access_time) {
        if (mUpdateCommentMedia == null)
            init_mUpdateNote();
        int res = -1;
        try {
            System.out.println("Database operation: updateCommentMedia(int id, String body)");
            mUpdateCommentMedia.setString(1, file_name);
            mUpdateCommentMedia.setString(2, mime_type);
            mUpdateCommentMedia.setString(3, drive_file_id);
            mUpdateCommentMedia.setTimestamp(4, access_time);
            mUpdateCommentMedia.setInt(5, comment_media_id);
            res = mUpdateCommentMedia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// UPDATE STATUS //////////////////////////
    /** ps to replace the status of an idea for specified row with given value */
    private PreparedStatement mUpdateStatus;
    /** the SQL for mUpdateNote */
    private static final String SQL_UPDATE_STATUS = String.format("UPDATE %s" +
            " SET validCommentMedia = ?" +
            " WHERE comment_media_id = ?", commentMediaTableName);

    /**
     * safely performs an update of the user's status in database using prepared
     * statement above
     * 
     * @return true if row is updated in DB, false otherwise.
     */
    private boolean init_mUpdateStatus() {
        // return true on success, false otherwise
        try {
            mUpdateStatus = mConnection.prepareStatement(SQL_UPDATE_STATUS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateStatus");
            System.err.println("Using SQL: " + SQL_UPDATE_STATUS);
            e.printStackTrace();
            commentMediaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the status for a user in the database
     *
     * @param comment_media_id = The comment media id to update
     * @param validCommentMedia = The new status of the comment media (true is valid false is deactivated)
     * @return The number of rows that were updated. 0 indicates no rows updated or an error.
     */
    int updateStatus(int comment_media_id, boolean validCommentMedia) {
        try {
            return executeUpdateQuery(comment_media_id, validCommentMedia);
        } catch (SQLException e) {
            try {
                // Clean up old statement if it exists
                if (mUpdateStatus != null) {
                    mUpdateStatus.close();
                    mUpdateStatus = null;
                }
                // Reinitialize and retry
                if (init_mUpdateStatus()) {
                    return executeUpdateQuery(comment_media_id, validCommentMedia);
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
     * @param comment_media_id = The commment media id to update
     * @param validCommentMedia = The new status of the comment media
     * @return The number of rows that were updated
     * @throws SQLException if there's an error executing the query
     */
    private int executeUpdateQuery(int comment_media_id, boolean validCommentMedia) throws SQLException {
        if (mUpdateStatus == null) {
            if (!init_mUpdateStatus()) {
                return 0;
            }
        }

        mUpdateStatus.setBoolean(1, validCommentMedia);
        mUpdateStatus.setInt(2, comment_media_id);
        return mUpdateStatus.executeUpdate();
    }

    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mSelectAll;

    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_ALL_TBLDATA = String.format("SELECT *" +
            " FROM %s;", commentMediaTableName);

    /**
     * safely performs mSelectAll = mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
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
            commentMediaDB.disconnect(); // @TODO is disconnecting on exception what we want?
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
                int comment_media_id = rs.getInt("comment_media_id");
                int comment_id = rs.getInt("comment_id");
                String file_name = rs.getString("file_name");
                String mime_type = rs.getString("mime_type");
                String drive_file_id = rs.getString("drive_file_id");
                Timestamp access_time = rs.getTimestamp("access_time");
                Boolean validCommentMedia = rs.getBoolean("validCommentMedia");
                RowData data = new RowData(comment_media_id, comment_id, file_name, mime_type, drive_file_id, access_time, validCommentMedia);
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
        return commentMediaDB.disconnect();
    }

}
