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
    var baseUrl = 'https://team-dog.dokku.cse.lehigh.edu';
    var feedContainer = document.querySelector('.feedContainer');
    var userId = getQueryParam('user_id');
    function getQueryParam(param) {
        var urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(param);
    }
    document.querySelector('.logout-button').addEventListener('click', function () { return __awaiter(void 0, void 0, void 0, function () {
        var response, error_1;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    _a.trys.push([0, 2, , 3]);
                    return [4 /*yield*/, fetch("".concat(baseUrl, "/logout"), {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        })];
                case 1:
                    response = _a.sent();
                    if (response.ok) {
                        window.location.href = 'https://team-dog.dokku.cse.lehigh.edu/signin.html';
                    }
                    else {
                        console.error('Logout failed');
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
                        return [4 /*yield*/, fetch(apiUrl)];
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
                            document.querySelector('.user-name').textContent = "".concat(userData.first_name, " ").concat(userData.last_name);
                            document.querySelector('.user-email').textContent = userData.email;
                            document.querySelector('.user-sexual-identity').textContent = userData.sexual_orientation || 'Not specified';
                            document.querySelector('.user-gender-orientation').textContent = userData.gender_identity || 'Not specified';
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
                        return [4 /*yield*/, fetch(apiUrl)];
                    case 2:
                        response = _a.sent();
                        return [4 /*yield*/, response.json()];
                    case 3:
                        data = _a.sent();
                        if (data.mStatus === 'ok' && data.mData) {
                            userData = data.mData;
                            document.querySelector('#edit-first-name').value = userData.first_name || '';
                            document.querySelector('#edit-last-name').value = userData.last_name || '';
                            document.querySelector('#edit-email').value = userData.email || '';
                            document.querySelector('#sexual-identity').value = userData.sexual_orientation || '';
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
                                return [4 /*yield*/, fetch("".concat(baseUrl, "/profile/update"), {
                                        method: 'PUT',
                                        headers: { 'Content-Type': 'application/json' },
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
    function loadPublicProfile(userId) {
        var profileUrl = "".concat(baseUrl, "/profile/").concat(userId);
        fetch(profileUrl)
            .then(function (response) { return response.json(); })
            .then(function (data) {
            if (data.mStatus === "ok" && data.mData) {
                var userData = data.mData;
                var profileSection = document.getElementById('public-profile');
                profileSection.querySelector('.user-name').textContent = userData.first_name + " " + userData.last_name;
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
    // Fetch and display posts
    function fetchPosts() {
        return __awaiter(this, void 0, void 0, function () {
            var response, data, groupedPosts, error_5;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        _a.trys.push([0, 3, , 4]);
                        return [4 /*yield*/, fetch("".concat(baseUrl, "/dashboard"))];
                    case 1:
                        response = _a.sent();
                        return [4 /*yield*/, response.json()];
                    case 2:
                        data = _a.sent();
                        if (data.mStatus === "ok" && data.mData) {
                            groupedPosts = groupPostsByIdeaId(data.mData);
                            displayPosts(groupedPosts);
                        }
                        else {
                            console.error("Unexpected data format:", data);
                        }
                        return [3 /*break*/, 4];
                    case 3:
                        error_5 = _a.sent();
                        console.error("Error fetching posts:", error_5);
                        return [3 /*break*/, 4];
                    case 4: return [2 /*return*/];
                }
            });
        });
    }
    // Group posts by idea_id and collect comments
    function groupPostsByIdeaId(data) {
        var posts = {};
        data.forEach(function (item) {
            var ideaId = item.idea_id;
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
    // Display posts in the feed
    function displayPosts(posts) {
        feedContainer.innerHTML = '';
        posts.forEach(function (post) {
            var postElement = document.createElement('div');
            postElement.classList.add('post');
            postElement.innerHTML = "\n            <div class=\"post-header\">\n                <div class=\"post-user-icon\"></div>\n                <div class=\"post-username\" user-id=\"".concat(post.user.user_id, "\">\n                    ").concat(post.user.first_name, " ").concat(post.user.last_name, "\n                </div>\n            </div>\n            <div class=\"post-body\">").concat(post.idea, "</div>\n            <div class=\"vote-section\">\n                <button class=\"vote-button upvote\" data-id=\"").concat(post.idea_id, "\"><i class=\"fas fa-arrow-up\"></i></button>\n                <span class=\"vote-count upvote-count\">").concat(post.upvote_count, "</span>\n                <button class=\"vote-button downvote\" data-id=\"").concat(post.idea_id, "\"><i class=\"fas fa-arrow-down\"></i></button>\n                <span class=\"vote-count downvote-count\">").concat(post.downvote_count, "</span>\n            </div>\n            <hr>\n            <div class=\"comment-section\">\n                ").concat(post.comments.map(function (comment) { return "\n                    <div class=\"comment\" data-comment-id=\"".concat(comment.comment_id, "\">\n                        <div class=\"post-user-icon\" style=\"float: left; margin-right: 5px;\"></div>\n                        <div class=\"comment-username\" user-id=\"").concat(comment.user.user_id, "\">\n                            ").concat(comment.user.first_name, " ").concat(comment.user.last_name, "\n                        </div>\n                        <div class=\"comment-body\">").concat(comment.body, "</div>\n                        ").concat(comment.user.user_id === userId ? "<button class=\"edit-comment-button\" data-id=\"".concat(comment.comment_id, "\">Edit</button>") : '', "\n                    </div>\n                "); }).join(''), "\n                <input type=\"text\" class=\"comment-input\" placeholder=\"Add a comment...\">\n                <button class=\"add-comment-button\" data-id=\"").concat(post.idea_id, "\">Add</button>\n            </div>\n            ");
            feedContainer.appendChild(postElement);
        });
        addEventListeners();
    }
    // Add event listeners for dashboard view
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
            var messageInput, message, response, error_6;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        messageInput = document.querySelector('.post-input');
                        message = messageInput.value;
                        if (!message.trim()) return [3 /*break*/, 4];
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 3, , 4]);
                        return [4 /*yield*/, fetch("".concat(baseUrl, "/ideas"), {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({ mIdea: message })
                            })];
                    case 2:
                        response = _a.sent();
                        if (response.ok) {
                            messageInput.value = '';
                            fetchPosts();
                        }
                        return [3 /*break*/, 4];
                    case 3:
                        error_6 = _a.sent();
                        console.error("Error posting message:", error_6);
                        return [3 /*break*/, 4];
                    case 4: return [2 /*return*/];
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
    // Function to upvote or downvote an idea
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
    // Function to post a comment on an idea
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
                                    'Content-Type': 'application/json'
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
                                    'Content-Type': 'application/json'
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
