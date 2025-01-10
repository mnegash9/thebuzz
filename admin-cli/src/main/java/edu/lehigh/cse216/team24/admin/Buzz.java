package edu.lehigh.cse216.team24.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/*
 * Class to create all necessary databases and methods for the Buzz app
 * And create necessary views
 * Users
 * Ideas
 * Comments
 * Votes
 * Idea Media
 * Comment Media
 */
public class Buzz {

    /// instances of other classes needed
    private static Users user;
    private static Ideas ideaDB;
    private static Comments comment;
    private static Upvotes upvote;
    private static Downvotes downvote;
    private static IdeaMedias ideaMedia;
    private static CommentMedias commentMedia;

    // private connection and database
    private static Database bz;
    private static Connection mConnection;

    public Buzz() {
        // database object
        bz = Database.getDatabase();
        mConnection = bz.mConnection;

        user = new Users();
        ideaDB = new Ideas();
        comment = new Comments();
        upvote = new Upvotes();
        downvote = new Downvotes();
        ideaMedia = new IdeaMedias();
        commentMedia = new CommentMedias();
    }

    /**
     * Create tables for all databases.
     */
    void createTables() {
        user.createTable();
        ideaDB.createTable();
        comment.createTable();
        upvote.createTable();
        downvote.createTable();
        ideaMedia.createTable();
        commentMedia.createTable();
    }

    /**
     * Remove tables from the database.
     */
    void dropTables() {
        downvote.dropTable();
        upvote.dropTable();
        comment.dropTable();
        ideaDB.dropTable();
        user.dropTable();
        ideaMedia.dropTable();
        commentMedia.dropTable();
    }

    /**
     * Add a user to the user database
     * 
     * @param first = first name of the user to add
     * @param last = last name of the user to add
     * @param email = email of the user to
     * @return boolean true if the user was added, false if not
     */
    boolean addUser(String first, String last, String email) {
        return 1 == user.insertRow(first, last, email);
    }

    /**
     * Add an idea to the idea database
     * 
     * @param user_id = user id posting the idea
     * @param idea = idea body to post
     * @param idea_link = the link attached to the idea
     * @return boolean true if the idea was posted, false if not
     */
    boolean addIdea(int user_id, String idea, String idea_link) {
        return 1 == ideaDB.insertIdea(user_id, idea, idea_link);
    }

    /**
     * Add a comment to the comment database
     * 
     * @param body = body of the comment
     * @param comment_link = the link attached to the comment
     * @param idea_id = idea to post the comment under
     * @param user_id = user id posting the comment
     * @return boolean true if the comment was added, false if not
     */
    boolean addComment(String body, String comment_link, int idea_id, int user_id) {
        return 1 == comment.insertComment(body, comment_link, idea_id, user_id);
    }

    /**
     * Add a comment media to the commentmedia database
     * 
     * @param comment_id = the comment id for which the media will be attached to
     * @param file_name = the file name of the media
     * @param mime_type = the mime type of the media
     * @param drive_file_id = the drive file id of the media in the google drive
     * @param access_time = the time that the comment media was uploaded
     * @return boolean true if the comment media was added, false if not
     */
    boolean addCommentMedia(int comment_id, String file_name, String mime_type, String drive_file_id, Timestamp access_time) {
        return 1 == commentMedia.insertCommentMedia(comment_id, file_name, mime_type, drive_file_id, access_time);
    }

    /**
     * Add a idea media to the commentmedia database
     * 
     * @param idea_id = the idea id for which the media will be attached to
     * @param file_name = the file name of the media
     * @param mime_type = the mime type of the media
     * @param drive_file_id = the drive file id of the media in the google drive
     * @param access_time = the time that the idea media was uploaded
     * @return boolean true if the idea media was added, false if not
     */
    boolean addIdeaMedia(int idea_id, String file_name, String mime_type, String drive_file_id, Timestamp access_time) {
        return 1 == ideaMedia.insertIdeaMedia(idea_id, file_name, mime_type, drive_file_id, access_time);
    }

    /**
     * Add an upvote ot teh upvote database
     * 
     * @param user_id = user id that is upvoting
     * @param idea_id = idea id of the post to upvote
     * @return boolean true if the upvote was added, false if an upvote existed and was removed
     */
    boolean addUpvote(int user_id, int idea_id) {
        return upvote.insertUpvote(user_id, idea_id);
    }

    /**
     * Add a downvote to the downvote database
     * 
     * @param user_id = user id that is downvoting
     * @param idea_id = idea id of the post to downvote
     * @return boolean true if the downvote was added, false if a a downvote existed and was removed
     */
    boolean addDownvote(int user_id, int idea_id) {
        return downvote.insertDownvote(user_id, idea_id);
    }

    /**
     * Function to invalidate a user (account_status -> false)
     * 
     * @param user_id = user id to invalidate
     * @return boolean true if succesful, false if not
     */
    boolean invalidateUser(int user_id) {
        return 1 == user.updateStatus(user_id, false);
    }

    /**
     * Function to validate a user (account_status -> true)
     * 
     * @param user_id = user id to validate
     * @return boolean true if succesful, false if not
     */
    boolean validateUser(int user_id) {
        return 1 == user.updateStatus(user_id, true);
    }

    /**
     * Function to invalidate an idea (idea_valid -> false)
     * 
     * @param user_id = user id to invalidate
     * @return boolean true if succesful, false if not
     */
    boolean invalidateIdea(int idea_id) {
        return 1 == ideaDB.updateStatus(idea_id, false);
    }

    /**
     * Function to validate a idea (idea_valid -> true)
     * 
     * @param user_id = user id to validate
     * @return boolean true if succesful, false if not
     */
    boolean validateIdea(int idea_id) {
        return 1 == ideaDB.updateStatus(idea_id, true);
    }

    /**
     * Function to invalidate an idea (idea_valid -> false)
     * 
     * @param user_id = user id to invalidate
     * @return boolean true if succesful, false if not
     */
    boolean invalidateIdeaMedia(int idea_id) {
        return 1 == ideaMedia.updateStatus(idea_id, false);
    }

    /**
     * Function to validate a idea (idea_valid -> true)
     * 
     * @param user_id = user id to validate
     * @return boolean true if succesful, false if not
     */
    boolean validateIdeaMedia(int idea_id) {
        return 1 == ideaMedia.updateStatus(idea_id, true);
    }

    /**
     * Function to invalidate an idea (idea_valid -> false)
     * 
     * @param user_id = user id to invalidate
     * @return boolean true if succesful, false if not
     */
    boolean invalidateCommentMedia(int comment_id) {
        return 1 == commentMedia.updateStatus(comment_id, false);
    }

    /**
     * Function to validate a idea (idea_valid -> true)
     * 
     * @param user_id = user id to validate
     * @return boolean true if succesful, false if not
     */
    boolean validateCommentMedia(int idea_id) {
        return 1 == commentMedia.updateStatus(idea_id, true);
    }

    /*
     * Disconnect all databases
     */
    void disconnect() {
        user.disconnect();
        ideaDB.disconnect();
        comment.disconnect();
        upvote.disconnect();
        downvote.disconnect();
        commentMedia.disconnect();
        ideaMedia.disconnect();
    }

    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int user_id, String first, String last, int idea_id,
            String idea, String idea_link, int comment_id, String body, String comment_link, int commenter_id, String commenter_first, String commenter_last, String ideaf_id, String commentf_id,
            int upvotes, int downvotes) {

    }

    //////////////////// CREATE DASHBOARD VIEW ////////
    private PreparedStatement createView;

   /** the SQL for mSelectAll */
   private static final String SQL_CREATE_VIEW = String
           .format("DROP VIEW IF EXISTS dashboard_view; \n" + //
                   "CREATE OR REPLACE VIEW dashboard_view AS\n" + //
                   "SELECT \n" + //
                   "    u.user_id,\n" + //
                   "    u.first_name,\n" + //
                   "    u.last_name,\n" + //
                   "    i.idea_id,\n" + //
                   "    i.idea,\n" + //
                   "    i.idea_link,\n" + //
                   "    c.comment_id,\n" + //
                   "    c.body,\n" + //
                   "    c.comment_link,\n" + //
                   "    c.user_id AS commenter_id,\n" + //
                   "    commenter.first_name AS commenter_first,\n" + //
                   "    commenter.last_name AS commenter_last,\n" + //
                   "    idea_media.drive_file_id AS ideaf_id,\n" + //
                   "    comment_media.drive_file_id AS commentf_id,\n" + //
                   "    COUNT(DISTINCT up.upvote_id) as upvote_count,\n" + //
                   "    COUNT(DISTINCT dv.downvote_id) as downvote_count\n" + //
                   "FROM \n" + //
                   "    usertable u\n" + //
                   "    JOIN ideatable i ON u.user_id = i.user_id\n" + //
                   "    LEFT JOIN commenttable c ON i.idea_id = c.idea_id\n" + //
                   "    LEFT JOIN usertable commenter ON c.user_id = commenter.user_id\n" + //
                   "    LEFT JOIN ideamediatable idea_media ON i.idea_id = idea_media.idea_id\n" + //
                   "    LEFT JOIN commentmediatable comment_media ON c.comment_id = comment_media.comment_id\n" + //
                   "    LEFT JOIN upvotetable up ON i.idea_id = up.idea_id\n" + //
                   "    LEFT JOIN downvotetable dv ON i.idea_id = dv.idea_id\n" + //
                   "WHERE \n" + //
                   "   u.active_account = true " + //
                   "   AND i.idea_valid = true " + //
                   "GROUP BY \n" + //
                   "    u.user_id,\n" + //
                   "    u.first_name,\n" + //
                   "    u.last_name,\n" + //
                   "    i.idea_id,\n" + //
                   "    i.idea,\n" + //
                   "    i.idea_link,\n" + //
                   "    c.comment_id,\n" + //
                   "    c.body,\n" + //
                   "    c.comment_link,\n" + //
                   "    c.user_id,\n" + //
                   "    commenter.first_name,\n" + //
                   "    commenter.last_name,\n" + //
                   "    idea_media.idea_media_id,\n" + //
                   "    comment_media.comment_media_id;");

    /**
     * safely performs mSelectAll = mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_createView() {
        // return true on success, false otherwise
        try {
            createView = mConnection.prepareStatement(SQL_CREATE_VIEW);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: createView");
            System.err.println("Using SQL: " + SQL_CREATE_VIEW);
            e.printStackTrace();
            bz.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Create view of dashbaord for the database
     * 
     * @return void
     */
    void createView() {
        // First try to use existing prepared statement
        try {
            executeCreateView();
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (createView != null) {
                    createView.close();
                    createView = null;
                }
                // Reinitialize
                if (init_createView()) {
                    executeCreateView();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    /** 
     * @throws SQLException
     */
    // Separate the query execution logic
    private void executeCreateView() throws SQLException {
        if (createView == null) {
            init_createView();
        }
        createView.execute();
    }

    ////////////////////////// GET DASHBOARD //////////////////////////
    /** ps to return all rows from dashboard view */
    private PreparedStatement mGetDashboard;

    /** the SQL for mSelectAllUsers */
    private static final String SQL_GET_DASHBOARD = String.format("SELECT *" +
            " FROM dashboard_view;");

    /**
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectAll() {
        // return true on success, false otherwise
        try {
            mGetDashboard = mConnection.prepareStatement(SQL_GET_DASHBOARD);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllUsers");
            System.err.println("Using SQL: " + SQL_GET_DASHBOARD);
            e.printStackTrace();
            bz.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for the created view
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> getDashboard() {
        // First create dashboard view if doesn't exist
        this.createView();
        try {
            return executeSelectAllQuery();
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (mGetDashboard != null) {
                    mGetDashboard.close();
                    mGetDashboard = null;
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
        if (mGetDashboard == null) {
            init_mSelectAll();
        }
        ArrayList<RowData> res = new ArrayList<>();
        try (ResultSet rs = mGetDashboard.executeQuery()) {
            while (rs.next()) {
                res.add(new RowData(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("idea_id"),
                        rs.getString("idea"),
                        rs.getString("idea_link"),
                        rs.getInt("comment_id"),
                        rs.getString("body"),
                        rs.getString("comment_link"),
                        rs.getInt("commenter_id"),
                        rs.getString("commenter_first"),
                        rs.getString("commenter_last"),
                        rs.getString("ideaf_id"),
                        rs.getString("commentf_id"),
                        rs.getInt("upvote_count"),
                        rs.getInt("downvote_count")));
            }
        }
        return res;
    }

    
    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData2(int comment_id, String body, int idea_id, int user_id, String comment_link, int comment_media_id,
            String file_name, String mime_type, String drive_file_id, Timestamp access_time) {

    }

    //////////////////// CREATE COMMENT VIEW ////////
    private PreparedStatement createCommentView;

   /** the SQL for mSelectAll */
   private static final String SQL_CREATE_COMMENT_VIEW = String
           .format("DROP VIEW IF EXISTS comment_media_view; \n" + //
                   "CREATE OR REPLACE VIEW comment_media_view AS\n" + //
                   "SELECT \n" + //
                   "    c.comment_id,\n" + //
                   "    c.body,\n" + //
                   "    c.idea_id,\n" + //
                   "    c.user_id, \n" + //
                   "    c.comment_link,\n" + //
                   "    cm.comment_media_id,\n" + //
                   "    cm.file_name,\n" + //
                   "    cm.mime_type,\n" + //
                   "    cm.drive_file_id,\n" + //
                   "    cm.access_time,\n" + //
                   "FROM \n" + //
                   "    commentable c\n" + //
                   "    LEFT JOIN commentmediatable cm ON c.comment_id = cm.comment_id\n" + //
                   "WHERE \n" + //
                   "   cm.validcommentmedia = true " + //
                   "GROUP BY \n" + //
                   "    c.comment_id,\n" + //
                   "    c.body,\n" + //
                   "    c.idea_id,\n" + //
                   "    c.user_id, \n" + //
                   "    c.comment_link,\n" + //
                   "    cm.comment_media_id,\n" + //
                   "    cm.file_name,\n" + //
                   "    cm.mime_type,\n" + //
                   "    cm.drive_file_id,\n" + //
                   "    cm.access_time;");

    /**
     * safely performs mSelectAll = mConnection.prepareStatement("SELECT id, idea
     * FROM tblData");
     * 
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_createCommentView() {
        // return true on success, false otherwise
        try {
            createCommentView = mConnection.prepareStatement(SQL_CREATE_COMMENT_VIEW);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: createView");
            System.err.println("Using SQL: " + SQL_CREATE_COMMENT_VIEW);
            e.printStackTrace();
            bz.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Create view of comment and comment media for the database
     * 
     * @return void
     */
    void createCommentView() {
        // First try to use existing prepared statement
        try {
            executeCreateView();
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (createCommentView != null) {
                    createCommentView.close();
                    createCommentView = null;
                }
                // Reinitialize
                if (init_createCommentView()) {
                    executeCreateCommentView();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    /** 
     * @throws SQLException
     */
    // Separate the query execution logic
    private void executeCreateCommentView() throws SQLException {
        if (createCommentView == null) {
            init_createCommentView();
        }
        createCommentView.execute();
    }

    ////////////////////////// GET COMMENT //////////////////////////
    /** ps to return all rows from comment view */
    private PreparedStatement mGetComment;

    /** the SQL for mSelectAllComments */
    private static final String SQL_GET_COMMENT = String.format("SELECT *" +
            " FROM comment_media_view;");

    /**
     * @return true if all rows can be selected, false otherwise.
     */
    private boolean init_mSelectAllComment() {
        // return true on success, false otherwise
        try {
            mGetComment = mConnection.prepareStatement(SQL_GET_COMMENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllUsers");
            System.err.println("Using SQL: " + SQL_GET_COMMENT);
            e.printStackTrace();
            bz.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for the created view
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData2> getComment() {
        // First create dashboard view if doesn't exist
        this.createCommentView();
        try {
            return executeSelectAllCommentQuery();
        } catch (SQLException e) {
            // If the prepared statement is invalid/closed, try to reinitialize it
            try {
                // Clean up old statement if it exists
                if (mGetComment != null) {
                    mGetComment.close();
                    mGetComment = null;
                }
                // Reinitialize
                if (init_mSelectAllComment()) {
                    return executeSelectAllCommentQuery();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return new ArrayList<>(); // Return empty list instead of null
        }
    }

    // Separate the query execution logic
    private ArrayList<RowData2> executeSelectAllCommentQuery() throws SQLException {
        if (mGetComment == null) {
            init_mSelectAllComment();
        }
        ArrayList<RowData2> res = new ArrayList<>();
        try (ResultSet rs = mGetComment.executeQuery()) {
            while (rs.next()) {
                res.add(new RowData2(
                    rs.getInt("comment_id"),
                    rs.getString("body"),
                    rs.getInt("idea_id"),
                    rs.getInt("user_id"),
                    rs.getString("comment_link"),
                    rs.getInt("comment_media_id"),
                    rs.getString("file_name"),
                    rs.getString("mime_type"),
                    rs.getString("drive_file_id"),
                    rs.getTimestamp("access_time")));
            }
        }
        return res;
    }

}
