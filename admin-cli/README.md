# Admin - CLI 

### Database Schema
The database schema includes a five tables as follows:
 
 * User TABLE
    * user_id (SERIAL PRIMARY KEY)
    * first_name (VARCHAR)
    * last_name (VARCHAR)
    * email (VARCHAR)
    * gender_identity (VARCHAR)
    * sexual_orient (VARCHAR)
    * note (VARCHAR)
    * active_account (BOOLEAN)

 CREATE TABLE IF NOT EXISTS UserTable (user_id SERIAL PRIMARY KEY, first_name VARCHAR(32) NOT NULL, last_name VARCHAR(32) NOT NULL, email VARCHAR(48) NOT NULL, gender_identity VARCHAR(32), sexual_orient VARCHAR(32), note VARCHAR(512), active_account BOOLEAN DEFAULT true, UNIQUE (email));

 * Idea TABLE
    * idea_id (SERIAL PRIMARY KEY)
    * user_id (INTEGER FOREIGN KEY)
    * idea (VARCHAR <= 512 Characters)
    * idea_valid (BOOLEAN)

CREATE TABLE IF NOT EXISTS IdeaTable (idea_id SERIAL PRIMARY KEY, user_id int NOT NULL, idea VARCHAR(512) NOT NULL, upvotes INT NOT NULL DEFAULT 0, downvotes INT NOT NULL DEFAULT 0, idea_valid BOOLEAN DEFAULT true, FOREIGN KEY (user_id) REFERENCES UserTable(user_id));

 * Comment TABLE
    * comment_id (SERIAL PRIMARY KEY)
    * body (VARCHAR)
    * idea_id (INTEGER FOREIGN KEY)
    * user_id (INTEGER FOREIGN KEY)

CREATE TABLE IF NOT EXISTS CommentTable (comment_id SERIAL PRIMARY KEY, body VARCHAR(512) NOT NULL, idea_id INT NOT NULL, user_id INT NOT NULL, FOREIGN KEY(idea_id) REFERENCES IdeaTable(idea_id), FOREIGN KEY (user_id) REFERENCES UserTable(user_id));

* Upvote TABLE
    * upvote_id (SERIAL PRIMARY KEY)
    * user_id (INTEGER FOREIGN KEY)
    * idea_id (INTEGER FOREIGN KEY)

CREATE TABLE IF NOT EXISTS UpvoteTable (upvote_id SERIAL PRIMARY KEY, user_id INT NOT NULL, idea_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES UserTable(user_id), FOREIGN KEY(idea_id) REFERENCES IdeaTable(idea_id), CONSTRAINT one_like UNIQUE(user_id, idea_id));

* Downvote TABLE
    * downvote_id (SERIAL PRIMARY KEY)
    * user_id (INTEGER FOREIGN KEY)
    * idea_id (INTEGER FOREIGN KEY)
    
CREATE TABLE IF NOT EXISTS DownvoteTable (downvote_id SERIAL PRIMARY KEY, user_id INT NOT NULL, idea_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES UserTable(user_id), FOREIGN KEY(idea_id) REFERENCES IdeaTable(idea_id), CONSTRAINT one_dislike UNIQUE(user_id, idea_id));

 * The database uses SQL Prepared Statements to safely change the data in the database.