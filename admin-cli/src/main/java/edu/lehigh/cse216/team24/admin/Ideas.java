package edu.lehigh.cse216.team24.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Ideas {

    // Global variable to allow us to change name of the table in one place,
    // everywhere
    private static String ideaTableName = "IdeaTable";

    /**
     * Connection to db. An open connection if non-null, null otherwise. And
     * database object which encloses the new connection passed from Database class
     */
    private Connection mConnection;
    private Database ideaDB;

    // Parameterized constructor allowing us to pass in a ideaTableName
    public Ideas(String n) {
        ideaDB = Database.getDatabase();
        mConnection = ideaDB.mConnection;
        ideaTableName = n;
    }

    public Ideas() {
        ideaDB = Database.getDatabase();
        mConnection = ideaDB.mConnection;
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * tightly coupled with each entry in the databse.
     */
    public static record RowData(int mIdeaId, int mUserId, String mIdea, String idea_link, boolean idea_active) {

    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;

    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (idea_id SERIAL PRIMARY KEY, " +
                    "user_id int NOT NULL, idea VARCHAR(512) NOT NULL, idea_link VARCHAR(512), " +
                    "idea_valid BOOLEAN DEFAULT true, FOREIGN KEY (user_id) REFERENCES UserTable(user_id));",
            ideaTableName);

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
            ideaDB.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Create ideatable. If it already exists, this will print an error
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
    /** ps to delete table ideatable from the database */
    private PreparedStatement mDropTable;

    /** the SQL for mDropTable */
    private static final String SQL_DROP_TABLE = String.format("DROP TABLE IF EXISTS %s CASCADE", ideaTableName);

    /**
     * safely performs mDropTable = mConnection.prepareStatement("DROP TABLE
     * ideatable");
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
            ideaDB.disconnect();
            return false;
        }
        return true;
    }

    /**
     * Remove ideatable from the database. If it does not exist, this will print
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
     * ps to instert into ideatable a new row with next auto-gen id, the 3 given, and auto-gen validity
     * check
     */
    private PreparedStatement mInsertOne;

    /** the SQL for mInsertOne */
    private static final String SQL_INSERT_ONE = String
            .format("INSERT INTO %s (idea_id, user_id, idea, idea_link, idea_valid)" +
                    " VALUES (default, ?, ?, ?, default);", ideaTableName);;

    /**
     * safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO
     * ideatable VALUES (default, ?, ?, ?, default)");
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
            ideaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
    }

    /**
     * Insert a row into the database
     *
     * @param user_id = the id of the user posting
     * @param idea = idea content
     * @param idea_link = the link attached to the idea
     * @return The number of rows that were inserted
     */
    int insertIdea(int user_id, String idea, String idea_link) {
        try {
            return executeInsertQuery(user_id, idea, idea_link);
        } catch (SQLException e) {
            try {
                // Clean up old statement if it exists
                if (mInsertOne != null) {
                    mInsertOne.close();
                    mInsertOne = null;
                }
                // Reinitialize and retry
                if (init_mInsertOne()) {
                    return executeInsertQuery(user_id, idea, idea_link);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0; // Return 0 to indicate no rows were inserted
        }
    }

    /**
     * Execute the insert query with the given parameters
     *
     * @param user_id = the id of the user posting
     * @param idea = idea content
     * @param idea_link = the link attached to the idea
     * @return The number of rows that were inserted
     * @throws SQLException if there's an error executing the query
     */
    private int executeInsertQuery(int user_id, String idea, String idea_link) throws SQLException {
        if (mInsertOne == null) {
            if (!init_mInsertOne()) {
                return 0; // Return 0 if initialization fails
            }
        }

        mInsertOne.setInt(1, user_id);
        mInsertOne.setString(2, idea);
        mInsertOne.setString(3, idea_link);
        System.out.println("Database operation: insertIdea(int, String)");
        return mInsertOne.executeUpdate();
    }

    ////////////////////////// UPDATE STATUS //////////////////////////
    /** ps to replace the status of an idea for specified row with given value */
    private PreparedStatement mUpdateStatus;
    /** the SQL for mUpdateNote */
    private static final String SQL_UPDATE_STATUS = String.format("UPDATE %s" +
            " SET idea_valid = ?" +
            " WHERE idea_id = ?", ideaTableName);

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
            ideaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the status for a user in the database
     *
     * @param idea_id = The idea id of the idea to update
     * @param idea_active = The new status of the idea (true is valid false is deactivated)
     * @return The number of rows that were updated. 0 indicates no rows updated or an error.
     */
    int updateStatus(int idea_id, boolean idea_active) {
        try {
            return executeUpdateQuery(idea_id, idea_active);
        } catch (SQLException e) {
            try {
                // Clean up old statement if it exists
                if (mUpdateStatus != null) {
                    mUpdateStatus.close();
                    mUpdateStatus = null;
                }
                // Reinitialize and retry
                if (init_mUpdateStatus()) {
                    return executeUpdateQuery(idea_id, idea_active);
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
     * @param idea_id = The idea id of the idea to update
     * @param idea_active = The new status of the idea
     * @return The number of rows that were updated
     * @throws SQLException if there's an error executing the query
     */
    private int executeUpdateQuery(int idea_id, boolean idea_active) throws SQLException {
        if (mUpdateStatus == null) {
            if (!init_mUpdateStatus()) {
                return 0;
            }
        }

        mUpdateStatus.setBoolean(1, idea_active);
        mUpdateStatus.setInt(2, idea_id);
        return mUpdateStatus.executeUpdate();
    }

    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from database */
    private PreparedStatement mSelectAllIdeas;

    /** the SQL for mSelectAllIdeas */
    private static final String SQL_SELECT_IDEAS = String.format("SELECT *" +
            " FROM %s;", ideaTableName);

    /**
     * safely performs mSelectAllIdeas = mConnection.prepareStatement("SELECT *
     * FROM ideatable");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectAll() {
        // return true on success, false otherwise
        try {
            mSelectAllIdeas = mConnection.prepareStatement(SQL_SELECT_IDEAS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllIdeas");
            System.err.println("Using SQL: " + SQL_SELECT_IDEAS);
            e.printStackTrace();
            ideaDB.disconnect(); // @TODO is disconnecting on exception what we want?
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
        try {
            return executeSelectAllQuery();
        } catch (SQLException e) {
            try {
                // Clean up old statement if it exists
                if (mSelectAllIdeas != null) {
                    mSelectAllIdeas.close();
                    mSelectAllIdeas = null;
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
    private ArrayList<RowData> executeSelectAllQuery() throws SQLException {
        if (mSelectAllIdeas == null) {
            init_mSelectAll();
        }

        ArrayList<RowData> res = new ArrayList<>();

        try (ResultSet rs = mSelectAllIdeas.executeQuery()) {
            while (rs.next()) {
                res.add(new RowData(
                        rs.getInt("idea_id"),
                        rs.getInt("user_id"),
                        rs.getString("idea"),
                        rs.getString("idea_link"),
                        rs.getBoolean("idea_valid")));
            }
        }
        return res;
    }

    ////////////////////////// SELECT ONE //////////////////////////
    /** ps to return row from ideatable with matching id */
    private PreparedStatement mSelectOne;

    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_ONE = String.format("SELECT *" +
            " FROM %s" +
            " WHERE id=? ;", ideaTableName);

    /**
     * safely performs mSelectOne = mConnection.prepareStatement("SELECT * from
     * ideatable WHERE id=?");
     * 
     * @return true if one row is selected, false otherwise.
     */
    private boolean init_mSelectOne() {
        try {
            mSelectOne = mConnection.prepareStatement(SQL_SELECT_ONE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            System.err.println("Using SQL: " + SQL_SELECT_ONE);
            e.printStackTrace();
            ideaDB.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param row_id = The id of the row being requested
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

    private RowData executeSelectOneQuery(int row_id) throws SQLException {
        if (mSelectOne == null) {
            init_mSelectOne();
        }

        mSelectOne.setInt(1, row_id);

        try (ResultSet rs = mSelectOne.executeQuery()) {
            if (rs.next()) {
                return new RowData(
                        rs.getInt("idea_id"),
                        rs.getInt("user_id"),
                        rs.getString("idea"),
                        rs.getString("idea_link"),
                        rs.getBoolean("idea_valid"));
            }
            return null; // or consider returning Optional.empty()
        }
    }

    // Method to call disconnect on database
    boolean disconnect() {
        return ideaDB.disconnect();
    }

}
