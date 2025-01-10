| Route | HTTP Method | Purpose | Request Format | Response Format |
|-------|-------------|---------|----------------|-----------------|
| /ideas | GET | Retrieve all ideas | N/A | `{ "mStatus": "ok", "mData": [list of ideas] }` |
| /ideas | POST | Create a new idea | `{ "content": string }` | `{ "mStatus": "ok", "mData": "Idea created successfully" }` |
| /ideas/{id} | GET | Retrieve a specific idea | N/A | `{ "mStatus": "ok", "mData": { idea details } }` |
| /ideas/{id} | DELETE | Delete an idea | N/A | `{ "mStatus": "ok", "mData": "deleted row" }` |
| /ideas/{id}/vote | POST | Up-votes or down-votes an idea | `{ "vote_type": string }` | `{ "mStatus": "ok", "mData": "Vote registered" }` |
| /ideas/{id}/comments | POST | Adds a new comment to an idea | `{ "content": string }` | `{ "mStatus": "ok", "mData": "Comment added" }` |
| /ideas/{id}/comments/{comment_id} | PUT | Allows user to edit their comment | `{ "content": string }` | `{ "mStatus": "ok", "mData": "Comment updated" }` |
| /profile | GET | Retrieves the user’s profile | N/A | `{ "mStatus": "ok", "mData": { profile details } }` |
| /profile/update | PUT | Updates the user's profile | `{ "field": string }` | `{ "mStatus": "ok", "mData": "Profile updated" }` |
| /users/{user_id}/profile | GET | Retrieves public profile for a specific user | N/A | `{ "mStatus": "ok", "mData": { public profile details } }` |
| /login | GET | Serve the login page with the OAuth link | N/A | `{"mStatus": "ok", "mData": "Redirect to OAuth server."}` |
| /auth/callback | GET | Handle the callback from Google and check token | N/A | `{"mStatus": "ok", "mData": "Session established"}` |
| /dashboard | GET | Serve the main content of the website | N/A | `{"mStatus": "ok", "mData": "Content loaded"}` |
| /logout | POST | Ends the user’s session | N/A | `{ "mStatus": "ok", "mData": "User logged out successfully" }` |

