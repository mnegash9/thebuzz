"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
document.addEventListener('DOMContentLoaded', function () {
    var baseUrl = 'https://thebuzz.homelinuxserver.org';
    var feedContainer = document.querySelector('.feedContainer');
    var userId = getQueryParam('user_id');
    /**
     * Retrieves a query parameter value by name from the URL.
     * @param {string} param - The name of the parameter to retrieve.
     * @returns {string|null} The value of the query parameter or null if not found.
     */
    function getQueryParam(param) {
        var urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(param);
    }
    /**
     * Logs out the current user.
     */
    var logoutButton = document.querySelector('.logout-button');
    if (!logoutButton) {
        console.log("Log out button not found");
    }
    else {
        logoutButton.addEventListener('click', function () { return __awaiter(void 0, void 0, void 0, function () {
            var email, userEmail, response, error_1;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        _a.trys.push([0, 2, , 3]);
                        email = document.querySelector('.user-email').textContent;
                        userEmail = (email == null) ? '' : email;
                        return [4 /*yield*/, fetch("".concat(baseUrl, "/logout"), {
                                method: 'POST',
                                headers: {
                                    'X-User-Email': userEmail,
                                    'Content-Type': 'application/json',
                                    'Cache-Control': 'no-store', // Prevent caching of POST requests
                                }
                            })];
                    case 1:
                        response = _a.sent();
                        if (response.ok) {
                            localStorage.clear();
                            sessionStorage.clear();
                            document.cookie = "sessionId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // Clear the sessionId cookie
                            window.location.href = baseUrl;
                        }
                        else {
                            console.error('Logout failed. Please try again.');
                        }
                        return [3 /*break*/, 3];
                    case 2:
                        error_1 = _a.sent();
                        console.error("Error logging out:", error_1);
                        return [3 /*break*/, 3];
                    case 3: return [2 /*return*/];
                }
            });
        }); });
    }
    /**
     * Loads and displays user profile data.
     */
    function loadUserProfile() {
        return __awaiter(this, void 0, void 0, function () {
            var apiUrl, response, data, userData, error_2;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        apiUrl = "".concat(baseUrl, "/profile/").concat(userId);
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 4, , 5]);
                        return [4 /*yield*/, fetch(apiUrl, {
                                method: 'GET',
                                headers: {
                                    'Cache-Control': 'max-age=3600', //1 hour caching of profile
                                },
                                cache: 'force-cache', // this should use cached data if avail, otherwise fetch it
                            })];
                    case 2:
                        response = _a.sent();
                        if (!response.ok) {
                            throw new Error("HTTP error! Status: ".concat(response.status));
                        }
                        return [4 /*yield*/, response.json()];
                    case 3:
                        data = _a.sent();
                        if (data.mStatus === 'ok' && data.mData) {
                            userData = data.mData;
                            document.querySelector('.user-name').textContent = "".concat(userData.first, " ").concat(userData.last);
                            document.querySelector('.user-email').textContent = userData.email;
                            document.querySelector('.user-sexual-identity').textContent = userData.sexualOrientation || 'Not specified';
                            document.querySelector('.user-gender-orientation').textContent = userData.genderIdentity || 'Not specified';
                            document.querySelector('.user-note').textContent = userData.note || 'No note';
                        }
                        else {
                            console.error("Unexpected response status or data structure:", data);
                        }
                        return [3 /*break*/, 5];
                    case 4:
                        error_2 = _a.sent();
                        console.error("Failed to fetch user profile data:", error_2);
                        return [3 /*break*/, 5];
                    case 5: return [2 /*return*/];
                }
            });
        });
    }
    /**
     * Loads and displays user profile data into the edit profile form.
     */
    function loadCurrentUserData() {
        return __awaiter(this, void 0, void 0, function () {
            var apiUrl, response, data, userData, error_3;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        apiUrl = "".concat(baseUrl, "/profile/").concat(userId);
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 4, , 5]);
                        return [4 /*yield*/, fetch(apiUrl, {
                                method: 'GET',
                                headers: {
                                    'Cache-Control': 'max-age=600, stale-while-revalidate=30', //cache for 10 mins and update in background
                                },
                                cache: 'default', //browser decides when to use the cache.
                            })];
                    case 2:
                        response = _a.sent();
                        return [4 /*yield*/, response.json()];
                    case 3:
                        data = _a.sent();
                        if (data.mStatus === 'ok' && data.mData) {
                            userData = data.mData;
                            document.querySelector('#edit-first-name').value = userData.first || '';
                            document.querySelector('#edit-last-name').value = userData.last || '';
                            document.querySelector('#edit-email').value = userData.email || '';
                            document.querySelector('#sexual-identity').value = userData.sexual_orient || '';
                            document.querySelector('#gender-orientation').value = userData.gender_identity || '';
                            document.querySelector('#note').value = userData.note || '';
                        }
                        else {
                            console.error("Unexpected response status or data structure:", data);
                        }
                        return [3 /*break*/, 5];
                    case 4:
                        error_3 = _a.sent();
                        console.error("Failed to fetch user profile data:", error_3);
                        return [3 /*break*/, 5];
                    case 5: return [2 /*return*/];
                }
            });
        });
    }
    /**
     * Sets up event listeners for the edit profile form.
     */
    function setupEditProfileListeners() {
        var saveButton = document.querySelector('.save-button');
        if (saveButton) {
            saveButton.addEventListener('click', function () {
                return __awaiter(this, void 0, void 0, function () {
                    var firstNameInput, lastNameInput, emailInput, sexualIdentity, genderOrientation, note, response, error_4;
                    return __generator(this, function (_a) {
                        switch (_a.label) {
                            case 0:
                                firstNameInput = document.querySelector('#edit-first-name').value;
                                lastNameInput = document.querySelector('#edit-last-name').value;
                                emailInput = document.querySelector('#edit-email').value;
                                sexualIdentity = document.querySelector('#sexual-identity').value;
                                genderOrientation = document.querySelector('#gender-orientation').value;
                                note = document.querySelector('#note').value;
                                _a.label = 1;
                            case 1:
                                _a.trys.push([1, 3, , 4]);
                                return [4 /*yield*/, fetch("".concat(baseUrl, "/profile/").concat(userId), {
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
                                    })];
                            case 2:
                                response = _a.sent();
                                if (response.ok) {
                                    alert('Profile updated successfully!');
                                    showSection('profile');
                                }
                                else {
                                    alert('Failed to update profile.');
                                }
                                return [3 /*break*/, 4];
                            case 3:
                                error_4 = _a.sent();
                                console.error("Error updating profile:", error_4);
                                return [3 /*break*/, 4];
                            case 4: return [2 /*return*/];
                        }
                    });
                });
            });
        }
    }
    /**
    * Loads the public profile for a given user.
    * @param {string} userId - The unique identifier for the user.
    */
    function loadPublicProfile(userId) {
        var profileUrl = "".concat(baseUrl, "/profile/").concat(userId);
        fetch(profileUrl)
            .then(function (response) { return response.json(); })
            .then(function (data) {
            if (data.mStatus === "ok" && data.mData) {
                var userData = data.mData;
                var profileSection = document.getElementById('public-profile');
                profileSection.querySelector('.user-name').textContent = userData.first + " " + userData.last;
                profileSection.querySelector('.user-email').textContent = userData.email;
                profileSection.querySelector('.user-note').textContent = userData.note || 'No note';
                showSection('public-profile');
            }
            else {
                console.error("Failed to load user profile:", data);
            }
        })
            .catch(function (error) { return console.error("Error fetching public profile:", error); });
    }
    /**
    * Fetches and displays posts from the dashboard API endpoint.
    */
    function fetchPosts() {
        return __awaiter(this, void 0, void 0, function () {
            var apiUrl, response, data, groupedPosts, error_5;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        apiUrl = "".concat(baseUrl, "/dashboard");
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 4, , 5]);
                        return [4 /*yield*/, fetch(apiUrl, {
                                method: 'GET',
                                headers: {
                                    'Cache-Control': 'stale-while-revalidate, max-age=600', // Cache for 10 minutes and update in the background
                                },
                                cache: 'default', // Browser decides when to use the cache
                            })];
                    case 2:
                        response = _a.sent();
                        return [4 /*yield*/, response.json()];
                    case 3:
                        data = _a.sent();
                        if (data.mStatus === "ok" && data.mData) {
                            groupedPosts = groupPostsByIdeaId(data.mData);
                            displayPosts(groupedPosts);
                        }
                        else {
                            console.error("Unexpected data format:", data);
                        }
                        return [3 /*break*/, 5];
                    case 4:
                        error_5 = _a.sent();
                        console.error("Error fetching posts:", error_5);
                        return [3 /*break*/, 5];
                    case 5: return [2 /*return*/];
                }
            });
        });
    }
    /**
     * Groups posts by their idea ID, collecting comments for each post.
     * @param {Array} data - The array of post and comment data from the server.
     * @returns {Object} An object containing grouped posts.
     */
    function groupPostsByIdeaId(data) {
        var posts = {};
        data.forEach(function (item) {
            var ideaId = item.idea_id;
            // If the idea doesn't exist in the posts object, create it
            if (!posts[ideaId]) {
                posts[ideaId] = {
                    idea_id: ideaId,
                    idea: item.idea,
                    idea_file: item.idea_file, // Add idea_file
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
                    comment_file: item.comment_file, // Add comment_file
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
        var urlRegex = /(http|ftp|https):\/\/([\w_-]+(?:(?:\.[\w_-]+)+))([\w.,@?^=%&:\/~+#-]*[\w@?^=%&\/~+#-])/ig;
        return text.replace(urlRegex, function (url) {
            return "<a href=\"".concat(url, "\" target=\"_blank\" rel=\"noopener noreferrer\">").concat(url, "</a>");
        });
    }
    /**
     * Renders posts in the feed container.
     * @param {Array} posts - An array of grouped posts to display.
     */
    function displayPosts(posts) {
        feedContainer.innerHTML = '';
        posts.forEach(function (post) {
            var postElement = document.createElement('div');
            postElement.classList.add('post');
            postElement.innerHTML = "\n            <div class=\"post-header\">\n                <div class=\"post-user-icon\"></div>\n                <div class=\"post-username\" user-id=\"".concat(post.user.user_id, "\">\n                    ").concat(post.user.first_name, " ").concat(post.user.last_name, "\n                </div>\n            </div>\n            <div class=\"post-body\">").concat(linkify(post.idea), "</div>\n            ").concat(post.idea_file ? renderFile(post.idea_file, 'idea') : '', "\n            <div class=\"vote-section\">\n                <button class=\"vote-button upvote\" data-id=\"").concat(post.idea_id, "\"><i class=\"fas fa-arrow-up\"></i></button>\n                <span class=\"vote-count upvote-count\">").concat(post.upvote_count, "</span>\n                <button class=\"vote-button downvote\" data-id=\"").concat(post.idea_id, "\"><i class=\"fas fa-arrow-down\"></i></button>\n                <span class=\"vote-count downvote-count\">").concat(post.downvote_count, "</span>\n            </div>\n            <hr>\n            <div class=\"comment-section\">\n                ").concat(post.comments.map(function (comment) { return "\n                    <div class=\"comment\" data-comment-id=\"".concat(comment.comment_id, "\">\n                        <div class=\"post-user-icon\" style=\"float: left; margin-right: 5px;\"></div>\n                        <div class=\"comment-username\" user-id=\"").concat(comment.user.user_id, "\">\n                            ").concat(comment.user.first_name, " ").concat(comment.user.last_name, "\n                        </div>\n                        <div class=\"comment-body\">").concat(linkify(comment.body), "</div>\n                        ").concat(comment.comment_file ? renderFile(comment.comment_file, 'comment') : '', "\n                        ").concat(comment.user.user_id === userId ? "<button class=\"edit-comment-button\" data-id=\"".concat(comment.comment_id, "\">Edit</button>") : '', "\n                    </div>\n                "); }).join(''), "\n                <input type=\"text\" class=\"comment-input\" placeholder=\"Add a comment...\">\n                <button class=\"add-comment-button\" data-id=\"").concat(post.idea_id, "\">Add</button>\n            </div>\n            ");
            feedContainer.appendChild(postElement);
        });
        addEventListeners();
    }
    /**
     * Renders a file based on its type.
     * @param {string} base64File - The base64 encoded file.
     * @param {string} type - The type of the file ('idea' or 'comment').
     * @returns {string} The HTML string to render the file.
     */
    function renderFile(base64File, type) {
        var fileType = base64File.substring(0, 5);
        if (fileType === 'iVBOR' || fileType === '/9j/4') { // Check if the file is an image (PNG or JPEG)
            return "<div class=\"".concat(type, "-file\"><img src=\"data:image/png;base64,").concat(base64File, "\" alt=\"Attached file\"></div>");
        }
        else {
            return "<div class=\"".concat(type, "-file\"><a href=\"data:application/octet-stream;base64,").concat(base64File, "\" download=\"file\">Download attached file</a></div>");
        }
    }
    /**
     * Adds event listeners for various user interactions within the dashboard.
     */
    function addEventListeners() {
        var _this = this;
        document.querySelectorAll('.upvote').forEach(function (button) {
            button.addEventListener('click', function () {
                var ideaId = button.getAttribute('data-id');
                vote(ideaId, 'upvote');
            });
        });
        document.querySelectorAll('.downvote').forEach(function (button) {
            button.addEventListener('click', function () {
                var ideaId = button.getAttribute('data-id');
                vote(ideaId, 'downvote');
            });
        });
        document.querySelectorAll('.add-comment-button').forEach(function (button) {
            button.addEventListener('click', function () {
                var ideaId = button.getAttribute('data-id');
                var commentInput = button.previousElementSibling;
                postComment(ideaId, commentInput.value);
                commentInput.value = '';
            });
        });
        document.querySelectorAll('.edit-comment-button').forEach(function (button) {
            button.addEventListener('click', handleEditButtonClick);
        });
        document.querySelector('.post-button').addEventListener('click', function () { return __awaiter(_this, void 0, void 0, function () {
            var postButton, messageInput, message, response, error_6;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        postButton = document.querySelector('.post-button');
                        messageInput = document.querySelector('.post-input');
                        message = messageInput.value;
                        if (!(message.trim() && !postButton.disabled)) return [3 /*break*/, 8];
                        // Disable button while posting
                        postButton.disabled = true;
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 6, 7, 8]);
                        return [4 /*yield*/, fetch("".concat(baseUrl, "/ideas"), {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Cache-Control': 'no-store',
                                },
                                body: JSON.stringify({ mIdea: message })
                            })];
                    case 2:
                        response = _a.sent();
                        if (!response.ok) return [3 /*break*/, 4];
                        messageInput.value = '';
                        return [4 /*yield*/, fetchPosts()];
                    case 3:
                        _a.sent(); // Wait for posts to update
                        return [3 /*break*/, 5];
                    case 4: throw new Error('Failed to post');
                    case 5: return [3 /*break*/, 8];
                    case 6:
                        error_6 = _a.sent();
                        console.error("Error posting message:", error_6);
                        return [3 /*break*/, 8];
                    case 7:
                        // Re-enable button whether request succeeded or failed
                        postButton.disabled = false;
                        return [7 /*endfinally*/];
                    case 8: return [2 /*return*/];
                }
            });
        }); });
        document.querySelectorAll('.post-username').forEach(function (element) {
            element.addEventListener('click', function () {
                var userId = this.getAttribute('user-id');
                loadPublicProfile(userId);
            });
        });
        document.querySelectorAll('.comment-username').forEach(function (element) {
            element.addEventListener('click', function () {
                var userId = this.getAttribute('user-id');
                loadPublicProfile(userId);
            });
        });
    }
    /**
     * Casts a vote for an idea, either upvote or downvote.
     * @param {string} ideaId - ID of the idea to vote on.
     * @param {string} type - Type of vote, either 'upvote' or 'downvote'.
     */
    function vote(ideaId, type) {
        return __awaiter(this, void 0, void 0, function () {
            var endpoint, response, error_7;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        endpoint = type === 'upvote' ? "/ideas/".concat(ideaId, "/upvote") : "/ideas/".concat(ideaId, "/downvote");
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 3, , 4]);
                        return [4 /*yield*/, fetch("".concat(baseUrl).concat(endpoint), { method: 'PUT' })];
                    case 2:
                        response = _a.sent();
                        if (response.ok) {
                            fetchPosts();
                        }
                        return [3 /*break*/, 4];
                    case 3:
                        error_7 = _a.sent();
                        console.error("Error ".concat(type, "voting:"), error_7);
                        return [3 /*break*/, 4];
                    case 4: return [2 /*return*/];
                }
            });
        });
    }
    /**
     * Posts a new comment to an idea.
     * @param {string} ideaId - ID of the idea to comment on.
     * @param {string} comment - Comment text to be added.
     */
    function postComment(ideaId, comment) {
        return __awaiter(this, void 0, void 0, function () {
            var response, error_8;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (!comment.trim()) return [3 /*break*/, 4];
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 3, , 4]);
                        return [4 /*yield*/, fetch("".concat(baseUrl, "/ideas/").concat(ideaId, "/comments"), {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Cache-Control': 'no-store', // Prevent caching of POST requests
                                },
                                body: JSON.stringify({ mIdea: comment })
                            })];
                    case 2:
                        response = _a.sent();
                        if (response.ok) {
                            fetchPosts();
                        }
                        return [3 /*break*/, 4];
                    case 3:
                        error_8 = _a.sent();
                        console.error("Error posting comment:", error_8);
                        return [3 /*break*/, 4];
                    case 4: return [2 /*return*/];
                }
            });
        });
    }
    /**
     * Handles click event on edit comment button, setting up input for editing.
     * @param {Event} event - The event triggered by clicking the edit button.
     */
    function handleEditButtonClick(event) {
        var button = event.target;
        var commentId = button.getAttribute('data-id');
        var commentDiv = button.parentElement;
        var commentBodyDiv = commentDiv.querySelector('.comment-body');
        var currentText = commentBodyDiv.textContent;
        commentBodyDiv.innerHTML = "<input type=\"text\" class=\"edit-comment-input\" value=\"".concat(currentText, "\">");
        button.textContent = "Save";
        button.classList.add('save-comment-button');
        button.removeEventListener('click', handleEditButtonClick);
        button.addEventListener('click', function () { return saveComment(commentId, commentDiv); });
    }
    /**
     * Saves the updated comment text to the server.
     * @param {string} commentId - ID of the comment being edited.
     * @param {HTMLElement} commentDiv - Div element containing the comment.
     */
    function saveComment(commentId, commentDiv) {
        return __awaiter(this, void 0, void 0, function () {
            var editInput, updatedText, response, commentBodyDiv, saveButton, error_9;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        editInput = commentDiv.querySelector('.edit-comment-input');
                        updatedText = editInput.value;
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 3, , 4]);
                        return [4 /*yield*/, fetch("".concat(baseUrl, "/comments/").concat(commentId), {
                                method: 'PUT',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Cache-Control': 'no-store', // Prevent caching of POST requests
                                },
                                body: JSON.stringify({ body: updatedText })
                            })];
                    case 2:
                        response = _a.sent();
                        if (response.ok) {
                            commentBodyDiv = commentDiv.querySelector('.comment-body');
                            commentBodyDiv.textContent = updatedText;
                            saveButton = commentDiv.querySelector('.save-comment-button');
                            saveButton.textContent = "Edit";
                            saveButton.classList.remove('save-comment-button');
                            saveButton.removeEventListener('click', saveComment);
                            saveButton.addEventListener('click', handleEditButtonClick);
                        }
                        return [3 /*break*/, 4];
                    case 3:
                        error_9 = _a.sent();
                        console.error("Error saving comment:", error_9);
                        return [3 /*break*/, 4];
                    case 4: return [2 /*return*/];
                }
            });
        });
    }
    /**
     * Toggles visibility of sections within the page based on the given section ID.
     * @param {string} sectionId - The ID of the section to display.
     */
    function showSection(sectionId) {
        // Hide all sections
        document.querySelectorAll('.content').forEach(function (section) {
            section.style.display = 'none';
        });
        // Show the requested section
        var section = document.getElementById(sectionId);
        if (section) {
            section.style.display = 'block';
        }
        else {
            console.error('Requested section not found:', sectionId);
        }
    }
    /**
     * Initializes the button and handles functionality for uploading files to post in an idea.
     */
    function setupFileUploadButton() {
        var _this = this;
        var fileInput = document.querySelector('#file-input');
        var fileNameDisplay = document.querySelector('#file-name-display');
        var postButton = document.querySelector('.post-button');
        var postInput = document.querySelector('.post-input');
        var uploadedFile = null;
        var isSubmitting = false; // Track submission state
        if (fileInput) {
            fileInput.addEventListener('change', function () {
                if (fileInput.files && fileInput.files.length > 0) {
                    uploadedFile = fileInput.files[0];
                    fileNameDisplay.textContent = uploadedFile.name;
                }
                else {
                    fileNameDisplay.textContent = 'No file selected';
                    uploadedFile = null;
                }
            });
        }
        if (postButton && postInput) {
            postButton.addEventListener('click', function () { return __awaiter(_this, void 0, void 0, function () {
                var message, base64String, error_10, payload, response, errorMessage, error_11;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            if (isSubmitting)
                                return [2 /*return*/]; // Prevent duplicate submissions
                            message = postInput.value.trim();
                            if (!message && !uploadedFile) {
                                alert('Please enter a message or attach a file.');
                                return [2 /*return*/];
                            }
                            _a.label = 1;
                        case 1:
                            _a.trys.push([1, 11, 12, 13]);
                            isSubmitting = true;
                            postButton.disabled = true;
                            postButton.textContent = 'Posting...'; // Visual feedback
                            base64String = null;
                            if (!uploadedFile) return [3 /*break*/, 5];
                            _a.label = 2;
                        case 2:
                            _a.trys.push([2, 4, , 5]);
                            postButton.textContent = 'Processing file...';
                            return [4 /*yield*/, convertFileToBase64(uploadedFile)];
                        case 3:
                            base64String = _a.sent();
                            return [3 /*break*/, 5];
                        case 4:
                            error_10 = _a.sent();
                            console.error('Error converting file to Base64:', error_10);
                            alert('Failed to process the uploaded file.');
                            return [2 /*return*/];
                        case 5:
                            payload = {
                                mIdea: message,
                            };
                            if (base64String) {
                                payload.file = base64String;
                                payload.fileName = uploadedFile.name;
                            }
                            postButton.textContent = 'Uploading...';
                            return [4 /*yield*/, fetch("".concat(baseUrl, "/ideas"), {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json',
                                        'Cache-Control': 'no-store',
                                    },
                                    body: JSON.stringify(payload),
                                })];
                        case 6:
                            response = _a.sent();
                            if (!response.ok) return [3 /*break*/, 8];
                            // Clear form
                            postInput.value = '';
                            if (fileInput)
                                fileInput.value = '';
                            if (fileNameDisplay)
                                fileNameDisplay.textContent = 'No file selected';
                            uploadedFile = null;
                            return [4 /*yield*/, fetchPosts()];
                        case 7:
                            _a.sent();
                            return [3 /*break*/, 10];
                        case 8: return [4 /*yield*/, response.text()];
                        case 9:
                            errorMessage = _a.sent();
                            throw new Error(errorMessage);
                        case 10: return [3 /*break*/, 13];
                        case 11:
                            error_11 = _a.sent();
                            console.error('Error posting message:', error_11);
                            alert("An error occurred: ".concat(error_11.message || 'Failed to post'));
                            return [3 /*break*/, 13];
                        case 12:
                            isSubmitting = false;
                            postButton.disabled = false;
                            postButton.textContent = 'Post'; // Reset button text
                            return [7 /*endfinally*/];
                        case 13: return [2 /*return*/];
                    }
                });
            }); });
        }
    }
    /**
     * Converts a file to a base64 string
     * @param {File} file - The file to convert
     * @returns {Promise<string>} A promise that resolves to the base64 string
     */
    function convertFileToBase64(file) {
        return new Promise(function (resolve, reject) {
            var reader = new FileReader();
            reader.onload = function () {
                // Split to remove the data URL prefix and get just the base64 string
                var base64String = reader.result.split(',')[1];
                resolve(base64String);
            };
            reader.onerror = function (error) { return reject(error); };
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
/// <reference path="app.ts" /> 
// check if new idea form present 
describe("UI Tests", function () {
    it("New Idea Form is present", function () {
        var postInput = document.querySelector('.post-input');
        var postButton = document.querySelector('.post-button');
        expect(postInput).not.toBeNull();
        expect(postButton).not.toBeNull();
    });
    // Check if the like section is properly rendered
    it("Like section is properly rendered", function () {
        var contentDiv = document.createElement('div');
        contentDiv.classList.add('content');
        document.body.appendChild(contentDiv);
        var mainList = new ElementList();
        var mockData = {
            mData: [{
                    mId: 1,
                    mIdea: "Test idea",
                    mLikes: 5
                }]
        };
        mainList.update(mockData);
        var likeButton = document.querySelector('.like-button');
        var likeCount = document.querySelector('.like-count');
        expect(likeButton).not.toBeNull();
        expect(likeCount).not.toBeNull();
        expect(likeCount === null || likeCount === void 0 ? void 0 : likeCount.textContent).toBe("5");
        document.body.removeChild(contentDiv);
    });
});
describe("Logic Tests", function () {
    it("Submitting empty idea shows an error alert", function () {
        var newEntryForm = new NewEntryForm();
        // creates the mock alerts
        spyOn(window, 'alert');
        // DOM elements for test
        var postInput = document.createElement('input');
        postInput.classList.add('post-input');
        postInput.value = '   '; // Empty input with whitespace
        document.body.appendChild(postInput);
        // submit form
        newEntryForm.submitForm();
        // Cannot post an empty idea 
        expect(window.alert).toHaveBeenCalledWith("Error: Cannot post an empty idea");
        // remove input in DOM
        document.body.removeChild(postInput);
    });
    // Check if delete confirmation is shown
    it("Delete button shows confirmation dialog", function () {
        var contentDiv = document.createElement('div');
        contentDiv.classList.add('content');
        document.body.appendChild(contentDiv);
        var mainList = new ElementList();
        spyOn(window, 'confirm').and.returnValue(false);
        var mockData = {
            mData: [{
                    mId: 1,
                    mIdea: "Test idea",
                    mLikes: 0
                }]
        };
        mainList.update(mockData);
        var deleteButton = document.querySelector('.delete-button');
        deleteButton === null || deleteButton === void 0 ? void 0 : deleteButton.click();
        expect(window.confirm).toHaveBeenCalledWith("Are you sure you want to delete this idea?");
        document.body.removeChild(contentDiv);
    });
    // Check if posts are sorted correctly (newest first)
    it("Posts are sorted with newest first", function () {
        var contentDiv = document.createElement('div');
        contentDiv.classList.add('content');
        document.body.appendChild(contentDiv);
        var mainList = new ElementList();
        var mockData = {
            mData: [
                { mId: 1, mIdea: "Older post", mLikes: 0 },
                { mId: 2, mIdea: "Newer post", mLikes: 0 }
            ]
        };
        mainList.update(mockData);
        var posts = document.querySelectorAll('.post-body');
        expect(posts[0].textContent).toBe("Newer post");
        expect(posts[1].textContent).toBe("Older post");
        document.body.removeChild(contentDiv);
    });
});
