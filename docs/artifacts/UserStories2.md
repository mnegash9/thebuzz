# User Stories and Tests

## **Authenticated User Stories**

### 1. As an authenticated user, I want to be able to log in using Google log-in so I can access my account.

- **Manual Test**:
  - **Test case**: Open the app and view and click on the Google log-in action button and ensure it goes through the process.
  - **Expected outcome**: After the button is clicked it will go through the Google log in process, and I am in my account, and the page shows I am in a logged in profile.

### 2. As an authenticated user, I want to be able to up-vote or down-vote any post, so that I can show my opinion in regards to the post.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user and up-vote one post and down-vote another post, and check that both are counted.
  - **Expected outcome**: Both up-vote and down-vote button registers a response, and either the up-vote or down-vote increases, it is reflected on the page.

### 3. As an authenticated user, I want to be able to comment on a post, so that I can provide written feedback to a user’s post.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user and press the comment button, and write a comment and post the comment.
  - **Expected outcome**: The comment button registers a response, allows for written text to be applied to the comment, allows it to be posted on the post, it is then subsequently reflected on the page under a comment section.

### 4. As an authenticated user, I want to be able to edit a comment I made previously, so that I can adjust and change parts or the whole of comment to better represent my feedback.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user and view a post that I previously provided a comment for, press on a edit button on the comment, and then rewrite the comment and post the comment again.
  - **Expected outcome**: The edit button on the comment registers a response. It then allows for the user to change their text and comment, allows it to be posted again and then reflected on the page under the comment section.

### 5. As an authenticated user, I want to be able to create a user profile page and put my information in it, so that others that view my profile know more about me.

- **Manual Test**:
  - **Test case**: Log in as new anonymous user, view a page for creating a profile page, edit a section for Username, User’s email, User’s sexual orientation, User’s gender orientation, and a note from the user, i am able to write in any of this section when first created, and re-edit my user’s note field over and over again.
  - **Expected outcome**: After logging in with google, the page pops up for creating a profile page, then there's a button for each field to provide a description, that registers a response. The button to finish and submit the profile registers a response and posts. Then on my profile page If I am the owner the edit button for any description field registers a response and I am able to edit the description of that field and re-post.

### 6. As an authenticated user, I want to be able to click on the name of any user who posts a comment or a post, so that I can see the information regarding that user who posted.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user, and press the name of someone who posted a comment or post, and then view their profile page.
  - **Expected outcome**: The Name of someone who posts registers a response once clicked, then a page pops up for the profile page of that user who posted.

### 7. As an authenticated user, I want to be able to view a navigation bar on the top of the page, and then have a button to click on my profile page, so that I can have easy access to view my profile page whenever I need to.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user view the navigation bar, a script can be written to see if it is visible. Then I can click on my profile page button and open the profile page screen.
  - **Expected outcome**: The navigation bar is on the top and I am able to view it. I can also click on my profile page button and it registers a response, allowing for the profile page screen to show up.

### 8. As an authenticated user, I want any network errors to be reported to the user, so the user knows that due to network instability the app can not load.

- **Manual Test**:
  - **Test case**: Test by making a mock connection that is not working.
  - **Expected outcome**: The app should not be able to connect, and therefore a message will pop up that shows there is a network error.
---

## **Admin User Stories**

### 1. As an admin user, I want to be able to create a new comment and profile page, up-vote and downn-vote any post.

- **Manual Test**:
  - **Test case**: Log in as an Admin user, go to the post management section and profile page section and complete these tasks.
    1.  **Create a new comment:** Verify that a new comment can be created and displayed underneath a post.
    2.  **Edit a comment:** Verify that an existing comment can be updated and changes are reflected. 
    3.  **Create a new profile page:** Verify that a new profile can be created and displayed with the correct information in each element.
    4.  **Update a profile page:** Verify that an existing profile page can be updated and changes are reflected.
    5.  **Up-vote and Down-vote an idea:** Verify that an idea can be provided only one upvote or one down-vote and it is displayed on the idea itself.
  - **Expected outcome**: All actions (Creating a comment, editing a comment, creating a profile page, updating a profile page, and up-voting or down-voting) should work without any errors, and the updates should be reflected in real-time. 

### 2. As an admin user, I want to be able to invalidate a user or comment, so to improve user experience and to make the overall experience appropriate and professional.

- **Manual Test**:
  - **Test case**: Log in as an Admin user, go to the post management section and complete this tasks.
    1. **Hiding An Idea:** Verify that a comment can be put under a new hidden table where it can not be seen by non-Admin users, the changes should be reflected.
    2. **Deactivating An Account:** Verify that an account can be deleted/deactivated and that the changes are reflected.
  - **Expected outcome**: Both actions (Hiding an idea and deactivating an account) should work without any errors, and the updates should be reflected in real time.

### 3. As an admin user, I want to be able to use view function, to check and get information regarding different queries, so that I can understand how the app is being used.

- **Manual Test**:
  - **Test case**: Log in as Admin user go to the post management section and complete the following action
    1. **Create a VIEW:** Verify a selection of posts have come up who have more likes than the average user's post    
  - **Expected outcome**: Should be able to see a VIEW that has all posts that have higher likes than an average post's amount of likes