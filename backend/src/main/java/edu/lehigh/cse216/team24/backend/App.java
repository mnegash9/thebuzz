package edu.lehigh.cse216.team24.backend;

// Javalin package for creating HTTP GET, PUT, POST, etc routes
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.Cookie;

// Import Google's JSON library
import com.google.gson.*;
// Import Google OAuth 2.0 packages.
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

// Caching Service - Memcachier
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*; //can change after we figure out which get used.
import java.util.concurrent.TimeoutException;

/**
 * The main class for the Javalin web server application.
 * This application serves as a backend service for managing ideas,
 * connecting to a database, and providing RESTful API routes.
 */
public class App {

    /* Instance of the Buzz app */
    static Buzz bz = new Buzz();

    // OAUTH IMPLEMENTATION
    // first some final variables to reduce refactoring later

    /** The default port our webserver uses. We set it to Javalin's default, 8080 */
    public static final int DEFAULT_PORT_WEBSERVER = 8080;

    /** Google Oauth Client ID from environement variable */
    private static final String CLIENT_ID_WEB = System.getenv("CLIENT_ID_WEB");
    private static final String CLIENT_ID_MOBILE = "209952242135-g8lkq6vpma3uit0pbekbu7qefjr6fpsa.apps.googleusercontent.com";

    /** User Sessions. string will be email and int is session ID number */
    private static Hashtable<String, Integer> userSessionHashtable = new Hashtable<>();

    /** session ID pool List. See method below for details. */
    private static final List<Integer> sessionIdPool = initializeSessionPool();

    /** Google ID token verifier instance */
    private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(), new GsonFactory())
            .setAudience(Arrays.asList(CLIENT_ID_WEB, CLIENT_ID_MOBILE))
            .build();

    /** Memcached Client for Phase 3 */
    private static final MemcachedClient memcache = memcache();
    // Global key for the userSession table that is cached
    private static final String memcache_key = "userSessionHashTable";

    /**
     * @return MemcachedClient
     */
    // initializer method for the cache
    static MemcachedClient memcache() {
        String memCacheURI = System.getenv("MEMCACHIER_SERVERS");
        String memCacheUsername = System.getenv("MEMCACHIER_USERNAME");
        String memCachePassword = System.getenv("MEMCACHIER_PASSWORD");

        if (memCacheURI == null | memCacheUsername == null | memCachePassword == null) {
            System.err.println("ERROR CACHING VARIABLES NOT SET");
            System.err.println(
                    "Environment variables: MEMCACHIER_SERVERS, MEMCACHIER_USERNAME, or MEMCACHIER_PASSWORD not set.");
            System.err.println("Try setting with: `export MEMCACHIER_SERVERS={server}` for example.");
            System.exit(1);
        }

        List<InetSocketAddress> servers = AddrUtil.getAddresses(memCacheURI.replace(",", " "));
        AuthInfo authInfo = AuthInfo.plain(memCacheUsername,
                memCachePassword);

        if (servers == null || authInfo == null) {
            System.err.println("Missing required MemCachier environment variables");
            return null;
        }
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);

        // Configure SASL auth for each server
        for (InetSocketAddress server : servers) {
            builder.addAuthInfo(server, authInfo);
        }

        // Use binary protocol
        builder.setCommandFactory(new BinaryCommandFactory());
        // Connection timeout in milliseconds (default: )
        builder.setConnectTimeout(1000);
        MemcachedClient mc = null;

        try {
            mc = builder.build();
        } catch (IOException ioe) {
            System.err.println("Couldn't create a connection to MemCachier: " +
                    ioe.getMessage());
        }
        return mc;
    }

    /**
     * Method that sets the hash table with the key userSessionHashTable
     */
    static void set_cached_hashtable() {
        try {
            memcache.set(memcache_key, 0, userSessionHashtable);
        } catch (TimeoutException te) {
            System.err.println("Timeout during set or get: " +
                    te.getMessage());
        } catch (InterruptedException ie) {
            System.err.println("Interrupt during set or get: " +
                    ie.getMessage());
        } catch (MemcachedException me) {
            System.err.println("Memcached error during get or set: " +
                    me.getMessage());
        }
    }

    /**
     * Method that adds the hash table with the key userSessionHashTable
     * 
     * @param email     email of the user to add
     * @param sessionID sessionId of the user to add
     */
    static void add_cached_hashtable(String email, int sessionID) {
        try {
            if (memcache.get(memcache_key) == null)
                memcache.add(memcache_key, 0, userSessionHashtable);
            userSessionHashtable.put(email, sessionID);
            set_cached_hashtable();

        } catch (TimeoutException te) {
            System.err.println("Timeout during set or get: " +
                    te.getMessage());
        } catch (InterruptedException ie) {
            System.err.println("Interrupt during set or get: " +
                    ie.getMessage());
        } catch (MemcachedException me) {
            System.err.println("Memcached error during get or set: " +
                    me.getMessage());
        }
    }

    /**
     * Method that removes the hash table with the key
     * 
     * @param email email of the user to remove
     * @return int of the session id that was removed
     */
    static int removed_cached_hashtable(String email) {
        int sessionID = 0;
        try {
            userSessionHashtable = memcache.get(memcache_key);
            sessionID = userSessionHashtable.remove(email);
            set_cached_hashtable();
        } catch (TimeoutException te) {
            System.err.println("Timeout during set or get: " +
                    te.getMessage());
        } catch (InterruptedException ie) {
            System.err.println("Interrupt during set or get: " +
                    ie.getMessage());
        } catch (MemcachedException me) {
            System.err.println("Memcached error during get or set: " +
                    me.getMessage());
        }
        return sessionID;
    }

    /**
     * Entry point of the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        main_uses_database(args);
    }

    /**
     * Initialize session pool with shuffled session IDs 1-25.
     * 
     * @return list of integers representing the user session numbers
     */
    private static List<Integer> initializeSessionPool() {
        // random session ids
        List<Integer> numbers = new ArrayList<>();
        int maxSessions = 10000; // can change later. Start fairly small.
        for (int i = 1; i <= maxSessions; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    private static int createSession(List<Integer> nums) {
        if (nums.isEmpty()) {
            throw new NoSuchElementException("No more session IDs available");
        }
        return nums.remove(0);
    }

    /**
     * Safely gets integer value from named env var if it exists, otherwise returns
     * default
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        if (envar == null || envar.length() == 0 || System.getenv(envar.trim()) == null)
            return defaultVal;
        try (Scanner sc = new Scanner(System.getenv(envar.trim()))) {
            if (sc.hasNextInt())
                return sc.nextInt();
            else
                System.err.printf("ERROR: Could not read %s from environment, using default of %d%n", envar,
                        defaultVal);
        }
        return defaultVal;
    }

    /**
     * Initializes and starts the Javalin web server, connecting to a database.
     * Reads configuration values from the environment to set up database
     * connections.
     * Supports various RESTful routes for managing ideas:
     * <ul>
     * <li>GET /ideas - Retrieve all ideas</li>
     * <li>POST /ideas - Create a new idea</li>
     * <li>PUT /ideas{id}/likes - Like an idea</li>
     * <li>GET, PUT, DELETE /ideas/{id} - Manage specific idea by ID</li>
     * </ul>
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main_uses_database(String[] args) {

        // our javalin app on which most operations must be performed
        Javalin app = Javalin
                .create(config -> {
                    config.requestLogger.http((ctx, ms) -> {
                        System.out.printf("%s%n", "=".repeat(42));
                        System.out.printf("%s\t%s\t%s%nfull url: %s%n", ctx.scheme(), ctx.method().name(), ctx.path(),
                                ctx.fullUrl());
                    });
                    config.staticFiles.add(staticFiles -> {
                        staticFiles.hostedPath = "/"; // change to host files on a subpath, like '/assets'
                        String static_location_override = System.getenv("STATIC_LOCATION");
                        if (static_location_override == null) { // serve from jar; files located in
                                                                // src/main/resources/public
                            staticFiles.directory = "/public"; // the directory where your files are located
                            staticFiles.location = Location.CLASSPATH; // Location.CLASSPATH (jar)
                        } else { // serve from filesystem
                            System.out.println(
                                    "Overriding location of static file serving using STATIC_LOCATION env var: "
                                            + static_location_override);
                            staticFiles.directory = static_location_override; // the directory where your files are
                                                                              // located
                            staticFiles.location = Location.EXTERNAL; // Location.EXTERNAL (file system)
                        }
                        staticFiles.precompress = false; // if the files should be pre-compressed and cached in memory
                                                         // (optimization)

                    });

                });
        if ("True".equalsIgnoreCase(System.getenv("CORS_ENABLED"))) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(app, acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }

        // gson provides us a way to turn JSON into objects, and objects into JSON.
        //
        // it must be final, so that it can be accessed from our lambdas
        final Gson gson = new Gson();

        Routes routes = new Routes(bz, gson);
        routes.registerRoutes(app);
        // Sets the port on which to listen for requests from the environment (uses
        // default if not found)

        // Register OAuth routes (new for phase 2) everything else same in this method.
        registerOAuthRoutes(app, gson);

        app.start(getIntFromEnv("PORT", DEFAULT_PORT_WEBSERVER));

    }

    /**
     * Oauth related routes.
     * 
     * @param app
     * @param gson
     */
    private static void registerOAuthRoutes(Javalin app, Gson gson) {
        app.post("/login", ctx -> {
            ctx.contentType("application/json");
            StructuredResponse resp;

            try {
                // Parse request body
                SimpleRequestLogin req = gson.fromJson(ctx.body(), SimpleRequestLogin.class);
                String idTokenString = req.jwtToken;

                if (idTokenString == null || idTokenString.isEmpty()) {
                    resp = new StructuredResponse("error", "Missing ID token", null, null, null);
                    ctx.result(gson.toJson(resp));
                    return;
                }

                GoogleIdToken idToken = verifier.verify(idTokenString);
                if (idToken != null) {
                    Payload payload = idToken.getPayload();

                    String email = payload.getEmail();
                    boolean emailVerified = payload.getEmailVerified();
                    String domain = payload.getHostedDomain();
                    String familyName = (String) payload.get("family_name");
                    String givenName = (String) payload.get("given_name");

                    /*
                     * don't care that it is from lehigh
                     * if (!(domain.equals("lehigh.edu"))) {
                     * resp = new StructuredResponse("ok", "Non-Lehigh user", null, null, null);
                     * ctx.result(gson.toJson(resp));
                     * return;
                     * }
                     */

                    if (!emailVerified) {
                        resp = new StructuredResponse("error", "Email not verified", null, null, null);
                        ctx.result(gson.toJson(resp));
                        return;
                    }
                    // Fetch user_id from the database

                    Integer userId = bz.getUserIdByEmail(email);
                    if (userId == null) {
                        bz.addUser(givenName, familyName, email);
                        userId = bz.getUserIdByEmail(email);
                    }

                    // Create session
                    int sessionKey = createSession(sessionIdPool);
                    add_cached_hashtable(email, sessionKey);

                    // Create session cookie
                    Cookie sessionCookie = new Cookie("sessionId", String.valueOf(sessionKey));
                    sessionCookie.setPath("/");
                    sessionCookie.setHttpOnly(true); // Prevents JavaScript access
                    sessionCookie.setSecure(true); // Only sent over HTTPS

                    // Set the cookie in the response
                    ctx.cookie(sessionCookie);

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("user_id", userId);
                    userData.put("sessionId", sessionKey);
                    userData.put("email", email);
                    userData.put("family_name", familyName);
                    userData.put("given_name", givenName);
                    userData.put("pictureUrl", (String) payload.get("picture"));

                    resp = new StructuredResponse("ok", "Login successful", null, null, userData);

                } else {
                    resp = new StructuredResponse("error", "Invalid ID token", null, null, idTokenString);
                }

            } catch (Exception e) {
                e.printStackTrace();
                resp = new StructuredResponse("error", "Error processing request: " + e.getMessage(), null, null, null);
            }

            ctx.result(gson.toJson(resp));
        });

        app.post("/logout", ctx -> {
            ctx.contentType("application/json");
            StructuredResponse resp;

            String userEmail = ctx.header("X-User-Email");
            if (userEmail != null) {
                Integer sessionId = removed_cached_hashtable(userEmail);
                if (sessionId != 0) { // NB: zero is invalid sessionID
                    sessionIdPool.add(sessionId);

                    // Clear the session cookie
                    Cookie clearCookie = new Cookie("sessionId", "");
                    clearCookie.setMaxAge(0); // Immediately expires the cookie
                    clearCookie.setPath("/");
                    ctx.cookie(clearCookie);

                    resp = new StructuredResponse("ok", "Logged out successfully", null, null, null);
                } else {
                    resp = new StructuredResponse("error", "No active session found", null, null, null);
                }
            } else {
                resp = new StructuredResponse("error", "No user email provided", null, null, null);
            }

            ctx.result(gson.toJson(resp));
        });
        // Test: curl -s http://localhost:8080/gdrive -X GET
        app.get("/gdrive", ctx -> {
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON

            // Test your setup
            String out;
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            System.out.println("Current absolute path is: " + s);
            DriveServiceValidator validator = new DriveServiceValidator(
                    "./team-dog.json",
                    "1Ckg-oqaC2eCegtzYrbrOO216eGMqtDeW");

            if (validator.validateSetup()) {
                out = "Service account setup is valid!";
                validator.printServiceAccountEmail();
            } else {
                out = "Service account setup failed validation.";
            }

            StructuredResponse resp = new StructuredResponse("ok", null, null, null, out);
            ctx.result(gson.toJson(resp));
        });
    }

    /**
     * Enables CORS (Cross-Origin Resource Sharing) for the Javalin application.
     * Configures response headers to allow cross-origin requests.
     *
     * @param app     The Javalin application to configure.
     * @param origin  The allowed origin for CORS requests.
     * @param methods The allowed HTTP methods for CORS requests.
     * @param headers The allowed headers for CORS requests.
     */
    private static void enableCORS(Javalin app, String origin, String methods, String headers) {
        System.out.println("!!! CAUTION: ~~~ ENABLING CORS ~~~ !!!");
        app.options("/*", ctx -> {
            String accessControlRequestHeaders = ctx.req().getHeader("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                ctx.res().setHeader("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = ctx.req().getHeader("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                ctx.res().setHeader("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
        });

        // 'before' is a decorator, which will run before any get/post/put/delete.
        // In our case, it will put three extra CORS headers into the response
        app.before(handler -> {
            handler.header("Access-Control-Allow-Origin", origin);
            handler.header("Access-Control-Request-Method", methods);
            handler.header("Access-Control-Allow-Headers", headers);
        });
    }

    /**
     * Simple request class for login
     */
    private static class SimpleRequestLogin {
        String jwtToken;
    }

    /**
     * Method to get the session hash table from the cache.
     */
    public static Hashtable<String, Integer> getHashTable() {
        try {
            userSessionHashtable = memcache.get(memcache_key);
        } catch (TimeoutException te) {
            System.err.println("Timeout during get: " +
                    te.getMessage());
        } catch (InterruptedException ie) {
            System.err.println("Interrupt during get: " +
                    ie.getMessage());
        } catch (MemcachedException me) {
            System.err.println("Memcached error during get: " +
                    me.getMessage());
        }
        return userSessionHashtable;
    }

}
