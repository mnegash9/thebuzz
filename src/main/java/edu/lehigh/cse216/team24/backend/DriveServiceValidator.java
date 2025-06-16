package edu.lehigh.cse216.team24.backend;

import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class DriveServiceValidator {
    private final DriveStorageService driveService;

    public DriveServiceValidator(String credentialsPath, String folderId)
            throws IOException, GeneralSecurityException {
        this.driveService = new DriveStorageService(folderId);
    }

    /**
     * Function that validates setup by listing files, creating a test folder,
     * uploading a test file
     * 
     * @return boolean true if successful and passing test, false if not
     */
    public boolean validateSetup() {
        try {
            // Test 1: Try to list files
            List<File> files = driveService.listFiles("");
            System.out.println("✓ Successfully connected to Drive API");

            // Test 2: Try to create a test folder
            String testFolderId = driveService.createFolder("test_folder_" + System.currentTimeMillis());
            System.out.println("✓ Successfully created test folder");

            // Test 3: Try to upload a test file
            java.io.File tempFile = java.io.File.createTempFile("test", ".txt");
            String fileId = driveService.uploadFile(tempFile, "text/plain", "test.txt");
            System.out.println("✓ Successfully uploaded test file");

            // Clean up
            driveService.deleteFile(fileId);
            driveService.deleteFile(testFolderId);
            tempFile.delete();

            return true;
        } catch (IOException e) {
            System.err.println("✗ Validation failed: " + e.getMessage());
            System.err.println("Common issues:");
            System.err.println("1. Service account credentials file is invalid or not accessible");
            System.err.println("2. Folder ID is incorrect");
            System.err.println("3. Folder is not shared with service account");
            System.err.println("4. Drive API is not enabled in Google Cloud Console");
            return false;
        }
    }

    public void printServiceAccountEmail() {
        try {
            // Get the first file to see what account we're using
            List<File> files = driveService.listFiles("");
            if (!files.isEmpty()) {
                File file = files.get(0);
                System.out.println("Service account has access to files owned by: " + file.getOwners());
            }
        } catch (IOException e) {
            System.err.println("Could not determine service account email");
        }
    }
}