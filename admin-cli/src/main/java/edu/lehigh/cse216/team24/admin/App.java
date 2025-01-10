package edu.lehigh.cse216.team24.admin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * App is our basic admin app.
 * This class provides a text-based menu system for performing basic database
 * operations on the data table.
 * 
 * The application requires proper database configuration through environment
 * variables before startup. It will exit if unable to establish a database connection.
 * 
 * Please use your tutorial database when running mvn package and the project
 * database when running mvn exec:java. The tests require a clean test subject
 * and thus clear the database before running.
 */
public class App {

    /**
     * Main method. Simply runs mainCliLoop
     * 
     * @param argv = standard Java argument. not used in this program.
     */
    public static void main(String[] argv) {
        mainCliLoop(argv);
    }

    static Buzz buzz;

    /**
     * Entry point for our admin command-line interface program.
     * 
     * Runs a loop that gets a request from the user and processes it using our Database class.
     * 
     * @param argv = Command-line options. Ignored by this program.
     */
    public static void mainCliLoop(String[] argv) {
        // Get a fully-configured connection to the database, or exit immediately
        buzz = new Buzz();
        if (buzz == null) {
            System.err.println("Unable to make database object, exiting.");
            System.exit(1);
        }

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            // function call
            String action = prompt(in);
            if (action.equalsIgnoreCase("?")) {
                menu();
            } else if (action.equalsIgnoreCase("q")) {
                break;
            } else if (action.equalsIgnoreCase("T")) {
                buzz.createTables();
            } else if (action.equalsIgnoreCase("D")) {
                buzz.dropTables();
            } else if (action.equalsIgnoreCase("*")) {
                printAllDashboard();
            } else if (action.equalsIgnoreCase("#")) {
                printAllComment();
            }else if (action.equalsIgnoreCase("adduser")) {
                String first = getString(in, "Enter the first name");
                String last = getString(in, "Enter the last name");
                String email = getString(in, "Enter the email");

                boolean res = buzz.addUser(first, last, email);
                System.out.println("user added: " + res);
            } else if (action.equalsIgnoreCase("addidea")) {
                int userId = getInt(in, "Enter the user id");
                String ideaBody = getString(in, "Enter idea");
                String ideaLink = getString(in, "Enter link");

                boolean res = buzz.addIdea(userId, ideaBody, ideaLink);
                System.out.println("idea added: " + res);
            } else if (action.equalsIgnoreCase("addcomment")) {
                String body = getString(in, "Enter the body of the comment");
                String commentLink = getString(in, "Enter a link");
                int ideaId = getInt(in, "Enter the idea id");
                int userId = getInt(in, "Enter the user id");

                boolean res = buzz.addComment(body, commentLink, ideaId, userId);
                System.out.println("comment added: " + res);
            } else if (action.equalsIgnoreCase("addcommentmedia")) {
                int commentId = getInt(in, "Enter the comment id");
                String commentFileName = getString(in, "Enter the name of the file");
                String commentMimeType = getString(in, "Enter the mime type");
                String commentDriveFileID = getString(in, "Entter the drive file id");
                String commentAccessTimeString = getString(in, "Enter the access time (YYYY-MM-DD HH:mm:ss)");
                Timestamp commentAccessTime = Timestamp.valueOf(commentAccessTimeString);

                boolean res = buzz.addCommentMedia(commentId, commentFileName, commentMimeType, commentDriveFileID, commentAccessTime);
                System.out.println("comment file added: " + res);
            } else if (action.equalsIgnoreCase("addideamedia")) {
                int ideaId = getInt(in, "Enter the idea id");
                String ideaFileName = getString(in, "Enter the name of the file");
                String ideaMimeType = getString(in, "Enter the mime type");
                String ideaDriveFileID = getString(in, "Entter the drive file id");
                String ideaAccessTimeString = getString(in, "Enter the access time (YYYY-MM-DD HH:mm:ss)");
                Timestamp ideaAccessTime = Timestamp.valueOf(ideaAccessTimeString);

                boolean res = buzz.addCommentMedia(ideaId, ideaFileName, ideaMimeType, ideaDriveFileID, ideaAccessTime);
                System.out.println("comment file added: " + res);
            } else if (action.equalsIgnoreCase("addupvote")) {
                int userId = getInt(in, "Enter the user id");
                int ideaId = getInt(in, "Enter the idea id");

                boolean res = buzz.addUpvote(userId, ideaId);
                System.out.println("upvote added: " + res);
            } else if (action.equalsIgnoreCase("adddownvote")) {
                int userId = getInt(in, "Enter the user id");
                int ideaId = getInt(in, "Enter the idea id");

                boolean res = buzz.addDownvote(userId, ideaId);
                System.out.println("downvote added: " + res);
            } else if (action.equalsIgnoreCase("invalidateuser")) {
                int userId = getInt(in, "Enter the user id");

                boolean res = buzz.invalidateUser(userId);
                System.out.println("user invalidated: " + res);
            } else if (action.equalsIgnoreCase("validateuser")) {
                int userId = getInt(in, "Enter the user id");

                boolean res = buzz.validateUser(userId);
                System.out.println("user validated: " + res);
            } else if (action.equalsIgnoreCase("invalidateidea")) {
                int ideaId = getInt(in, "Enter the idea id");

                boolean res = buzz.invalidateIdea(ideaId);
                System.out.println("idea invalidated: " + res);
            } else if (action.equalsIgnoreCase("validateidea")) {
                int ideaId = getInt(in, "Enter the idea id");

                boolean res = buzz.validateIdea(ideaId);
                System.out.println("idea validated: " + res);
            }  else if (action.equalsIgnoreCase("validateidea")) {
                int ideaId = getInt(in, "Enter the idea id");

                boolean res = buzz.validateIdea(ideaId);
                System.out.println("idea validated: " + res);
            } else if (action.equalsIgnoreCase("validateIdeaMedia")) {
                int ideaMediaId = getInt(in, "Enter the idea id");

                boolean res = buzz.validateIdeaMedia(ideaMediaId);
                System.out.println("idea media validate: " + res);
            } else if (action.equalsIgnoreCase("invalidateIdeaMedia")) {
                int ideaMediaId = getInt(in, "Enter the idea id");

                boolean res = buzz.invalidateIdeaMedia(ideaMediaId);
                System.out.println("idea media invalidated: " + res);
            } else if (action.equalsIgnoreCase("validateCommentMedia")) {
                int commentMediaId = getInt(in, "Enter the idea id");

                boolean res = buzz.validateCommentMedia(commentMediaId);
                System.out.println("comment media validated: " + res);
            }else if (action.equalsIgnoreCase("invalidateCommentMedia")) {
                int commentMediaId = getInt(in, "Enter the idea id");

                boolean res = buzz.invalidateCommentMedia(commentMediaId);
                System.out.println("comment media invalidated: " + res);
            }else if (action.equalsIgnoreCase("usefakedata")) {
                buzz.dropTables(); // drop existing data
                buzz.createTables(); // create new tables

                parseAllFiles();
                System.out.println("Fake Data Generated and Added to the Buzz.");
            }

        }
        // Always remember to disconnect from the database when the program
        // exits
        buzz.disconnect();
    }

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create tables");
        System.out.println("  [D] Drop tables");
        System.out.println("  [*] Get dashboard view");
        System.out.println("  [#] Get comment view");
        System.out.println("  [adduser] Add user (need first, last, email)");
        System.out.println("  [addidea] Add idea (need user_id, idea)");
        System.out.println("  [addcomment] Add comment (need user_id, idea_id)");
        System.out.println("  [addcommentmedia] Add comment media (need comment_id)");
        System.out.println("  [addideamedia] Add idea media (need idea_id)");
        System.out.println("  [addupvote] Add upvote (need user_id, idea_id)");
        System.out.println("  [adddownvote] Add downvote (need user_id, idea_id)");
        System.out.println("  [invalidateuser] Invalidate user -> make account_status false (need user_id)");
        System.out.println("  [validateuser] Validate user -> make account_status true (need user_id)");
        System.out.println("  [invalidateIdeaMedia] Invalidate idea media -> make validIdeaMedia false (need idea_id)");
        System.out.println("  [validateIdeaMedia] Validate idea media -> make validIdeaMedia true (need idea_id)");
        System.out.println("  [invalidateCommentMedia] Invalidate comment media -> make validCommentMedia false (need comment_id)");
        System.out.println("  [validateCommentMedia] Validate comment media -> make validCommentIdea true (need comment_id)");
        System.out.println("  [usefakedata] Clear all tables and use fake data for testing");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this idea)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in = A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static String prompt(BufferedReader in) {
        // The valid actions:
        String[] actions = { "T", "D", "q", "*", "#" , "?", "adduser", "addidea", "addcomment", "addcommentmedia" , "addideamedia", "addupvote",
                "adddownvote", "invalidateuser", "validateuser", "invalidateidea", "validateidea", "invalidateideamedia", "validateideamedia", "invalidatecommentmedia", "validatecommentmedia", "usefakedata" };

        // We repeat until a valid single-character option is selected
        while (true) {
            String out = "";
            for (String a : actions) {
                out += a + " ";
            }
            System.out.print("[" + out + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (Arrays.asList(actions).contains(action)) {
                return action;
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String idea
     * 
     * @param in = A BufferedReader, for reading from the keyboard
     * @param idea = A idea to display when asking for input
     * 
     * @return The string that the user provided. May be "".
     */
    static String getString(BufferedReader in, String idea) {
        String s;
        try {
            System.out.print(idea + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in = A BufferedReader, for reading from the keyboard
     * @param idea = A idea to display when asking for input
     * 
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(BufferedReader in, String idea) {
        int i = -1;
        try {
            System.out.print(idea + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    static void printAllDashboard() {

        ArrayList<Buzz.RowData> buzz_rez = buzz.getDashboard();

        // Define format string for each column with appropriate width
        String format = "| %-6d | %-10s | %-10s | %-6d | %-10s | %-9s | %-6d | %-10s | %-13s | %-7d | %-10s | %-7d | %-9d | %-9s | %-12s |\n";

        // Print header
        String separator = "-".repeat(180); // Adjust length based on your needs
        System.out.println(separator);
        System.out.printf("| %-6s | %-10s | %-10s | %-6s | %-10s | %-9s | %-6s | %-10s | %-13s | %-7s | %-10s | %-7s | %-9s | %-9s | %-12s |\n",
                "UserID", "First Name", "Last Name", "IdeaID", "Idea", "Idea Link", "CommID", "Comment", "Comment Link", "CommUID", "Comm First",
                "Upvotes", "Downvotes", "Idea File", "Comment File");
        System.out.println(separator);

        // Print each row
        for (Buzz.RowData row : buzz_rez) {
            System.out.printf(format,
                    row.user_id(),
                    truncateString(row.first(), 10),
                    truncateString(row.last(), 10),
                    row.idea_id(),
                    truncateString(row.idea(), 10),
                    truncateString(row.idea_link(), 9),
                    row.comment_id(),
                    truncateString(row.body(), 10),
                    truncateString(row.comment_link(), 9),
                    row.comment_id(),
                    truncateString(row.commenter_first(), 10),
                    row.upvotes(),
                    row.downvotes(),
                    truncateString(row.ideaf_id(), 9),
                    truncateString(row.commentf_id(), 12));
        }

    }

    /**WORK IN PROGRESS */
    static void printAllComment() {

        ArrayList<Buzz.RowData2> buzz_rez = buzz.getComment();

        // Define format string for each column with appropriate width
        String format = "| %-6d | %-20s | %-6d | %-6d | %-15s | %-11d | %-20s | %-20s | %-20s | %-26s |\n";

        // Print header
        String separator = "-".repeat(181); // Adjust length based on your needs
        System.out.println(separator);
        System.out.printf("| %-6s | %-20s | %-6s | %-6s | %-15s | %-11s | %-20s | %-20s | %-20s | %-26s |\n",
                "CommID", "Body", "IdeaID", "UserID", "CommLink", "CommMediaID", "FileName", "MimeType", "DriveID", "AccessTime");
        System.out.println(separator);

        // Print each row
        for (Buzz.RowData2 row : buzz_rez) {
            System.out.printf(format,
                    row.comment_id(),
                    truncateString(row.body(), 20),
                    row.idea_id(),
                    row.user_id(),
                    truncateString(row.comment_link(), 15),
                    row.comment_media_id(),
                    truncateString(row.file_name(), 20),
                    truncateString(row.mime_type(), 20),
                    truncateString(row.drive_file_id(), 20),
                    row.access_time());
        }

    }

    /**
     * @param str
     * @param length
     * @return String
     */
    // Helper method to truncate long strings
    private static String truncateString(String str, int length) {
        if (str == null)
            return "null";
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }

    /**
     * Parsing all files by calling all the parsing methods in order of dependencies
     */
    public static void parseAllFiles() {
        parseUsers("./fakedata/users.txt");
        parseIdeas("./fakedata/ideas.txt");
        parseComments("./fakedata/comments.txt");
        parseUpvotes("./fakedata/upvotes.txt");
        parseDownvotes("./fakedata/downvotes.txt");
    }

    /**
     * Method to parse users and add their user lists
     * 
     * @param filename = Filename of the fakedata to parse users
     */
    private static void parseUsers(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 3) {
                    String first = parts[0];
                    String last = parts[1];
                    String email = parts[2];
                    buzz.addUser(first, last, email);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to parse ideas and add their idea lists
     * 
     * @param filename = Filename of the ideas
     */
    private static void parseIdeas(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    int userId = Integer.parseInt(parts[0]);
                    String idea = parts[1];
                    buzz.addIdea(userId, idea, null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to parse comments and add to the database
     * 
     * @param filename = Filename of the comments
     */
    private static void parseComments(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 3) {
                    String body = parts[0];
                    int ideaId = Integer.parseInt(parts[1]);
                    int userId = Integer.parseInt(parts[2]);
                    buzz.addComment(body, null, ideaId, userId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to parse upvotes and add to the database
     * 
     * @param filename = Filename of the upvotes
     */
    private static void parseUpvotes(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    int userId = Integer.parseInt(parts[0]);
                    int ideaId = Integer.parseInt(parts[1]);
                    buzz.addUpvote(userId, ideaId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to parse downvotes and add to the database
     * 
     * @param filename = Filename of the downvotes
     */
    private static void parseDownvotes(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    int userId = Integer.parseInt(parts[0]);
                    int ideaId = Integer.parseInt(parts[1]);
                    buzz.addDownvote(userId, ideaId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}