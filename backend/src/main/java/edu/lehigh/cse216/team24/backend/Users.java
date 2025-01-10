package edu.lehigh.cse216.team24.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** 
 * The Users class manages user-related database operations including creation, 
 * reading, updating, and deleting ideas from the database.
 */
public class Users {

    // Global variable to allow us to change name of the table in one place,
    // everywhere
    private static String userTableName = "UserTable";

    /**
     * Connection to db. An open connection if non-null, null otherwise. And
     * database object which encloses the new connection passed from Database class
     */
    private Connection mConnection;
    private Database userDB;

    // Parameterized constructor allowing us to pass in a userTableName
    public Users(String n) {
        userDB = Database.getDatabase();
        mConnection = userDB.mConnection;
        userTableName = n;
    }

    public Users() {
        userDB = Database.getDatabase();
        mConnection = userDB.mConnection;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int user_id, String first, String last, String email, String gender_identity,
            String sexual_orient, String note, boolean active_user) {

    }

    public static record OtherUser(String first, String last, String email, String note) {

    }

    ////////////////////////// CREATE TABLE //////////////////////////

    /** 
     * precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;

    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
                    "user_id SERIAL PRIMARY KEY, first_name VARCHAR(32) NOT NULL, last_name VARCHAR(32) NOT NULL, " +
                    "email VARCHAR(48) NOT NULL, gender_identity VARCHAR(32), sexual_orient VARCHAR(32), " +
                    "note VARCHAR(512), active_account BOOLEAN DEFAULT true, UNIQUE (email));",
            userTableName);

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
            userDB.disconnect();
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
    private static final String SQL_DROP_TABLE = String.format("DROP TABLE IF EXISTS %s CASCADE", userTableName);

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
            userDB.disconnect();
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
            .format("INSERT INTO %s (user_id, first_name, last_name, email)" +
                    " VALUES (default, ?, ?, ?);", userTableName);;

    /**
     * safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO
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
            mInsertOne = mConnection.prepareStatement(SQL_INSERT_ONE);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            System.err.println("Using SQL: " + SQL_INSERT_ONE);
            e.printStackTrace();
            userDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
    }

    /**
     * Insert a user row into the database
     *
     * @param first the first name of the user
     * @param last  last name of the user
     * @param email the email of the new user
     * @return The number of rows that were inserted. 0 indicates no insertion or an
     *         error.
     */
    int insertRow(String first, String last, String email) {
        try {
            return executeInsertQuery(first, last, email);
        } catch (SQLException e) {
            try {
                // Clean up old statement if it exists
                if (mInsertOne != null) {
                    mInsertOne.close();
                    mInsertOne = null;
                }
                // Reinitialize and retry
                if (init_mInsertOne()) {
                    return executeInsertQuery(first, last, email);
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
    private int executeInsertQuery(String first, String last, String email) throws SQLException {
        if (mInsertOne == null) {
            if (!init_mInsertOne()) {
                return 0; // Return 0 if initialization fails
            }
        }

        mInsertOne.setString(1, first);
        mInsertOne.setString(2, last);
        mInsertOne.setString(3, email);
        System.out.println("Database operation: insertUser(String, String, String)");
        return mInsertOne.executeUpdate();
    }

    ////////////////////////// UPDATE GENDER //////////////////////////
    /** ps to replace the idea in tabledata for specified row with given value */
    private PreparedStatement mUpdateGender;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_GENDER = String.format("UPDATE %s" +
            " SET gender_identity = ?" +
            " WHERE user_id = ?", userTableName);

    /**
     * safely performs an update of the user's gender identity in database using
     * prepared statement above
     * 
     * @return true if row is updated in DB, false otherwise.
     */
    private boolean init_mUpdateGender() {
        // return true on success, false otherwise
        try {
            mUpdateGender = mConnection.prepareStatement(SQL_UPDATE_GENDER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateGender");
            System.err.println("Using SQL: " + SQL_UPDATE_GENDER);
            e.printStackTrace();
            userDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the gender identity for a user in the database
     * 
     * @param id   The user id of the user to update
     * @param idea The new gender identity
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateGender(int user_id, String gender) {
        if (mUpdateGender == null)
            init_mUpdateGender();
        int res = -1;
        try {
            System.out.println("Database operation: updateGender(int user_id, String gender)");
            mUpdateGender.setString(1, gender);
            mUpdateGender.setInt(2, user_id);
            res = mUpdateGender.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// UPDATE SEXUAL ORIENT //////////////////////////
    /** ps to replace the idea in tabledata for specified row with given value */
    private PreparedStatement mUpdateSexualOrient;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_SEXUAL_ORIENT = String.format("UPDATE %s" +
            " SET sexual_orient = ?" +
            " WHERE user_id = ?", userTableName);

    /**
     * safely performs an update of the user's sexual orientation in database using
     * prepared statement above
     * 
     * @return true if row is updated in DB, false otherwise.
     */
    private boolean init_mUpdateSexualOrient() {
        // return true on success, false otherwise
        try {
            mUpdateSexualOrient = mConnection.prepareStatement(SQL_UPDATE_SEXUAL_ORIENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateSexualOrient");
            System.err.println("Using SQL: " + SQL_UPDATE_SEXUAL_ORIENT);
            e.printStackTrace();
            userDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the sexual orientation for a user in the database
     * 
     * @param id   The user id of the user to update
     * @param idea The new sexual orientation
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateSexualOrient(int user_id, String sexualOrient) {
        if (mUpdateSexualOrient == null)
            init_mUpdateSexualOrient();
        int res = -1;
        try {
            System.out.println("Database operation: updateSexualOrient(int user_id, String sexualOrient)");
            mUpdateSexualOrient.setString(1, sexualOrient);
            mUpdateSexualOrient.setInt(2, user_id);
            res = mUpdateSexualOrient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// UPDATE NOTE //////////////////////////
    /** ps to replace the idea in tabledata for specified row with given value */
    private PreparedStatement mUpdateNote;
    /** the SQL for mUpdateNote */
    private static final String SQL_UPDATE_NOTE = String.format("UPDATE %s" +
            " SET note = ?" +
            " WHERE user_id = ?", userTableName);

    /**
     * safely performs an update of the user's note in database using prepared
     * statement above
     * 
     * @return true if row is updated in DB, false otherwise.
     */
    private boolean init_mUpdateNote() {
        // return true on success, false otherwise
        try {
            mUpdateNote = mConnection.prepareStatement(SQL_UPDATE_NOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateNote");
            System.err.println("Using SQL: " + SQL_UPDATE_NOTE);
            e.printStackTrace();
            userDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the note for a user in the database
     * 
     * @param id   The user id of the user to update
     * @param idea The new note
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateNote(int user_id, String note) {
        if (mUpdateNote == null)
            init_mUpdateNote();
        int res = -1;
        try {
            System.out.println("Database operation: updateNote(int user_id, String note)");
            mUpdateNote.setString(1, note);
            mUpdateNote.setInt(2, user_id);
            res = mUpdateNote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// UPDATE STATUS //////////////////////////
    /** ps to update the status of a user (invalidate or validate them) */
    private PreparedStatement mUpdateStatus;
    /** the SQL for mUpdateNote */
    private static final String SQL_UPDATE_STATUS = String.format("UPDATE %s" +
            " SET active_account = ?" +
            " WHERE user_id = ?", userTableName);

    /**
     * safely performs an update of the user's status in database using prepared
     * statement above
     *
     * @return true if prepared statement is created successfully, false otherwise.
     */
    private boolean init_mUpdateStatus() {
        try {
            if (mUpdateStatus != null) {
                mUpdateStatus.close();
            }
            mUpdateStatus = mConnection.prepareStatement(SQL_UPDATE_STATUS);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateStatus");
            System.err.println("Using SQL: " + SQL_UPDATE_STATUS);
            e.printStackTrace();
            try {
                if (mUpdateStatus != null) {
                    mUpdateStatus.close();
                    mUpdateStatus = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Update the status for a user in the database
     *
     * @param user_id     The user id of the user to update
     * @param active_user The new status of the user (true is valid false is
     *                    deactivated)
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateStatus(int user_id, boolean active_user) {
        // First try to use existing prepared statement
        try {
            return executeUpdateStatusQuery(user_id, active_user);
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Reinitialize
                if (init_mUpdateStatus()) {
                    return executeUpdateStatusQuery(user_id, active_user);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        }
    }

    /**
     * Executes the update status query with the given parameters
     *
     * @param user_id     The user id to update
     * @param active_user The new active status
     * @return Number of rows updated, or -1 on error
     * @throws SQLException if there's a database error
     */
    private int executeUpdateStatusQuery(int user_id, boolean active_user) throws SQLException {
        if (mUpdateStatus == null) {
            if (!init_mUpdateStatus()) {
                return -1;
            }
        }

        mUpdateStatus.setBoolean(1, active_user);
        mUpdateStatus.setInt(2, user_id);
        return mUpdateStatus.executeUpdate();
    }

    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from tblData, but only the id and idea columns */
    private PreparedStatement mSelectAllUsers;

    /** the SQL for mSelectAllUsers */
    private static final String SQL_SELECT_ALL_TBLDATA = String.format("SELECT *" +
            " FROM %s;", userTableName);

    /**
     * safely performs mSelectAllUsers = mConnection.prepareStatement("SELECT id,
     * idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectAll() {
        // return true on success, false otherwise
        try {
            mSelectAllUsers = mConnection.prepareStatement(SQL_SELECT_ALL_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllUsers");
            System.err.println("Using SQL: " + SQL_SELECT_ALL_TBLDATA);
            e.printStackTrace();
            userDB.disconnect(); // @TODO is disconnecting on exception what we want?
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
        // First try to use existing prepared statement
        try {
            return executeSelectAllQuery();
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (mSelectAllUsers != null) {
                    mSelectAllUsers.close();
                    mSelectAllUsers = null;
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
        if (mSelectAllUsers == null) {
            init_mSelectAll();
        }

        ArrayList<RowData> res = new ArrayList<>();

        try (ResultSet rs = mSelectAllUsers.executeQuery()) {
            while (rs.next()) {
                res.add(new RowData(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("gender_identity"),
                        rs.getString("sexual_orient"),
                        rs.getString("note"),
                        rs.getBoolean("active_account")));
            }
        }
        return res;
    }

    ////////////////////////// SELECT ONE //////////////////////////
    /** ps to return row from tblData with matching id */
    private PreparedStatement mSelectOne;

    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_ONE_TBLDATA = String.format("SELECT *" +
            " FROM %s" +
            " WHERE user_id=? ;", userTableName);

    /**
     * safely performs mSelectOne = mConnection.prepareStatement("SELECT * from
     * tblData WHERE id=?");
     * 
     * @return true if one row is selected, false otherwise.
     */
    private boolean init_mSelectOne() {
        try {
            mSelectOne = mConnection.prepareStatement(SQL_SELECT_ONE_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            System.err.println("Using SQL: " + SQL_SELECT_ONE_TBLDATA);
            e.printStackTrace();
            userDB.disconnect(); // @TODO is disconnecting on exception what we want?
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
            return null;
        }
    }

    /**
     * Function to execute query to search for one user
     * 
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
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("gender_identity"),
                        rs.getString("sexual_orient"),
                        rs.getString("note"),
                        rs.getBoolean("active_account"));
            }
            return null; // none found
        }
    }

    /**
     * Get all data for a specific row, by ID for user with other profile
     * 
     * @param row_id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    OtherUser selectOtherUser(int row_id) {
        try {
            return executeSelectOtherOneQuery(row_id);
        } catch (SQLException e) {
            try {
                if (mSelectOne != null) {
                    mSelectOne.close();
                    mSelectOne = null;
                }
                if (init_mSelectOne()) {
                    return executeSelectOtherOneQuery(row_id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Function to execute search for another user's profile and get back limited
     * information
     * 
     * @param row_id
     * @return OtherUser
     * @throws SQLException
     */
    private OtherUser executeSelectOtherOneQuery(int row_id) throws SQLException {
        if (mSelectOne == null) {
            init_mSelectOne();
        }
        mSelectOne.setInt(1, row_id);
        try (ResultSet rs = mSelectOne.executeQuery()) {
            if (rs.next()) {
                return new OtherUser(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("note"));
            }
            return null; // none found
        }
    }

    ////////////////////////// UPDATE PROFILE //////////////////////////
    private PreparedStatement profileUpdate;
    /** the SQL for profileUpdate */
    private static final String PROFILE_SQL_UPDATE_USERTABLE = String.format("UPDATE %s " +
            "SET gender_identity = ?, sexual_orient = ?, note = ? "
            +
            "WHERE user_id = ?;", userTableName);

    /**
     * Initializes the PreparedStatement for updating a user profile
     * 
     * @return true on success, false otherwise.
     */
    private boolean init_profileUpdate() {
        try {
            profileUpdate = mConnection.prepareStatement(PROFILE_SQL_UPDATE_USERTABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: profileUpdate");
            System.err.println("Using SQL: " + PROFILE_SQL_UPDATE_USERTABLE);
            e.printStackTrace();
            this.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Profile Update
     * 
     * @param id The id of the row to update
     * @return The number of rows that were updated
     */
    int updateProfile(int id, String gId, String sOrient, String note) {
        if (profileUpdate == null) // not yet initialized, do lazy init
            init_profileUpdate(); // lazy init
        int count = 0;
        try {
            System.out.println(
                    "Database operation: profileUpdate(int, String, String, String)");
            profileUpdate.setString(1, gId);
            profileUpdate.setString(2, sOrient);
            profileUpdate.setString(3, note);
            profileUpdate.setInt(4, id); // Set the user id for the row
            count += profileUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * @return boolean
     */
    // Method to call disconnect on database
    boolean disconnect() {
        return userDB.disconnect();
    }
}