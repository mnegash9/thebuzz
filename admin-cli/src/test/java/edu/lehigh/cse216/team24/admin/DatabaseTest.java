package edu.lehigh.cse216.team24.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Test class for the {@code Database} class.
 * 
 * This class contains unit tests to verify basic database operations such as:
 * <ul>
 * <li>Creating and dropping tables.</li>
 * <li>Inserting, updating, and deleting rows.</li>
 * <li>Ensuring data is stored and retrieved correctly.</li>
 * </ul>
 * 
 * Extends {@code TestCase} to use JUnit assertions for validation.
 */
public class DatabaseTest extends TestCase {

    private Buzz bz;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DatabaseTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DatabaseTest.class);
    }

    /**
     * JUnit setup class to initialize db variable
     */
    @Override
    protected void setUp() {
        bz = new Buzz();
    }

    /**
     * JUnit teardown method to disconnect at end of test.
     */
    @Override
    protected void tearDown() {
        if (bz != null) {
            bz.disconnect();
        }
    }

    /**
     * Rigourous Test :-)
     */
    public void testDatabase() {
        Database d1 = Database.getDatabase();
        assertTrue(d1 != null);
    }

    /**
     * test creating and dropping all the tables
     * 
     * This method verifies the behavior of the drop and add table methods
     */
    public void testAddDropTables() {
        // drop tables and create them again.
        bz.dropTables();
        bz.createTables();

        // should be able to insert a generic row if table exists.
        boolean addRowResult = bz.addUser("test", "user", "test.user@hotmail.com");
        assertEquals(true, addRowResult);

        bz.dropTables();
        // insert shouldn't work after table is dropped.
        addRowResult = bz.addUser("test", "user", "test.user@hotmail.com");
        assertEquals(false, addRowResult);
    }

    /**
     * Test for adding a row to table by ensuring that:
     * 1. A row can be inserted into the table and the insertion is successful.
     * 2. The inserted row can be retrieved and its data matches the expected
     * values.
     * 3. The row's fields are correctly populated and not null.
     */
    public void testAddUser() {
        // clean slate
        bz.dropTables();
        bz.createTables();

        // add generic row for testing. Check if it is added.
        boolean userAdded = bz.addUser("test", "user", "test.user@hotmail.com");
        assertEquals(true, userAdded);

        // add new idea
        boolean ideaAdded = bz.addIdea(1, "New breakroom cooler please!", "lehigh.edu");
        assertEquals(true, ideaAdded);

        // test if new row's data was added correctly.
        ArrayList<Buzz.RowData> row = bz.getDashboard();
        assertEquals("test", row.get(0).first());
        assertEquals("user", row.get(0).last());
    }

    /**
     * Test user invalidation by:
     * 1. Inserting a user into the database.
     * 2. Invalidating the inserted user and verifying that the user is invalidated.
     * 3. Confirming the invalidated user is no longer available in the table.
     */
    public void testInvalidateUser() {
        // clean slate
        bz.dropTables();
        bz.createTables();

        // add new user
        boolean userAdded = bz.addUser("test", "user", "test.user@hotmail.com");
        assertEquals(true, userAdded);

        // add new idea
        boolean ideaAdded = bz.addIdea(1, "New breakroom cooler please!", "lehigh.edu");
        assertEquals(true, ideaAdded);

        // invalidate user
        boolean userInvalidated = bz.invalidateUser(1);
        assertEquals(true, userInvalidated);

        // check if row added is now null. Should be true.
        ArrayList<Buzz.RowData> row = bz.getDashboard();
        assertEquals(true, row.isEmpty());
    }

    /**
     * test adding an idea and a comment:
     * 1. A row can be inserted and subsequently updated with new data.
     * 2. The update affects the correct row and the updated data is reflected.
     * 3. The number of likes remains unchanged after the update.
     */
    public void testAddIdeaWComment() {
        // clean slate
        bz.dropTables();
        bz.createTables();

        // add new user
        boolean userAdded = bz.addUser("test", "user", "test.user@hotmail.com");
        assertEquals(true, userAdded);

        // add new idea
        boolean ideaAdded = bz.addIdea(1, "New breakroom cooler please!", "lehigh.edu");
        assertEquals(true, ideaAdded);

        // add new comment
        boolean commentAdded = bz.addComment("that would be refreshing", "google.com", 1, 1);
        assertEquals(true, commentAdded);

        // check if user is correct
        ArrayList<Buzz.RowData> row = bz.getDashboard();
        assertEquals("test", row.get(0).first());
        assertEquals("user", row.get(0).last());

        // check idea
        assertEquals("New breakroom cooler please!", row.get(0).idea());
        // check idea link
        assertEquals("lehigh.edu", row.get(0).idea_link());
        // check comment
        assertEquals("that would be refreshing", row.get(0).body());
        // check comment link
        assertEquals("google.com", row.get(0).comment_link());
    }

    /**
     * test adding an idea and an upvote and downvote:
     * 1. A user can be added
     * 2. An idea can be added.
     * 3. An upvote can be added to an idea.
     * 4. A downvote can be added to an idea.
     */
    public void testAddUpvoteDownvote() {
        // clean slate
        bz.dropTables();
        bz.createTables();

        // add new user
        boolean userAdded = bz.addUser("test", "user", "test.user@hotmail.com");
        assertEquals(true, userAdded);

        // add new idea
        boolean ideaAdded = bz.addIdea(1, "New breakroom cooler please!", "yahoo.com");
        assertEquals(true, ideaAdded);

        // add upvote
        boolean upvoteAdded = bz.addUpvote(1, 1);
        assertEquals(true, upvoteAdded);

        // add downvote
        boolean downvoteAdded = bz.addDownvote(1, 1);
        assertEquals(true, downvoteAdded);

        // check if user is correct
        ArrayList<Buzz.RowData> row = bz.getDashboard();
        assertEquals("test", row.get(0).first());
        assertEquals("user", row.get(0).last());

        // check idea
        assertEquals("New breakroom cooler please!", row.get(0).idea());
        // check idea
        assertEquals("yahoo.com", row.get(0).idea_link());
        // check upvote
        assertEquals(1, row.get(0).upvotes());
        // check downvote
        assertEquals(1, row.get(0).downvotes());
    }

    /**
     * test adding an idea and repeated upvotes and downvotes:
     * 1. A user can be added
     * 2. An idea can be added.
     * 3. An upvote can be added to an idea.
     * 4. Another upvote added to an idea removes the old upvote
     * 5. A downvote can be added to an idea.
     * 6. Another downvote added to an idea removes the old downvote
     */
    public void testRepeatedUpvotesDownvotes() {
        // clean slate
        bz.dropTables();
        bz.createTables();

        // add new user
        boolean userAdded = bz.addUser("test", "user", "test.user@hotmail.com");
        assertEquals(true, userAdded);

        // add new idea
        boolean ideaAdded = bz.addIdea(1, "New breakroom cooler please!", "gmail.com");
        assertEquals(true, ideaAdded);

        // add upvote
        boolean upvoteAdded = bz.addUpvote(1, 1);
        assertEquals(true, upvoteAdded);

        // add repeated upvote
        upvoteAdded = bz.addUpvote(1, 1);
        assertEquals(false, upvoteAdded);

        // add downvote
        boolean downvoteAdded = bz.addDownvote(1, 1);
        assertEquals(true, downvoteAdded);

        // add repeated downvote
        downvoteAdded = bz.addDownvote(1, 1);
        assertEquals(false, downvoteAdded);

        // check if user is correct
        ArrayList<Buzz.RowData> row = bz.getDashboard();
        assertEquals("test", row.get(0).first());
        assertEquals("user", row.get(0).last());

        // check idea
        assertEquals("New breakroom cooler please!", row.get(0).idea());
        // check idea link
        assertEquals("gmail.com", row.get(0).idea_link());
        // check upvote
        assertEquals(0, row.get(0).upvotes());
        // check downvote
        assertEquals(0, row.get(0).downvotes());
    }

    public void testAddIdeaMediaAndCommentMedia() {
        bz.dropTables();
        bz.createTables();

        // add new user
        boolean userAdded = bz.addUser("test", "user", "test.user@hotmail.com");
        assertEquals(true, userAdded);

        // add new idea
        boolean ideaAdded = bz.addIdea(1, "New breakroom cooler please!", "lehigh.edu");
        assertEquals(true, ideaAdded);

        // add new idea media
        Timestamp currentIdeaTimestamp = new Timestamp(System.currentTimeMillis());
        boolean ideaMediaAdded = bz.addIdeaMedia(1, "dogpicture.file", "application/octet-stream", "5hyr3pMq6l7KV0NYh9RKkTksW7tre4wau", currentIdeaTimestamp);
        assertEquals(true, ideaMediaAdded);

        // add new comment
        boolean commentAdded = bz.addComment("that would be refreshing", "google.com", 1, 1);
        assertEquals(true, commentAdded);

        // add new comment media
        Timestamp currentCommentTimestamp = new Timestamp(System.currentTimeMillis());
        boolean commentMediaAdded = bz.addCommentMedia(1, "catpicture.file", "application/octet-stream", "ysMV0N5hWRKkTkq6trr3p7Yh9l7Ke4wau", currentCommentTimestamp);
        assertEquals(true, commentMediaAdded);

        // check if user is correct
        ArrayList<Buzz.RowData> row = bz.getDashboard();
        assertEquals("test", row.get(0).first());
        assertEquals("user", row.get(0).last());

        // check idea
        assertEquals("New breakroom cooler please!", row.get(0).idea());
        // check idea link
        assertEquals("lehigh.edu", row.get(0).idea_link());
        // check idea media
        assertEquals("5hyr3pMq6l7KV0NYh9RKkTksW7tre4wau", row.get(0).ideaf_id());
        // check comment
        assertEquals("that would be refreshing", row.get(0).body());
        // check comment link
        assertEquals("google.com", row.get(0).comment_link());
        // check comment media
        assertEquals("ysMV0N5hWRKkTkq6trr3p7Yh9l7Ke4wau", row.get(0).commentf_id());
        
    }

}
