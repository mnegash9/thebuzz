| Route | HTTP Method | Purpose | Request Format |
|-------|-------------|---------|----------------|
| /posts | GET | Retrieve all posts | N/A |
| /posts | POST | Create a new post | `{ "content": string }` |
| /posts/{id} | GET | Retrieve a specific post | N/A |
| /posts/{id}/like | PUT | Like a post | `{ "id": int }` |
| /posts/{id} | PUT | update a post | `{ "id": int, "content": string }` |
