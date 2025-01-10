package edu.lehigh.cse216.team24.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class IdeaMedias {
    // Global variable to allow us to change name of the table in one place,
    // everywhere
    private static String ideaMediaTableName = "IdeaMediaTable";

    /**
     * Connection to db. An open connection if non-null, null otherwise. And
     * database object which encloses the new connection passed from Database class
     */
    private Connection mConnection;
    private Database ideaMediaDB;

    // Parameterized constructor allowing us to pass in a userTableName
    public IdeaMedias(String n) {
        ideaMediaDB = Database.getDatabase();
        mConnection = ideaMediaDB.mConnection;
        ideaMediaTableName = n;
    }

    public IdeaMedias() {
        ideaMediaDB = Database.getDatabase();
        mConnection = ideaMediaDB.mConnection;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int idea_media_id, int idea_id, String file_name, String mime_type, String drive_file_id, Timestamp access_time, Boolean validIdeaMedia) {
    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;

    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (idea_media_id SERIAL PRIMARY KEY, " +
                    "idea_id INT NOT NULL, file_name VARCHAR(512) NOT NULL, mime_type VARCHAR(512) NOT NULL, drive_file_id VARCHAR(512) NOT NULL, access_time TIMESTAMP NOT NULL, " +
                    "validIdeaMedia BOOLEAN DEFAULT true, FOREIGN KEY (idea_id) REFERENCES IdeaTable(idea_id));",
                    ideaMediaTableName);

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
            ideaMediaDB.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Create ideamediatable. If it already exists, this will print an error
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
    /** ps to delete table ideamediatable from the database */
    private PreparedStatement mDropTable;

    /** the SQL for mDropTable */
    private static final String SQL_DROP_TABLE = String.format("DROP TABLE IF EXISTS %s CASCADE", ideaMediaTableName);

    /**
     * safely performs mDropTable = mConnection.prepareStatement("DROP TABLE
     * ideamediatable");
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
            ideaMediaDB.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Remove ideamediatable from the database. If it does not exist, this will print
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
     * ps to instert into ideamediatable a new row with next auto-gen id and the five given
     * values
     */
    private PreparedStatement mInsertOne;

    /** the SQL for mInsertOne */
    private static final String SQL_INSERT_ONE = String
            .format("INSERT INTO %s (idea_media_id, idea_id, file_name, mime_type, drive_file_id, access_time, validIdeaMedia)" +
                    " VALUES (default, ?, ?, ?, ?, ?, default);", ideaMediaTableName);;

    /**
     * safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO
     * ideamediatable VALUES (default, ?, ?, ?, ?, ?, default)");
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
            ideaMediaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
    }

    /**
     * Insert a row into the database
     * 
     * @param idea_id = the idea that the media is attached to
     * @param file_name = the file name of the media
     * @param mime_type = the mime type of the media
     * @param drive_file_id = the google drive file id for that media
     * @param access_time = the time that the media was uploaded
     * @param validIdeaMedia = the validity of the idea media
     * 
     */
    int insertIdeaMedia(int idea_id, String file_name, String mime_type, String drive_file_id, Timestamp access_time) {
        if (mInsertOne == null) // not yet initialized, do lazy init
            init_mInsertOne(); // lazy init
        int count = 0;
        try {
            System.out.println("Database operation: insertCommentMedia(String, Int)");
            mInsertOne.setInt(1, idea_id);
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

    ////////////////////////// UPDATE IDEA MEDIA STATUS //////////////////////////
    /** ps to replace the status of an idea for specified row with given value */
    private PreparedStatement mUpdateStatus;
    /** the SQL for mUpdateNote */
    private static final String SQL_UPDATE_STATUS = String.format("UPDATE %s" +
            " SET validIdeaMedia = ?" +
            " WHERE idea_media_id = ?", ideaMediaTableName);

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
            ideaMediaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the status for a user in the database
     *
     * @param idea_media_id = The idea media id to update
     * @param validIdeaMedia = The new status of the idea media (true is valid false is deactivated)
     * @return The number of rows that were updated. 0 indicates no rows updated or an error.
     */
    int updateStatus(int idea_media_id, boolean validIdeaMedia) {
        try {
            return executeUpdateQuery(idea_media_id, validIdeaMedia);
        } catch (SQLException e) {
            try {
                // Clean up old statement if it exists
                if (mUpdateStatus != null) {
                    mUpdateStatus.close();
                    mUpdateStatus = null;
                }
                // Reinitialize and retry
                if (init_mUpdateStatus()) {
                    return executeUpdateQuery(idea_media_id, validIdeaMedia);
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
     * @param idea_media_id = The idea media id to update
     * @param validIdeaMedia = The new status of the comment media
     * @return The number of rows that were updated
     * @throws SQLException if there's an error executing the query
     */
    private int executeUpdateQuery(int idea_media_id, boolean validIdeaMedia) throws SQLException {
        if (mUpdateStatus == null) {
            if (!init_mUpdateStatus()) {
                return 0;
            }
        }

        mUpdateStatus.setBoolean(1, validIdeaMedia);
        mUpdateStatus.setInt(2, idea_media_id);
        return mUpdateStatus.executeUpdate();
    }


    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from ideamediatable */
    private PreparedStatement mSelectAll;

    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_ALL_TBLDATA = String.format("SELECT *" +
            " FROM %s;", ideaMediaTableName);

    /**
     * safely performs mSelectAll = mConnection.prepareStatement("SELECT *
     * FROM ideamediatable");
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
            ideaMediaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a list of all idea media id, idea id, file name, mime type, drive file id, and access time
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
                int idea_media_id = rs.getInt("idea_media_id");
                int idea_id = rs.getInt("idea_id");
                String file_name = rs.getString("file_name");
                String mime_type = rs.getString("mime_type");
                String drive_file_id = rs.getString("drive_file_id");
                Timestamp access_time = rs.getTimestamp("access_time");
                Boolean validIdeaMedia = rs.getBoolean("validMediaIdea");
                RowData data = new RowData(idea_media_id, idea_id, file_name, mime_type, drive_file_id, access_time, validIdeaMedia);
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
        return ideaMediaDB.disconnect();
    }

}
