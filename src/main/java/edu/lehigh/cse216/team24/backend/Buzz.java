package edu.lehigh.cse216.team24.backend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

/*
 * Class to create all necessary databases and methods for the Buzz app
 * And create necessary views
 * Users
 * Ideas
 * Comments
 * Votes
 */
public class Buzz {

    /// instances of other classes needed
    private static Users userDB;
    private static Ideas ideaDB;
    private static Comments commentDB;
    private static Upvotes upvote;
    private static Downvotes downvote;
    private static IdeaMedia ideaMediaDB;
    private static CommentMedia commentMediaDB;

    // public connection (to check in app) and database
    public Database db;

    public Buzz() {
        // database object
        db = Database.getDatabase();
        userDB = new Users();
        ideaDB = new Ideas();
        commentDB = new Comments();
        upvote = new Upvotes();
        downvote = new Downvotes();
        ideaMediaDB = new IdeaMedia();
        commentMediaDB = new CommentMedia();
    }

    /**
     * Create tables for all databases.
     */
    void createTables() {
        userDB.createTable();
        ideaDB.createTable();
        commentDB.createTable();
        upvote.createTable();
        downvote.createTable();
    }

    /**
     * Remove tables from the database.
     */
    void dropTables() {
        ideaMediaDB.dropTable();
        commentMediaDB.dropTable();
        downvote.dropTable();
        upvote.dropTable();
        commentDB.dropTable();
        ideaDB.dropTable();
        userDB.dropTable();
    }

    /**
     * Add a user to the user database
     * 
     * @param first first name of the user to add
     * @param last  last name of the user to add
     * @param email email of the user to
     * @return boolean true if the user was added, false if not
     */
    boolean addUser(String first, String last, String email) {
        return 1 == userDB.insertRow(first, last, email);
    }

    /**
     * Get a user from the user database
     * 
     * @param user_id user_id of the user to get
     * @return RowData of the comment received
     */
    Users.RowData getUser(int user_id) {
        return userDB.selectOne(user_id);
    }

    /**
     * Update profile infor for user in the database
     * 
     * @param user_id user_id of the user to update
     * @param gId     gender identity of the profile to update
     * @param sOrient updated sexual orientation to update
     * @param note    udpated note for the user profile
     * @return boolean true if profile was successfully updated, false if not
     */
    boolean updateProfile(int id, String gId, String sOrient, String note) {
        return 1 == userDB.updateProfile(id, gId, sOrient, note);
    }

    /**
     * Get a user from the user database without owning
     * 
     * @param user_id user_id of the user to get
     * @return RowData of the comment received
     */
    Users.OtherUser getOtherUser(int user_id) {
        return userDB.selectOtherUser(user_id);
    }

    /**
     * Add an idea to the idea database
     * 
     * @param user_id user id posting the idea
     * @param idea    idea body to post
     * @return idea_id of the row that was inserted into the idea database
     */
    int addIdea(int user_id, String idea) {
        return ideaDB.insertIdea(user_id, idea);
    }

    /**
     * Add a link to the idea database
     * 
     * @param idea_id idea id of the row to add link for
     * @param link    the link to add to the database
     * @return boolean true if successful, false if not
     */
    boolean addLinkToIdea(int idea_id, String link) {
        return 1 == ideaDB.addLink(idea_id, link);
    }

    /**
     * Get an idea from the idea database
     * 
     * @param idea_id idea_id of the idea to get
     * @return RowData of the idea received
     */
    Ideas.RowData getIdea(int idea_id) {
        return ideaDB.selectOne(idea_id);
    }

    /**
     * Get all ideas as RowData elements
     * 
     * @return list of ideas in the idea database
     */
    ArrayList<Ideas.RowData> getAllIdeas() {
        return ideaDB.selectAll();
    }

    /**
     * Add a commentDB to the commentDB database
     * 
     * @param body    body of the commentDB
     * @param idea_id idea to post the commentDB under
     * @param user_id user id posting the commentDB
     * @return integer of the row corresponding to last added row, or 0 if
     *         unsuccessful
     */
    int addComment(String body, int idea_id, int user_id) {
        return commentDB.insertComment(body, idea_id, user_id);
    }

    /**
     * Add a link to the comment database
     * 
     * @param comment_id comment_id of the comment to add the link to
     * @param link       the link to add to the database
     * @return boolean true if successful, false if not
     */
    boolean addLinkToComment(int comment_id, String link) {
        return 1 == commentDB.addLink(comment_id, link);
    }

    /**
     * Update a comment to the comment database
     * 
     * @param comment_id id of the comment to update
     * @param body       body of the commentDB
     * @return boolean true if the commentDB was added, false if not
     */
    boolean updateComment(int comment_id, String body) {
        return 1 == commentDB.updateComment(comment_id, body);
    }

    /**
     * Get an comment based on the idea database
     * 
     * @param idea_id idea_id for the set of comments to get for idea
     * @return RowData of the comment received
     */
    ArrayList<Comments.RowData> getComments(int idea_id) {
        return commentDB.selectComments(idea_id);
    }

    /**
     * Get all comments as RowData elements
     * 
     * @return list of comments in the commentDB database
     */
    ArrayList<Comments.RowData> getAllComments() {
        return commentDB.selectAll();
    }

    /**
     * Add an upvote ot teh upvote database
     * 
     * @param user_id user id that is upvoting
     * @param idea_id idea_id of the post to upvote
     * @return boolean true if the upvote was added, false if an upvote existed and
     *         was removed
     */
    boolean addUpvote(int user_id, int idea_id) {
        // check if a user has a downvote - if so delete it first
        if (downvote.hasDownvote(user_id, idea_id)) {
            downvote.deleteDownvote(user_id, idea_id);
        }
        return upvote.insertUpvote(user_id, idea_id);
    }

    /**
     * Add a downvote to the downvote database
     * 
     * @param user_id user id that is downvoting
     * @param idea_id idea_id of the post to downvote
     * @return boolean true if the downvote was added, false if a a downvote existed
     *         and was removed
     */
    boolean addDownvote(int user_id, int idea_id) {
        // check if a user has upvote - if so delete it first
        if (upvote.hasUpvote(user_id, idea_id)) {
            upvote.deleteUpvote(user_id, idea_id);
        }
        return downvote.insertDownvote(user_id, idea_id);
    }

    /**
     * Function to invalidate a user (account_status -> false)
     * 
     * @param user_id user id to invalidate
     * @return boolean true if succesful, false if not
     */
    boolean invalidateUser(int user_id) {
        return 1 == userDB.updateStatus(user_id, false);
    }

    /**
     * Function to validate a user (account_status -> true)
     * 
     * @param user_id user id to validate
     * @return boolean true if succesful, false if not
     */
    boolean validateUser(int user_id) {
        return 1 == userDB.updateStatus(user_id, true);
    }

    /**
     * Function to get a user id from the database matching a given email
     * 
     * @param email email of user to look up
     * @return Integer user id -> null if not founr
     */
    public Integer getUserIdByEmail(String email) {
        String GET_USERID_FROM_EMAIL = "SELECT user_id FROM usertable WHERE email = ?;";

        try (PreparedStatement stmt = db.mConnection.prepareStatement(GET_USERID_FROM_EMAIL)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                // Only attempt to get user_id if there is a result
                return rs.next() ? rs.getInt("user_id") : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Function to invalidate an idea (idea_valid -> false)
     * 
     * @param user_id user id to invalidate
     * @return boolean true if succesful, false if not
     */
    boolean invalidateIdea(int idea_id) {
        return 1 == ideaDB.updateStatus(idea_id, false);
    }

    /**
     * Function to validate a idea (idea_valid -> true)
     * 
     * @param user_id user id to validate
     * @return boolean true if succesful, false if not
     */
    boolean validateIdea(int idea_id) {
        return 1 == ideaDB.updateStatus(idea_id, true);
    }

    /*
     * Disconnect all databases
     */
    void disconnect() {
        userDB.disconnect();
        ideaDB.disconnect();
        commentDB.disconnect();
        upvote.disconnect();
        downvote.disconnect();
        ideaMediaDB.disconnect();
    }

    /**
     * Add a file to the cloud storage for given idea
     *
     * @param idea_id      idea_id corresponding to the idea to post under
     * @param user_id      user id of the user posting content
     * @param base64String file to post
     * @return String of file id if successful or 'failed' if unsuccessful
     */
    String addFileToIdea(int idea_id, int user_id, String base64String) {
        File tempFile = null;

        try {
            // Validate inputs
            if (base64String == null || base64String.trim().isEmpty()) {
                return "failed";
            }

            // Create temp directory if it doesn't exist
            File tempDir = new File("./tmp");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // Decode base64 string
            byte[] fileBytes = Base64.getDecoder().decode(base64String);

            // Generate unique filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String uniqueID = UUID.randomUUID().toString().substring(0, 8);
            String fileName = String.format("%s_%s_%d.file", timestamp, uniqueID, user_id);

            // Create temporary file
            tempFile = new File(tempDir + "/" + fileName);

            // Write the decoded bytes to the temporary file
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileBytes);
                fos.flush();
            }

            // Detect MIME type
            String mimeType = Files.probeContentType(tempFile.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // Default mime type if unable todetect
            }

            final DriveStorageService gDrive = new DriveStorageService("1Ckg-oqaC2eCegtzYrbrOO216eGMqtDeW");

            // Upload to Google Drive
            String fileId = gDrive.uploadFile(tempFile, mimeType, fileName);

            // Add to database
            ideaMediaDB.insertRow(idea_id, fileName, mimeType, fileId);

            // Return true if we got a file ID back
            if (fileId != null && !fileId.isEmpty())
                return fileId;
            else
                return "failed";

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return "failed";
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            return "failed";
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return "failed";
        } finally {
            // Clean up temporary file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

    }

    /**
     * Add a file to the cloud storage for given comment
     *
     * @param comment_id   comment_id corresponding to the idea to post under
     * @param user_id      user id of the user posting content
     * @param base64String file to post
     * @return String of file id if successful or 'failed' if unsuccessful
     */
    String addFileToComment(int comment_id, int user_id, String base64String) {
        File tempFile = null;

        try {
            // Validate inputs
            if (base64String == null || base64String.trim().isEmpty()) {
                return "failed";
            }

            // Create temp directory if it doesn't exist
            File tempDir = new File("./tmp");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // Decode base64 string
            byte[] fileBytes = Base64.getDecoder().decode(base64String);

            // Generate unique filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String uniqueID = UUID.randomUUID().toString().substring(0, 8);
            String fileName = String.format("%s_%s_%d.file", timestamp, uniqueID, user_id);

            // Create temporary file
            tempFile = new File(tempDir + "/" + fileName);

            // Write the decoded bytes to the temporary file
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileBytes);
                fos.flush();
            }

            // Detect MIME type
            String mimeType = Files.probeContentType(tempFile.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // Default mime type if unable todetect
            }

            final DriveStorageService gDrive = new DriveStorageService("1Ckg-oqaC2eCegtzYrbrOO216eGMqtDeW");

            // Upload to Google Drive
            String fileId = gDrive.uploadFile(tempFile, mimeType, fileName);

            // Add to database
            commentMediaDB.insertRow(comment_id, fileName, mimeType, fileId);

            // Return true if we got a file ID back
            if (fileId != null && !fileId.isEmpty())
                return fileId;
            else
                return "failed";

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return "failed";
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            return "failed";
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return "failed";
        } finally {
            // Clean up temporary file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

    }

    /**
     * Function to delete a file given fileId from drive
     * 
     * @param fileId file id to delete from drive
     * @return void
     */
    void deleteFile(String fileId) {
        try {
            final DriveStorageService gDrive = new DriveStorageService("1Ckg-oqaC2eCegtzYrbrOO216eGMqtDeW");
            gDrive.deleteFile(fileId);
        } catch (Exception e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }

    /**
     * Function to download a file given fileId from drive
     * 
     * @param fileId          file id to download from drive
     * @param destinationFile a File to put the contents into
     * @return void
     */
    void downloadFile(String fileId, File tmp) {
        try {
            final DriveStorageService gDrive = new DriveStorageService("1Ckg-oqaC2eCegtzYrbrOO216eGMqtDeW");

            gDrive.downloadFile(fileId, tmp);
        } catch (Exception e) {
            System.err.println("Error downloading file: " + e.getMessage());
        }
    }

    //
    // CREATING VIEWS
    //
    /**
     * RowData is the data we envision stored in a row on the database. It is
     * coupled with each entry in the databse.
     */
    public static record RowData(int user_id, String first_name, String last_name, int idea_id,
            String idea, int comment_id, String body, int commenter_id, String commenter_first, String commenter_last,
            String ideaf_id, String commentf_id, int upvote_count, int downvote_count) {
    }

    /**
     * SentData is the data we envision sent to web and mobiloe. It is
     * coupled with each entry in the databse.
     */
    public static record SentData(int user_id, String first_name, String last_name, int idea_id,
            String idea, int comment_id, String body, int commenter_id, String commenter_first, String commenter_last,
            String idea_file, String comment_file, int upvote_count, int downvote_count) {
    }

    /**
     * CommentViewData is the data we envision sent to mobile for comments. It is
     * coupled with each entry in the databse.
     */
    public static record CommentViewData(int comment_id, String body, String comment_link, int idea_id,
            int user_id, String comment_file) {
    }

    //////////////////// CREATE DASHBOARD VIEW ////////
    void createView() {
        String SQL_CREATE_VIEW = """
                DROP VIEW IF EXISTS dashboard_view;
                CREATE OR REPLACE VIEW dashboard_view AS
                SELECT
                    u.user_id,
                    u.first_name,
                    u.last_name,
                    i.idea_id,
                    i.idea,
                    i.idea_link,
                    c.comment_id,
                    c.body,
                    c.comment_link,
                    c.user_id AS commenter_id,
                    commenter.first_name AS commenter_first,
                    commenter.last_name AS commenter_last,
                    idea_media.drive_file_id AS ideaf_id,
                    comment_media.drive_file_id AS commentf_id,
                    COUNT(DISTINCT up.upvote_id) as upvote_count,
                    COUNT(DISTINCT dv.downvote_id) as downvote_count
                FROM
                    usertable u
                    JOIN ideatable i ON u.user_id = i.user_id
                    LEFT JOIN commenttable c ON i.idea_id = c.idea_id
                    LEFT JOIN usertable commenter ON c.user_id = commenter.user_id
                    LEFT JOIN ideamediatable idea_media ON i.idea_id = idea_media.idea_id
                    LEFT JOIN commentmediatable comment_media ON c.comment_id = comment_media.comment_id
                    LEFT JOIN upvotetable up ON i.idea_id = up.idea_id
                    LEFT JOIN downvotetable dv ON i.idea_id = dv.idea_id
                WHERE
                    u.active_account = true
                    AND i.idea_valid = true
                GROUP BY
                    u.user_id,
                    u.first_name,
                    u.last_name,
                    i.idea_id,
                    i.idea,
                    c.comment_id,
                    c.body,
                    c.user_id,
                    commenter.first_name,
                    commenter.last_name,
                    idea_media.idea_media_id,
                    comment_media.comment_media_id
                """;

        try (PreparedStatement stmt = db.mConnection.prepareStatement(SQL_CREATE_VIEW)) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Error creating dashboard view: " + e.getMessage());
            e.printStackTrace();
            db.disconnect(); // Maintaining the existing disconnect behavior
        }
    }

    ////////////////////////// GET DASHBOARD //////////////////////////
    /**
     * Query the database for the created view
     * 
     * @return All rows, as an ArrayList
     */
    public ArrayList<SentData> getDashboard() {
        // createView(); // Ensure view exists
        ArrayList<SentData> results = new ArrayList<>();

        String query = """
                    SELECT user_id, first_name, last_name, idea_id, idea,
                           comment_id, body, commenter_id, commenter_first,
                           commenter_last, ideaf_id, commentf_id,
                           upvote_count, downvote_count
                    FROM dashboard_view
                """;

        try (PreparedStatement stmt = db.mConnection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Get base data from result set
                String ideaf_id = rs.getString("ideaf_id");
                String commentf_id = rs.getString("commentf_id");

                // Process file attachments
                String ideaFile = processAttachment(ideaf_id);
                String commentFile = processAttachment(commentf_id);

                // Create and add the SentData object
                results.add(new SentData(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("idea_id"),
                        rs.getString("idea"),
                        rs.getInt("comment_id"),
                        rs.getString("body"),
                        rs.getInt("commenter_id"),
                        rs.getString("commenter_first"),
                        rs.getString("commenter_last"),
                        ideaFile,
                        commentFile,
                        rs.getInt("upvote_count"),
                        rs.getInt("downvote_count")));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    //////////////////// CREATE COMMENT MEDIA VIEW ////////

    void createCommentsView() {
        String SQL_CREATE_VIEW = """
                DROP VIEW IF EXISTS comment_media_view;
                CREATE VIEW comment_media_view AS
                SELECT
                    c.comment_id,
                    c.body,
                    c.comment_link,
                    c.idea_id,
                    c.user_id,
                    cm.comment_media_id,
                    cm.file_name,
                    cm.mime_type,
                    cm.drive_file_id,
                    cm.access_time
                FROM commenttable c
                LEFT JOIN commentmediatable cm
                    ON c.comment_id = cm.comment_id AND cm.validcommentmedia = true;
                                """;

        try (PreparedStatement stmt = db.mConnection.prepareStatement(SQL_CREATE_VIEW)) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Error creating comments view: " + e.getMessage());
            e.printStackTrace();
            db.disconnect(); // Maintaining the existing disconnect behavior
        }
    }

    ////////////////////////// GET COMMENTS VIEW MOBILE //////////////////////////
    /**
     * Query the database for the created view
     * 
     * @return All rows, as an ArrayList
     */
    public ArrayList<CommentViewData> getCommentViewMobile(int idea_id) {
        // createCommentsView(); // Ensure view exists
        ArrayList<CommentViewData> results = new ArrayList<>();

        String query = """
                    SELECT comment_id, body, comment_link, idea_id, user_id, drive_file_id
                    FROM comment_media_view
                    WHERE idea_id = ?;
                """;

        try (PreparedStatement stmt = db.mConnection.prepareStatement(query)) {
            // setting the parameter and executing query
            stmt.setInt(1, idea_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Get file data from result set
                String commentf_id = rs.getString("drive_file_id");

                // Process file attachments
                String commentFile = processAttachment(commentf_id);

                // Create and add the SentData object
                results.add(new CommentViewData(
                        rs.getInt("comment_id"),
                        rs.getString("body"),
                        rs.getString("comment_link"),
                        rs.getInt("idea_id"),
                        rs.getInt("user_id"),
                        commentFile));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Function to process attachments and use the download function to downlaod,
     * convert to base 64 and return output as string
     * 
     * @param fileId Google Drive file id to use to attach the file
     * @return String
     */
    // Helper method to process file attachments
    private String processAttachment(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            return "";
        }

        File tmp = new File("fileFromDrive.tmp");
        try {
            downloadFile(fileId, tmp);
            byte[] fileContent = Files.readAllBytes(tmp.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            System.err.println("Error processing attachment " + fileId + ": " + e.getMessage());
            return "";
        } finally {
            if (tmp.exists()) {
                tmp.delete();
            }
        }
    }

}
