document.addEventListener('DOMContentLoaded', () => {
    const baseUrl = 'https://matyas.homelinuxserver.org';
    const feedContainer = document.querySelector('.feedContainer');

    const userId = getQueryParam('user_id');

    /**
     * Retrieves a query parameter value by name from the URL.
     * @param {string} param - The name of the parameter to retrieve.
     * @returns {string|null} The value of the query parameter or null if not found.
     */
    function getQueryParam(param) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(param);
    }
    /**
     * Logs out the current user.
     */
    document.querySelector('.logout-button').addEventListener('click', async () => {
        try {
            const response = await fetch(`${baseUrl}/logout`, {
                method: 'POST',

                headers: {
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-store', // Prevent caching of POST requests
                }
            });

            if (response.ok) {
                window.location.href = 'https://matyas.homelinuxserver.org/signin.html';
            } else {
                console.error('Logout failed');
            }
        } catch (error) {
            console.error("Error logging out:", error);
        }
    });
    /**
     * Loads and displays user profile data.
     */
    async function loadUserProfile() {
        const apiUrl = `${baseUrl}/profile/${userId}`;

        try {
            const response = await fetch(apiUrl, {
                method: 'GET',
                headers: {
                    'Cache-Control': 'max-age=3600', //1 hour caching of profile
                },
                cache: 'force-cache', // this should use cached data if avail, otherwise fetch it
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();

            if (data.mStatus === 'ok' && data.mData) {
                const userData = data.mData;

                document.querySelector('.user-name').textContent = `${userData.first} ${userData.last}`;
                document.querySelector('.user-email').textContent = userData.email;
                document.querySelector('.user-sexual-identity').textContent = userData.sexualOrientation || 'Not specified';
                document.querySelector('.user-gender-orientation').textContent = userData.genderIdentity || 'Not specified';
                document.querySelector('.user-note').textContent = userData.note || 'No note';
            } else {
                console.error("Unexpected response status or data structure:", data);
            }
        } catch (error) {
            console.error("Failed to fetch user profile data:", error);
        }
    }
    /**
     * Loads and displays user profile data into the edit profile form.
     */
    async function loadCurrentUserData() {
        const apiUrl = `${baseUrl}/profile/${userId}`;
        try {
            const response = await fetch(apiUrl, {
                method: 'GET',
                headers: {
                    'Cache-Control': 'max-age=600, stale-while-revalidate=30', //cache for 10 mins and update in background
                },
                cache: 'default', //browser decides when to use the cache.
            });
            const data = await response.json();
            if (data.mStatus === 'ok' && data.mData) {
                const userData = data.mData;
                document.querySelector('#edit-first-name').value = userData.first || '';
                document.querySelector('#edit-last-name').value = userData.last || '';
                document.querySelector('#edit-email').value = userData.email || '';
                document.querySelector('#sexual-identity').value = userData.sexual_orient || '';
                document.querySelector('#gender-orientation').value = userData.gender_identity || '';
                document.querySelector('#note').value = userData.note || '';
            } else {
                console.error("Unexpected response status or data structure:", data);
            }
        } catch (error) {
            console.error("Failed to fetch user profile data:", error);
        }
    }
    /**
     * Sets up event listeners for the edit profile form.
     */
    function setupEditProfileListeners() {
        const saveButton = document.querySelector('.save-button');
        if (saveButton) {
            saveButton.addEventListener('click', async function () {
                const firstNameInput = document.querySelector('#edit-first-name').value;
                const lastNameInput = document.querySelector('#edit-last-name').value;
                const emailInput = document.querySelector('#edit-email').value;
                const sexualIdentity = document.querySelector('#sexual-identity').value;
                const genderOrientation = document.querySelector('#gender-orientation').value;
                const note = document.querySelector('#note').value;

                try {
                    const response = await fetch(`${baseUrl}/profile/${userId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            'Cache-Control': 'no-store', // do not cache updates
                        },
                        body: JSON.stringify({
                            userID: userId,
                            firstName: firstNameInput,
                            lastName: lastNameInput,
                            email: emailInput,
                            genderIdentity: genderOrientation,
                            sexualOrientation: sexualIdentity,
                            note: note,
                            activeAcct: true
                        })
                    });
                    if (response.ok) {
                        alert('Profile updated successfully!');
                        showSection('profile');
                    } else {
                        alert('Failed to update profile.');
                    }
                } catch (error) {
                    console.error("Error updating profile:", error);
                }
            });
        }
    }
    /**
    * Loads the public profile for a given user.
    * @param {string} userId - The unique identifier for the user.
    */
    function loadPublicProfile(userId) {
        const profileUrl = `${baseUrl}/profile/${userId}`;
        fetch(profileUrl)
            .then(response => response.json())
            .then(data => {
                if (data.mStatus === "ok" && data.mData) {
                    const userData = data.mData;
                    const profileSection = document.getElementById('public-profile');
                    profileSection.querySelector('.user-name').textContent = userData.first + " " + userData.last;
                    profileSection.querySelector('.user-email').textContent = userData.email;
                    profileSection.querySelector('.user-note').textContent = userData.note || 'No note';
                    showSection('public-profile');
                } else {
                    console.error("Failed to load user profile:", data);
                }
            })
            .catch(error => console.error("Error fetching public profile:", error));
    }

    /**
    * Fetches and displays posts from the dashboard API endpoint.
    */
    async function fetchPosts() {
        const apiUrl = `${baseUrl}/dashboard`;
        try {
            const response = await fetch(apiUrl, {
                method: 'GET',
                headers: {
                    'Cache-Control': 'stale-while-revalidate, max-age=600', // Cache for 10 minutes and update in the background
                },
                cache: 'default', // Browser decides when to use the cache
            });
            const data = await response.json();

            if (data.mStatus === "ok" && data.mData) {
                const groupedPosts = groupPostsByIdeaId(data.mData);
                displayPosts(groupedPosts);
            } else {
                console.error("Unexpected data format:", data);
            }
        } catch (error) {
            console.error("Error fetching posts:", error);
        }
    }

    /**
     * Groups posts by their idea ID, collecting comments for each post.
     * @param {Array} data - The array of post and comment data from the server.
     * @returns {Object} An object containing grouped posts.
     */
    function groupPostsByIdeaId(data) {
        const posts = {};

        data.forEach(item => {
            const ideaId = item.idea_id;

            // If the idea doesn't exist in the posts object, create it
            if (!posts[ideaId]) {
                posts[ideaId] = {
                    idea_id: ideaId,
                    idea: item.idea,
                    upvote_count: item.upvote_count,
                    downvote_count: item.downvote_count,
                    user: {
                        user_id: item.user_id,
                        first_name: item.first_name,
                        last_name: item.last_name
                    },
                    comments: []
                };
            }

            if (item.comment_id && item.comment_id > 0) {
                posts[ideaId].comments.push({
                    comment_id: item.comment_id,
                    body: item.body,
                    user: {
                        user_id: item.commenter_id,
                        first_name: item.commenter_first,
                        last_name: item.commenter_last
                    }
                });
            }
        });

        return Object.values(posts);
    }

    /**
     * function identifies URLs so that we can make them clickable in a comment/idea.
     * @param text string that holds comment/idea to check for links.
     */
    function linkify(text) {
        const urlRegex = /(http|ftp|https):\/\/([\w_-]+(?:(?:\.[\w_-]+)+))([\w.,@?^=%&:\/~+#-]*[\w@?^=%&\/~+#-])/ig;
        return text.replace(urlRegex, function (url) {
            return `<a href="${url}" target="_blank" rel="noopener noreferrer">${url}</a>`;
        });
    }

    /**
     * Renders posts in the feed container.
     * @param {Array} posts - An array of grouped posts to display.
     */
    function displayPosts(posts) {
        feedContainer.innerHTML = '';

        posts.forEach(post => {
            const postElement = document.createElement('div');
            postElement.classList.add('post');

            postElement.innerHTML = `
            <div class="post-header">
                <div class="post-user-icon"></div>
                <div class="post-username" user-id="${post.user.user_id}">
                    ${post.user.first_name} ${post.user.last_name}
                </div>
            </div>
            <div class="post-body">${linkify(post.idea)}</div>
            <div class="vote-section">
                <button class="vote-button upvote" data-id="${post.idea_id}"><i class="fas fa-arrow-up"></i></button>
                <span class="vote-count upvote-count">${post.upvote_count}</span>
                <button class="vote-button downvote" data-id="${post.idea_id}"><i class="fas fa-arrow-down"></i></button>
                <span class="vote-count downvote-count">${post.downvote_count}</span>
            </div>
            <hr>
            <div class="comment-section">
                ${post.comments.map(comment => `
                    <div class="comment" data-comment-id="${comment.comment_id}">
                        <div class="post-user-icon" style="float: left; margin-right: 5px;"></div>
                        <div class="comment-username" user-id="${comment.user.user_id}">
                            ${comment.user.first_name} ${comment.user.last_name}
                        </div>
                        <div class="comment-body">${linkify(comment.body)}</div>
                        ${comment.user.user_id === userId ? `<button class="edit-comment-button" data-id="${comment.comment_id}">Edit</button>` : ''}
                    </div>
                `).join('')}
                <input type="text" class="comment-input" placeholder="Add a comment...">
                <button class="add-comment-button" data-id="${post.idea_id}">Add</button>
            </div>
            `;

            feedContainer.appendChild(postElement);
        });

        addEventListeners();
    }

    /**
     * Adds event listeners for various user interactions within the dashboard.
     */
    function addEventListeners() {
        document.querySelectorAll('.upvote').forEach(button => {
            button.addEventListener('click', () => {
                const ideaId = button.getAttribute('data-id');
                vote(ideaId, 'upvote');
            });
        });

        document.querySelectorAll('.downvote').forEach(button => {
            button.addEventListener('click', () => {
                const ideaId = button.getAttribute('data-id');
                vote(ideaId, 'downvote');
            });
        });

        document.querySelectorAll('.add-comment-button').forEach(button => {
            button.addEventListener('click', () => {
                const ideaId = button.getAttribute('data-id');
                const commentInput = button.previousElementSibling;
                postComment(ideaId, commentInput.value);
                commentInput.value = '';
            });
        });



        document.querySelectorAll('.edit-comment-button').forEach(button => {
            button.addEventListener('click', handleEditButtonClick);
        });

        document.querySelector('.post-button').addEventListener('click', async () => {
            const postButton = document.querySelector('.post-button');
            const messageInput = document.querySelector('.post-input');
            const message = messageInput.value;

            if (message.trim() && !postButton.disabled) {
                // Disable button while posting
                postButton.disabled = true;

                try {
                    const response = await fetch(`${baseUrl}/ideas`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Cache-Control': 'no-store',
                        },
                        body: JSON.stringify({ mIdea: message })
                    });

                    if (response.ok) {
                        messageInput.value = '';
                        await fetchPosts(); // Wait for posts to update
                    } else {
                        throw new Error('Failed to post');
                    }
                } catch (error) {
                    console.error("Error posting message:", error);
                    // Show error to user
                } finally {
                    // Re-enable button whether request succeeded or failed
                    postButton.disabled = false;
                }
            }
        });

        document.querySelectorAll('.post-username').forEach(element => {
            element.addEventListener('click', function () {
                const userId = this.getAttribute('user-id');
                loadPublicProfile(userId);
            });
        });

        document.querySelectorAll('.comment-username').forEach(element => {
            element.addEventListener('click', function () {
                const userId = this.getAttribute('user-id');
                loadPublicProfile(userId);
            });
        });
    }

    /**
     * Casts a vote for an idea, either upvote or downvote.
     * @param {string} ideaId - ID of the idea to vote on.
     * @param {string} type - Type of vote, either 'upvote' or 'downvote'.
     */
    async function vote(ideaId, type) {
        const endpoint = type === 'upvote' ? `/ideas/${ideaId}/upvote` : `/ideas/${ideaId}/downvote`;

        try {
            const response = await fetch(`${baseUrl}${endpoint}`, { method: 'PUT' });
            if (response.ok) {
                fetchPosts();
            }
        } catch (error) {
            console.error(`Error ${type}voting:`, error);
        }
    }

    /**
     * Posts a new comment to an idea.
     * @param {string} ideaId - ID of the idea to comment on.
     * @param {string} comment - Comment text to be added.
     */
    async function postComment(ideaId, comment) {
        if (comment.trim()) {
            try {
                const response = await fetch(`${baseUrl}/ideas/${ideaId}/comments`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Cache-Control': 'no-store', // Prevent caching of POST requests
                    },
                    body: JSON.stringify({ mIdea: comment })
                });

                if (response.ok) {
                    fetchPosts();
                }
            } catch (error) {
                console.error("Error posting comment:", error);
            }
        }
    }
    /**
     * Handles click event on edit comment button, setting up input for editing.
     * @param {Event} event - The event triggered by clicking the edit button.
     */
    function handleEditButtonClick(event) {
        const button = event.target;
        const commentId = button.getAttribute('data-id');
        const commentDiv = button.parentElement;
        const commentBodyDiv = commentDiv.querySelector('.comment-body');

        const currentText = commentBodyDiv.textContent;
        commentBodyDiv.innerHTML = `<input type="text" class="edit-comment-input" value="${currentText}">`;
        button.textContent = "Save";
        button.classList.add('save-comment-button');

        button.removeEventListener('click', handleEditButtonClick);
        button.addEventListener('click', () => saveComment(commentId, commentDiv));
    }

    /**
     * Saves the updated comment text to the server.
     * @param {string} commentId - ID of the comment being edited.
     * @param {HTMLElement} commentDiv - Div element containing the comment.
     */
    async function saveComment(commentId, commentDiv) {
        const editInput = commentDiv.querySelector('.edit-comment-input');
        const updatedText = editInput.value;

        try {
            const response = await fetch(`${baseUrl}/comments/${commentId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-store', // Prevent caching of POST requests
                },
                body: JSON.stringify({ body: updatedText })
            });

            if (response.ok) {
                const commentBodyDiv = commentDiv.querySelector('.comment-body');
                commentBodyDiv.textContent = updatedText;

                // Change "Save" button back to "Edit"
                const saveButton = commentDiv.querySelector('.save-comment-button');
                saveButton.textContent = "Edit";
                saveButton.classList.remove('save-comment-button');
                saveButton.removeEventListener('click', saveComment);
                saveButton.addEventListener('click', handleEditButtonClick);
            }
        } catch (error) {
            console.error("Error saving comment:", error);
        }
    }
    /**
     * Toggles visibility of sections within the page based on the given section ID.
     * @param {string} sectionId - The ID of the section to display.
     */
    function showSection(sectionId) {
        // Hide all sections
        document.querySelectorAll('.content').forEach(section => {
            section.style.display = 'none';
        });

        // Show the requested section
        const section = document.getElementById(sectionId);
        if (section) {
            section.style.display = 'block';
        } else {
            console.error('Requested section not found:', sectionId);
        }
    }

    /**
     * Initializes the button and handles functionality for uploading files to post in an idea.
     */
    function setupFileUploadButton() {
        const fileInput = document.querySelector('#file-input');
        const fileNameDisplay = document.querySelector('#file-name-display');
        const postButton = document.querySelector('.post-button');
        const postInput = document.querySelector('.post-input');

        let uploadedFile = null;
        let isSubmitting = false; // Track submission state

        if (fileInput) {
            fileInput.addEventListener('change', () => {
                if (fileInput.files && fileInput.files.length > 0) {
                    uploadedFile = fileInput.files[0];
                    fileNameDisplay.textContent = uploadedFile.name;
                } else {
                    fileNameDisplay.textContent = 'No file selected';
                    uploadedFile = null;
                }
            });
        }

        if (postButton && postInput) {
            postButton.addEventListener('click', async () => {
                if (isSubmitting) return; // Prevent duplicate submissions

                const message = postInput.value.trim();

                if (!message && !uploadedFile) {
                    alert('Please enter a message or attach a file.');
                    return;
                }

                try {
                    isSubmitting = true;
                    postButton.disabled = true;
                    postButton.textContent = 'Posting...'; // Visual feedback

                    let base64String = null;
                    if (uploadedFile) {
                        try {
                            postButton.textContent = 'Processing file...';
                            base64String = await convertFileToBase64(uploadedFile);
                        } catch (error) {
                            console.error('Error converting file to Base64:', error);
                            alert('Failed to process the uploaded file.');
                            return;
                        }
                    }

                    const payload = {
                        mIdea: message,
                    };

                    if (base64String) {
                        payload.file = base64String;
                        payload.fileName = uploadedFile.name;
                    }

                    postButton.textContent = 'Uploading...';
                    const response = await fetch(`${baseUrl}/ideas`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Cache-Control': 'no-store',
                        },
                        body: JSON.stringify(payload),
                    });

                    if (response.ok) {
                        // Clear form
                        postInput.value = '';
                        if (fileInput) fileInput.value = '';
                        if (fileNameDisplay) fileNameDisplay.textContent = 'No file selected';
                        uploadedFile = null;

                        await fetchPosts();
                    } else {
                        const errorMessage = await response.text();
                        throw new Error(errorMessage);
                    }
                } catch (error) {
                    console.error('Error posting message:', error);
                    alert(`An error occurred: ${error.message || 'Failed to post'}`);
                } finally {
                    isSubmitting = false;
                    postButton.disabled = false;
                    postButton.textContent = 'Post'; // Reset button text
                }
            });
        }
    }


    /**
     * Converts a file to a base64 string
     * @param {File} file - The file to convert
     * @returns {Promise<string>} A promise that resolves to the base64 string
     */
    function convertFileToBase64(file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => {
                // Split to remove the data URL prefix and get just the base64 string
                const base64String = reader.result.split(',')[1];
                resolve(base64String);
            };
            reader.onerror = (error) => reject(error);
            reader.readAsDataURL(file);
        });
    }


    if (document.querySelector('#file-input')) {
        setupFileUploadButton();
    }

    if (document.querySelector('.feedContainer')) {
        fetchPosts();
    }
    if (document.querySelector('.user-name')) {
        loadUserProfile();
    }
    if (document.querySelector('#edit-first-name')) {
        loadCurrentUserData();
        setupEditProfileListeners();
    }
});