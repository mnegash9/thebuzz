# User Stories and Tests

## **Authenticated User Stories**

### 1. As an authenticated user, I want to be able to log out, so that If necessary I have the ability to.

- **Manual Test**:
  - **Test case**: Login in and navigate to the profile page, then on the bottom press a logout button, which should make it so that I must log in again in the application.
  - **Expected outcome**: After the button is clicked it will bring me back to the login page and I will not be able to access my page until I login.

### 2. As an authenticated user, I want to be able to edit a comment I made previously, so that I can adjust and change parts or the whole of comment to better represent my feedback.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user and view a post that I previously provided a comment for, press on a edit button on the comment, and then rewrite the comment and post the comment again.
  - **Expected outcome**: The edit button on the comment registers a response. It then allows for the user to change their text and comment, allows it to be posted again and then reflected on the page under the comment section.

### 3. As an authenticated user, I want to be able to create a user profile page and put my information in it, so that others that view my profile know more about me.

- **Manual Test**:
  - **Test case**: Log in as new anonymous user, view a page for creating a profile page, edit a section for Username, User’s email, User’s sexual orientation, User’s gender orientation, and a note from the user, i am able to write in any of this section when first created, and re-edit my user’s note field over and over again.
  - **Expected outcome**: After logging in with google, the page pops up for creating a profile page, then there's a button for each field to provide a description, that registers a response. The button to finish and submit the profile registers a response and posts. Then on my profile page If I am the owner the edit button for any description field registers a response and I am able to edit the description of that field and re-post.

### 4. As an authenticated user, I want to be able to click on the name of any user who posts a comment or a post, so that I can see the information regarding that user who posted.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user, and press the name of someone who posted a comment or post, and then view their profile page.
  - **Expected outcome**: The Name of someone who posts registers a response once clicked, then a page pops up for the profile page of that user who posted.

### 5. As an authenticated user, I want any network errors to be reported to the user, so the user knows that due to network instability the app can not load.

- **Manual Test**:
  - **Test case**: Test by making a mock connection that is not working.
  - **Expected outcome**: The app should not be able to connect, and therefore a message will pop up that shows there is a network error.

### 6. As an authenticated user, I want to be able to post files, videos, photos, links, and other documents, so that I can provide more content to others.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user and click on the post an idea button, click another button that allows for content to be uploaded, which opens the gallery to select a photo to upload, that photo is then subsequently attached to the idea or comment.
  - **Expected outcome**: Clicking the content button registers a response to open up the gallery app, which allows the user to select a photo or video to upload, once clicked is attached to the content and when posted registers a response to have the content attached.

### 7. As an authenticated user, I want to be able to take a photo in an app to upload a comment or an idea, to be able to get a quick photo or video and upload it immediately for others to see.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user and click on the post an idea button, click another button that allows for a photo to be taken in-app, which opens the photo app, and once the photo is taken is then attached to the idea or comment.
  - **Expected outcome**: Clicking the photo icon registers a response to open up the photo app, which allows the user to take a photo, which then is attached to the content and registers a response to have the content attached in the post.

### 8. As an authenticated user, I want to be able to click on a link and it displays the content in the correct app, so that I do not have to manually navigate into another app.

- **Manual Test**:
  - **Test case**: Log in as an authenticated user and click on a link on an idea, which then brings me to the link.
  - **Expected outcome**: Clicking on a link on an idea should register a response as to open up the browser and then have it automatically look up the link.

### 9. As an authenticated user, I want my content to be stored in a local cache, to speed up the time it takes loading the content from the feed.

- **Manual Test**:
  - **Test case**: Return as an authenticated user on the application and look at the feed.
  - **Expected outcome**: The content should be present and displayed immediately without the need to reload the content.
---

## **Admin User Stories**

### 1. As an admin user, I want to be able to list the documents uploaded along with the person who uploaded it, and when they were last accessed, so that I can invalidate and/or remove old material to provide users the ability to upload whenever they need.

- **Manual Test**:
  - **Test case**: Login as Admin user go to the post management section and: 
    1. **View the Documents:** View a table that has the documents, along with the user's id, and the date of the last time it was accessed.
    2. **Remove Old Content:** Once I see old material and the storage has low space, I can delete old material from the table and from the drive.
  - **Expected outcome**: The content or documents are removed from storage and from the metadata on Supabase. And space on the storage increases.