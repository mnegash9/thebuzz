package edu.lehigh.cse216.team24.backend;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The IdeaMedia class manages ideamedia-related database operations including
 * creation,
 * reading, updating, and deleting ideas from the database.
 */
public class IdeaMedia {

    // Global variable to allow us to change name of the table in one place,
    // everywhere
    private static String ideaMediaTableName = "ideaMediaTable";

    private Database ideaMediaDB;

    // Parameterized constructor allowing us to pass in a ideaMediaTableName
    public IdeaMedia(String n) {
        ideaMediaDB = Buzz.db;
        ideaMediaTableName = n;
    }

    public IdeaMedia() {
        ideaMediaDB = Buzz.db;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int idea_media_id, int idea_id, String file_name, String mime_type,
            String drive_file_id, Object timestamp) {

    }

    ////////////////////////// DROP TABLE //////////////////////////
    /** ps to delete table tbldata from the database */
    private PreparedStatement mDropTable;

    /** the SQL for mDropTable */
    private static final String SQL_DROP_TABLE = String.format("DROP TABLE IF EXISTS %s CASCADE", ideaMediaTableName);

    /**
     * safely performs mDropTable = ideaMediaDB.mConnection.prepareStatement("DROP TABLE
     * tblData");
     * 
     * @return true if table is dropped, false otherwise.
     */
    private boolean init_mDropTable() {
        try {
            mDropTable = ideaMediaDB.mConnection.prepareStatement(SQL_DROP_TABLE);
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
            .format("INSERT INTO %s (idea_media_id, idea_id, file_name, mime_type, drive_file_id, access_time)" +
                    " VALUES (default, ?, ?, ?, ?, ?);", ideaMediaTableName);;

    /**
     * safely performs mInsertOne = ideaMediaDB.mConnection.prepareStatement("INSERT INTO
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
            mInsertOne = ideaMediaDB.mConnection.prepareStatement(SQL_INSERT_ONE);
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
     * Function to insert an ideamedia element into the database
     * 
     * @param idea_id       the idea id associated with the media element
     * @param file_name     filename
     * @param mime_type     type of content that is being added
     * @param drive_file_id the String file identifier used by google drive
     * @return integer result of how many elements were inserted 1 or 0.
     */
    int insertRow(int idea_id, String file_name, String mime_type, String drive_file_id) {
        try {
            return executeInsertQuery(idea_id, file_name, mime_type, drive_file_id);
        } catch (SQLException e) {
            try {
                // Clean up old statement if it exists
                if (mInsertOne != null) {
                    mInsertOne.close();
                    mInsertOne = null;
                }
                // Reinitialize and retry
                if (init_mInsertOne()) {
                    return executeInsertQuery(idea_id, file_name, mime_type, drive_file_id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0; // Return 0 to indicate no rows were inserted
        }
    }

    /**
     * Execute the insert query with the given user parameters
     *
     * @param first the first name of the user
     * @param last  last name of the user
     * @param email the email of the new user
     * @return The number of rows that were inserted
     * @throws SQLException if there's an error executing the query
     */
    private int executeInsertQuery(int idea_id, String file_name, String mime_type, String drive_file_id)
            throws SQLException {
        if (mInsertOne == null) {
            if (!init_mInsertOne()) {
                return 0; // Return 0 if initialization fails
            }
        }
        // "INSERT INTO ideamediatable (idea_media_id, idea_id, file_name, mime_type,
        // drive_file_id, access_time) VALUES (default, ?, ?, ?, ?, ?);"
        mInsertOne.setInt(1, idea_id);
        mInsertOne.setString(2, file_name);
        mInsertOne.setString(3, mime_type);
        mInsertOne.setString(4, drive_file_id);
        mInsertOne.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        System.out.println("Database operation: insertIdeaMedia(int, String, String, String, Timestamp)");
        return mInsertOne.executeUpdate();
    }

    ////////////////////////// UPDATE TIMESTAMP //////////////////////////
    /** ps to replace the idea in tabledata for specified row with given value */
    private PreparedStatement mUpdateTimestamp;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_TIMESTAMP = String.format("UPDATE %s" +
            " SET access_time = ?" + " WHERE drive_file_id = ?", ideaMediaTableName);

    /**
     * safely performs an update of the timestamp in database using drive file id
     * prepared statement above
     * 
     * @return true if row is updated in DB, false otherwise.
     */
    private boolean init_mUpdateTimestamp() {
        // return true on success, false otherwise
        try {
            mUpdateTimestamp = ideaMediaDB.mConnection.prepareStatement(SQL_UPDATE_TIMESTAMP);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateTimestamp");
            System.err.println("Using SQL: " + SQL_UPDATE_TIMESTAMP);
            e.printStackTrace();
            ideaMediaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the timestamp for idea media in the database
     * 
     * @param drive_file_id The file id of the google drive to update
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateTimestamp(int drive_file_id) {
        if (mUpdateTimestamp == null)
            init_mUpdateTimestamp();
        int res = -1;
        try {
            System.out.println("Database operation: updateTimestamp(int drive_file_id)");
            mUpdateTimestamp.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            mUpdateTimestamp.setInt(2, drive_file_id);
            res = mUpdateTimestamp.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Function to disconnect ideaMediaDB.mConnection using database class
     * 
     * @return boolean true if disconnected cleanly, false if not
     */
    // Method to call disconnect on database
    boolean disconnect() {
        return ideaMediaDB.disconnect();
    }
}