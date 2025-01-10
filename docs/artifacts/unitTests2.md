
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

## Web/ Mobile Unit Tests
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

## Admin Unit Tests
1. **Database Management Tests**:
   - Test the creation of new database tables as per the updated ERD.
   
2. **Idea Management Tests**:
   - Test functionality for invalidating an idea.
   - Test that invalidated ideas do not appear in user views.

3. **User Management Tests**:
   - Test functionality for invalidating a user account.
   - Test that invalidated users cannot log in.


