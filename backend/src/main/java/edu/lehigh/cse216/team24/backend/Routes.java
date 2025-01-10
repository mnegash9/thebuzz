package edu.lehigh.cse216.team24.backend;

import io.javalin.Javalin;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

/**
 * The Routes class is responsible for registering the HTTP routes (endpoints)
 * used by the Javalin application, including handling GET, POST, PUT, and
 * DELETE requests related to ideas.
 */
public class Routes {
    private final Buzz bz;
    private final Gson gson;

    /**
     * Constructor for the Routes class.
     * 
     * @param db   The Database instance that will be used to interact with the
     *             data layer (for CRUD operations).
     * @param gson A Gson instance to handle JSON serialization and deserialization.
     */
    public Routes(Buzz bz, Gson gson) {
        this.bz = bz;
        this.gson = gson;
    }

    /**
     * Registers the various routes/endpoints with the Javalin application.
     * 
     * @param app The Javalin application instance where routes will be registered.
     */
    public void registerRoutes(Javalin app) {

        // Test: curl -s http://localhost:8080/ideas -X GET
        app.get("/ideas", ctx -> {
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON

            // Query the database for all ideas
            StructuredResponse resp = new StructuredResponse("ok", null, null, null, bz.getAllIdeas());
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // Test: curl -s http://localhost:8080/ideas/{id} -X GET
        app.get("/ideas/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));

            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON

            Ideas.RowData data = bz.getIdea(idx);
            StructuredResponse resp = null;
            if (data == null) { // row not found, so return an error response
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null, null, null);
            } else { // we found it, so just return the data
                resp = new StructuredResponse("ok", null, null, null, data);
            }

            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // Test: curl -s http://localhost:8080/ideas -X POST -d
        // '{"mIdea":"newideaHere","file":"aGVsbG8gd29ybGQ="}'

        app.post("/ideas", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // Retrieve session ID from the cookie
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null) {
                resp = new StructuredResponse("error", "Session ID missing. User not authenticated.", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Retrieve user ID from email
            Integer userId = getUserFromSession(sessionId);
            if (userId == null) {
                resp = new StructuredResponse("error", "User not found in database", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }

            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server
            // Error
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);

            // add the idea to idea database, if succesful check if there is a file/link
            // sent
            int rowInsertedIdea = bz.addIdea(userId, req.mIdea());
            if (rowInsertedIdea != 0) {

                // file and link provided
                if (req.file() != null && req.link() != null) {
                    String fileId = bz.addFileToIdea(rowInsertedIdea, userId, req.file());
                    boolean addedLink = bz.addLinkToIdea(rowInsertedIdea, req.link());

                    if (fileId != "failed" && addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted idea", "successfully inserted link",
                                "successfully inserted file", null);
                    } else if (fileId == "failed" && addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted idea", "successfully inserted link",
                                "failed to add file (is it in base64?)", null);
                    } else if (fileId != "failed" && !addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted idea", "failed to add link",
                                "successfully inserted file", null);
                    } else {
                        resp = new StructuredResponse("ok", "successfully inserted idea", "failed to add link",
                                "failed to add file (is it in base64?)", null);
                    }
                }

                // file provided
                if (req.file() != null && req.link() == null) {
                    String fileId = bz.addFileToIdea(rowInsertedIdea, userId, req.file());

                    if (fileId != "failed") {
                        resp = new StructuredResponse("ok", "successfully inserted idea", "no link provided",
                                "successfully inserted file", null);
                    } else if (fileId == "failed") {
                        resp = new StructuredResponse("ok", "successfully inserted idea", "no link provided",
                                "failed to add file (is it in base64?)", null);
                    }
                }

                // link provided
                if (req.file() == null && req.link() != null) {
                    boolean addedLink = bz.addLinkToIdea(rowInsertedIdea, req.link());

                    if (addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted idea", "successfully inserted link",
                                "no file provided", null);
                    } else if (!addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted idea", "failed to add link",
                                "no file provided", null);
                    }
                }

                // neither provided
                if (req.file() == null && req.link() == null) {
                    resp = new StructuredResponse("ok", "successfully inserted idea", "no link provided",
                            "no file provided", null);

                }

            } else {
                resp = new StructuredResponse("error", "error inserting idea (message null?)",
                        "link requires non null idea",
                        "file requires non null idea", null);
            }

            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // Test: curl -s http://localhost:8080/ideas/{id}/upvote -X PUT
        app.put("/ideas/{id}/upvote", ctx -> {
            // Parse the ID from the path parameter
            int idx = Integer.parseInt(ctx.pathParam("id"));
            // Always return status 200 even if there's an error, but include the error in
            // the response JSON
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp;

            // Retrieve session ID from the cookie
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null) {
                resp = new StructuredResponse("error", "Session ID missing. User not authenticated.", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Retrieve user ID from email
            Integer userId = getUserFromSession(sessionId);
            if (userId == null) {
                resp = new StructuredResponse("error", "User not found in database", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Increment the upvotes in the database, using the provided id
            boolean result = bz.addUpvote(userId, idx);
            if (!result) {
                // If no rows were updated, return an error response
                resp = new StructuredResponse("error", "unable to increment upvotes for row " + idx, null, null, null);
            } else {
                // Return a success response if likes were successfully incremented
                resp = new StructuredResponse("ok", "incremented upvotes for row " + idx, null, null, null);
            }

            // Return the response as a JSON object
            ctx.result(gson.toJson(resp));
        });

        // Test: curl -s http://localhost:8080/ideas/{id}/downvote -X PUT
        app.put("/ideas/{id}/downvote", ctx -> {
            // Parse the ID from the path parameter
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // Always return status 200 even if there's an error, but include the error in
            // the response JSON
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp;

            // Retrieve session ID from the cookie
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null) {
                resp = new StructuredResponse("error", "Session ID missing. User not authenticated.", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Retrieve user ID from email
            Integer userId = getUserFromSession(sessionId);
            if (userId == null) {
                resp = new StructuredResponse("error", "User not found in database", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Increment the downvotes in the database, using the provided id
            boolean result = bz.addDownvote(userId, idx);
            if (!result) {
                // If no rows were updated, return an error response
                resp = new StructuredResponse("error", "unable to increment likes for row " + idx, null, null, null);
            } else {
                // Return a success response if likes were successfully incremented
                resp = new StructuredResponse("ok", "incremented likes for row " + idx, null, null, null);
            }

            // Return the response as a JSON object
            ctx.result(gson.toJson(resp));
        });

        // NEW AS OF PHASE 2//

        // Test: curl -s http://localhost:8080//ideas/{id}/comments -X POST -d “{
        // "content": string }"
        app.post("/ideas/{id}/comments", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // Retrieve session ID from the cookie
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null) {
                resp = new StructuredResponse("error", "Session ID missing. User not authenticated.", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Retrieve user ID from email
            Integer userId = getUserFromSession(sessionId);
            if (userId == null) {
                resp = new StructuredResponse("error", "User not found in database", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }

            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server
            // Error
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);

            // NB: we get the value of the inserted comments row here
            int rowInserted = bz.addComment(req.mIdea(), idx, userId);
            if (rowInserted != 0) {

                // file and link provided
                if (req.file() != null && req.link() != null) {
                    String fileId = bz.addFileToComment(rowInserted, userId, req.file());
                    boolean addedLink = bz.addLinkToComment(rowInserted, req.link());

                    if (fileId != "failed" && addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted comment",
                                "successfully inserted link",
                                "successfully inserted file", null);
                    } else if (fileId == "failed" && addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted comment",
                                "successfully inserted link",
                                "failed to add file (is it in base64?)", null);
                    } else if (fileId != "failed" && !addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted comment", "failed to add link",
                                "successfully inserted file", null);
                    } else {
                        resp = new StructuredResponse("ok", "successfully inserted comment", "failed to add link",
                                "failed to add file (is it in base64?)", null);
                    }
                }

                // file provided
                if (req.file() != null && req.link() == null) {
                    String fileId = bz.addFileToComment(rowInserted, userId, req.file());

                    if (fileId != "failed") {
                        resp = new StructuredResponse("ok", "successfully inserted comment", "no link provided",
                                "successfully inserted file", null);
                    } else if (fileId == "failed") {
                        resp = new StructuredResponse("ok", "successfully inserted comment", "no link provided",
                                "failed to add file (is it in base64?)", null);
                    }
                }

                // link provided
                if (req.file() == null && req.link() != null) {
                    boolean addedLink = bz.addLinkToComment(rowInserted, req.link());

                    if (addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted comment",
                                "successfully inserted link",
                                "no file provided", null);
                    } else if (!addedLink) {
                        resp = new StructuredResponse("ok", "successfully inserted comment", "failed to add link",
                                "no file provided", null);
                    }
                }

                // neither provided
                if (req.file() == null && req.link() == null) {
                    resp = new StructuredResponse("ok", "successfully inserted comment", "no link provided",
                            "no file provided", null);

                }

            }

            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // Test: curl -s http://localhost:8080/ideas/{id}/comments/{comment_id} -X PUT
        app.put("/comments/{comment_id}", ctx -> {
            // If we can't get an ID or can't parse the JSON, javalin sends a status 500
            int idx = Integer.parseInt(ctx.pathParam("comment_id"));

            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // Retrieve session ID from the cookie
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null) {
                resp = new StructuredResponse("error", "Session ID missing. User not authenticated.", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Retrieve user ID from email
            Integer userId = getUserFromSession(sessionId);
            if (userId == null) {
                resp = new StructuredResponse("error", "User not found in database", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server
            // Error
            SimpleRequestComment req = gson.fromJson(ctx.body(), SimpleRequestComment.class);

            boolean success = bz.updateComment(idx, req.body);
            if (!success) {
                resp = new StructuredResponse("error", "unable to update row " + idx, null, null, null);
            } else {
                resp = new StructuredResponse("ok", null, null, null, success);
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // Test: curl -s http://localhost:8080/dashboard -X GET
        app.get("/dashboard", ctx -> {
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON

            // Query the database for all ideas
            StructuredResponse resp = new StructuredResponse("ok", null, null, null, bz.getDashboard());
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // Test: curl -s http://localhost:8080/ideas -X GET
        app.get("/profile/{user_id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("user_id"));

            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // Retrieve session ID from the cookie
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null) {
                resp = new StructuredResponse("error", "Session ID missing. User not authenticated.", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Retrieve user ID from email
            Integer userId = getUserFromSession(sessionId);
            if (userId == null) {
                resp = new StructuredResponse("error", "User not found in database", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }

            // checks if userId matches route, if so it displays all info, otherwise only
            // shows public info.
            if (userId == idx) {
                Users.RowData data = bz.getUser(userId);

                if (data == null) { // row not found, so return an error response
                    resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null, null, null);
                } else { // we found it, so just return the data
                    resp = new StructuredResponse("ok", null, null, null, data);
                }
            } else {
                Users.OtherUser data = bz.getOtherUser(idx);
                if (data == null) { // row not found, so return an error response
                    resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null, null, null);
                } else { // we found it, so just return the data
                    resp = new StructuredResponse("ok", null, null, null, data);
                }
            }

            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        app.put("/profile/{id}", ctx -> {
            // Parse the ID from the path parameter
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // Always return status 200 even if there's an error, but include the error in
            // the response JSON
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp;
            SimpleRequestUser req = gson.fromJson(ctx.body(), SimpleRequestUser.class);

            // Retrieve session ID from the cookie
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null) {
                resp = new StructuredResponse("error", "Session ID missing. User not authenticated.", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Retrieve user ID from email
            Integer userId = getUserFromSession(sessionId);
            if (userId == null) {
                resp = new StructuredResponse("error", "User not found in database", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }

            if (userId == idx) {
                // Call profileUpdate to update a profile.
                boolean success = bz.updateProfile(req.userID, req.genderIdentity, req.sexualOrientation, req.note);
                if (!success) {
                    // If no rows were updated, return an error response
                    resp = new StructuredResponse("error", "unable to edit profile for row " + idx, null, null, null);
                } else {
                    // Return a success response if likes were successfully incremented
                    resp = new StructuredResponse("ok", null, null, null, "edited profile for row " + idx);
                }
            } else {
                resp = new StructuredResponse("error", "Cannot edit other people's profiles", null, null, null);
                ctx.result(gson.toJson(resp));
                return;
            }
            // Return the response as a JSON object
            ctx.result(gson.toJson(resp));
        });

        app.get("/comments/{idea_id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("idea_id"));

            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON

            ArrayList<Buzz.CommentViewData> data = bz.getCommentViewMobile(idx);
            StructuredResponse resp = null;
            if (data == null) { // row not found, so return an error response
                resp = new StructuredResponse("error", "Data with idea id " + idx + " not found", null, null, null);
            } else { // we found it, so just return the data
                resp = new StructuredResponse("ok", null, null, null, data);
            }

            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

    }

    /**
     * Function to search the app's hashmap and get a userID from the sessionId
     * 
     * @param sessionId sessionId given to user at log in as cookie
     * @return Integer user ID of the user in the app's hashmap
     */
    public Integer getUserFromSession(String sessionId) {
        for (Map.Entry<String, Integer> entry : App.getHashTable().entrySet()) {
            if (entry.getValue().toString().equals(sessionId)) {
                String email = entry.getKey(); // set email to variable
                return bz.getUserIdByEmail(email);
            }
        }
        return null; // No match found
    }
}
