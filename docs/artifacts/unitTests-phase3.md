# Unit Test Descriptions for Project Roles

## Backend Unit Tests
1. **OAuth Authentication Tests**:
   - Test successful user authentication via Google Identity.

2. **Voting Logic Tests**:
   - Test route for up-voting an idea.
   - Test route for down-voting an idea.
   - Test the transition of votes when a user changes from up-vote to down-vote and vice versa.
   - Test the transition from up-vote/down-vote back to neutral.

3. **Comment Management Tests**:
   - Test the addition of comments to ideas.
   - Test the editing of existing comments.

4. **User Profile Management Tests**:
   - Test updating user profile information (name, gender identity, sexual orientation, note).
   - Test retrieval of user profile information.

5. **File Upload Handling**:
   - Test that the backend accepts file uploads with the new /upload route.
   - Make sure the files are uploaded to Google Drive or Google Cloud Storage.

6. **Updated Routes**:
   - Test the updated route /ideas and confirm that the user can include a link with their post.
   - Test the updated /ideas/{id}/comments route and confirm the user can include a link with their comment.


## Web Unit Tests
1. **Login Page Tests**:
   - Test redirection to the profile page upon successful login.

2. **Voting Functionality Tests**:
   - Test the up-vote button functionality.
   - Test the down-vote button functionality.
   - Test that the voting state updates correctly in the UI.

3. **Comment Functionality Tests**:
   - Test the comment input functionality.
   - Test that comments can be edited.
   - Test that the comments section displays correctly.

4. **Profile Page Tests**:
   - Test rendering of the user profile page with correct data.
   - Test functionality for editing profile fields.

5. **File Upload Interface**:
   - Verify that users can select files using the file input and that the selected files are prepared for upload.

6. **Link Insertion Functionality**:
   - Verify that users can enter a URL as an optional field when creating a post or comment and that it is included in the submission.

7. **Displaying Uploaded Content**:
   - Verify that inserted links are clickable and open in a new tab.

## Mobile Unit Tests
1. **Login Page Tests**:
   - Test redirection to the profile page upon successful login.

2. **Voting Functionality Tests**:
   - Test the up-vote button functionality.
   - Test the down-vote button functionality.
   - Test that the voting state updates correctly in the UI.

3. **Comment Functionality Tests**:
   - Test the comment input functionality.
   - Test that comments can be edited.
   - Test that the comments section displays correctly.

4. **Profile Page Tests**:
   - Test rendering of the user profile page with correct data.
   - Test functionality for editing profile fields.

5. **Camera and Gallery Tests**:
   - Verify that the app can launch the camera, take a picture, and return the image to the app.
   - Make sure users can select existing images from the gallery.

6. **File Upload Functionality**:
   - Verify that images or files are correctly uploaded to the backend.
   - Make sure users can optionally post ideas or comments with links attached.

7. **Displaying Uploaded Content**:
   - Ensure that uploaded images are displayed correctly within ideas and comments.
   - Verify that non-image files are represented with clickable links.



## Admin Unit Tests
1. **Database Management Tests**:
   - Test the creation of new database tables as per the updated ERD.
   
2. **Idea Management Tests**:
   - Test functionality for invalidating an idea.
   - Test that invalidated ideas do not appear in user views.

3. **User Management Tests**:
   - Test functionality for invalidating a user account.
   - Test that invalidated users cannot log in.

4. **Content Management Features**:
   - Verify that the admin app can list all uploaded documents, including data such as owner and last access time.
   - Make sure an admin can delete or invalidate content, and that this action removes files from cloud storage and updates the database accordingly.
   - Confirm that the admin app correctly identifies content based on last access time.
