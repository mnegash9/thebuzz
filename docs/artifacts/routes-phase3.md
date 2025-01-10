| Route | HTTP Method | Purpose | Request Format | Response Format |
|-------|-------------|---------|----------------|-----------------|
| /ideas | GET | Retrieve all ideas | N/A | `{ "mStatus": "ok", "mData": [list of ideas] }` |
| /ideas | POST | Create a new idea | `{ "content": string, "link": "string (optional)", "fileUrl": "string (optional)" }` | `{ "mStatus": "ok", "mData": "Idea created successfully" }` |
| /ideas/{id} | GET | Retrieve a specific idea | N/A | `{ "mStatus": "ok", "mData": { idea details } }` |
| /ideas/{id}/upvote | PUT | Up-votes or down-votes an idea | N/A | `{ "mStatus": "ok", "mData": ""incremented upvotes for row " + idx" }` |
| /ideas/{id}/downvote | PUT | Up-votes or down-votes an idea | N/A | `{ "mStatus": "ok", "mData": ""incremented upvotes for row " + idx" }` |
| /ideas/{id}/comments | POST | Adds a new comment to an idea | `{ "mIdea": string, "link": "string (optional)", "fileUrl": "string (optional)" }` | `{ "mStatus": "ok", "mData": "Comment added" }` |
| /comments/{comment_id} | PUT | Allows user to edit their comment | `{ "body": string }` | `{ "mStatus": "ok", "mData": "Comment updated" }` |
| /comments/{idea_id} | GET | Retrieve comments for specific idea | N/A | `{ "mStatus": "ok", "mData": { comments } }` |
| /profile/{user_id} | GET | Retrieves the user’s profile | N/A | `{ "mStatus": "ok", "mData": { profile details } }` |
| /profile/{id} | PUT | Updates the user's profile | `{ "field": string }` | `{ "mStatus": "ok", "mData": "Profile updated" }` |
| /dashboard | GET | Serve the main content of the website | N/A | `{"mStatus": "ok", "mData": [dashboard elements]}` |
| /login | POST | authenticate the user | `{"jwtToken": string}` | `{"mStatus": "ok", "mData": "Login succesful"}` |
| /logout | POST | Ends the user’s session | N/A | `{ "mStatus": "ok", "mData": "Logged out successfully" }` |
| /upload | POST | Allows user to upload a file/image | `{"data": data object}` | `{"mStatus": "ok","mData": {"fileUrl": "string"}}` |

