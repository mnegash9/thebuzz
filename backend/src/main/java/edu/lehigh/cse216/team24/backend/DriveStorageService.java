package edu.lehigh.cse216.team24.backend;

// Drive Storage Imports
import com.google.auth.oauth2.GoogleCredentials;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Class to handle all storage request written to Google Drive
 */
public class DriveStorageService {
    private static final String APPLICATION_NAME = "the-buzz";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);

    private final Drive driveService;
    private final String appFolderId;

    public DriveStorageService(String appFolderId)
            throws IOException, GeneralSecurityException {
        this.appFolderId = appFolderId;
        this.driveService = createDriveService();
    }

    /** Memcached Client for Phase 3 */
    private static final MemcachedClient memcache = memcache();

    /**
     * Function to read environment variables and create a memcachedclient that can
     * be used for caching
     * 
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
     * Creates a drive service using a given environment variable
     * 
     * @return Drive object to be used to handle interactions with Google Drive
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private Drive createDriveService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Load credentials from environment variable
        String credentialString_base64 = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_ENCODED");
        // Decode base64 string
        String credentialString = new String(Base64.getDecoder().decode(credentialString_base64));

        InputStream credentialStream = new ByteArrayInputStream(credentialString.getBytes(StandardCharsets.UTF_8));

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialStream)
                .createScoped(SCOPES)
                .createDelegated("clifford@team-dog.iam.gserviceaccount.com");

        // Use HttpCredentialsAdapter to wrap the GoogleCredentials
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Implementation of file upload that uses drive object to upload file
     * 
     * @param localFile File object to add to the drive
     * @param mimeType  mime type of the file to add
     * @param filename  filename to use for hte uploaded file in drive
     * @return String file_id from google which is a unique string if the file was
     *         successfully created
     * @throws IOException
     */
    public String uploadFile(java.io.File localFile, String mimeType, String filename) throws IOException {
        // Create file metadata
        File fileMetadata = new File();
        fileMetadata.setName(filename);
        fileMetadata.setParents(Collections.singletonList(appFolderId));

        // Create file content
        FileContent mediaContent = new FileContent(mimeType, localFile);

        // Upload file
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        // Convert file to byte array for caching
        byte[] fileBytes = Files.readAllBytes(localFile.toPath());

        // Add byte array to cache
        try {
            memcache.add(file.getId(), 0, fileBytes);
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

        return file.getId();
    }

    /**
     * Function that uses memcache client to search cache for file first and if it
     * misses, downloads file from google drive
     * 
     * @param fileId          unique file id provided by Google
     * @param destinationFile where to write the contents of the downloaded file to
     * @throws IOException
     */
    public void downloadFile(String fileId, java.io.File destinationFile) throws IOException {
        OutputStream outputStream = new FileOutputStream(destinationFile);
        try {
            // Try to get file from cache first
            byte[] cachedData = null;
            try {
                cachedData = (byte[]) memcache.get(fileId);
            } catch (TimeoutException te) {
                System.err.println("Timeout during cache get: " + te.getMessage());
            } catch (InterruptedException ie) {
                System.err.println("Interrupt during cache get: " + ie.getMessage());
            } catch (MemcachedException me) {
                System.err.println("Memcached error during get: " + me.getMessage());
            }

            if (cachedData != null) {
                // File found in cache, write it to destination
                outputStream.write(cachedData);
                outputStream.flush();
                System.out.println("File retrieved from cache successfully");
                return;
            }

            // If not in cache, download from Google Drive
            ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();
            driveService.files().get(fileId).executeMediaAndDownloadTo(tempOutputStream);

            // Get the downloaded data
            byte[] downloadedData = tempOutputStream.toByteArray();

            // Write to destination file
            outputStream.write(downloadedData);
            outputStream.flush();

            // Store in cache for future use
            try {
                memcache.add(fileId, 0, downloadedData);
                System.out.println("File downloaded from Google Drive and cached successfully");
            } catch (TimeoutException te) {
                System.err.println("Timeout during cache add: " + te.getMessage());
            } catch (InterruptedException ie) {
                System.err.println("Interrupt during cache add: " + ie.getMessage());
            } catch (MemcachedException me) {
                System.err.println("Memcached error during add: " + me.getMessage());
            }

        } finally {
            outputStream.close();
        }
    }

    /**
     * Deletes a file from google drive
     * 
     * @param fileId unique file id string provided by Google
     * @throws IOException
     */
    public void deleteFile(String fileId) throws IOException {
        // Delete from cache
        try {
            memcache.delete(fileId);
            System.out.println("File deleted from cache");
        } catch (TimeoutException te) {
            System.err.println("Timeout during cache delete: " + te.getMessage());
        } catch (InterruptedException ie) {
            System.err.println("Interrupt during cache delete: " + ie.getMessage());
        } catch (MemcachedException me) {
            System.err.println("Memcached error during delete: " + me.getMessage());
        }

        // Delete from drive
        driveService.files().delete(fileId).execute();
    }

    /**
     * Function that can be used to listFiles in the drive using queries from google
     * 
     * @param query String request of what to search ( can be blank to list all
     *              contents in app's file)
     * @return List<File>
     * @throws IOException
     */
    public List<File> listFiles(String query) throws IOException {
        String fullQuery;
        if (query == null || query.isBlank()) {
            fullQuery = String.format("'%s' in parents", appFolderId);
        } else {
            fullQuery = String.format("'%s' in parents and %s", appFolderId, query);
        }

        FileList result = driveService.files().list()
                .setQ(fullQuery)
                .setSpaces("drive")
                .setFields("files(id, name, mimeType, size, createdTime)")
                .execute();

        return result.getFiles();
    }

    /**
     * Function that can be used to create a folder in google drive
     * 
     * @param folderName Folder name to be used for the drive
     * @return String
     * @throws IOException
     */
    public String createFolder(String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setParents(Collections.singletonList(appFolderId));

        File file = driveService.files().create(fileMetadata)
                .setFields("id")
                .execute();

        return file.getId();
    }

    /**
     * Function that can be used to see if a file exists in Google Drive
     * 
     * @param filename Filename to search for in drive
     * @return boolean
     * @throws IOException
     */
    public boolean fileExists(String filename) throws IOException {
        String query = String.format("name = '%s'", filename);
        FileList result = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id)")
                .execute();

        return !result.getFiles().isEmpty();
    }
}