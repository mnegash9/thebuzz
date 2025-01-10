"use strict";
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
