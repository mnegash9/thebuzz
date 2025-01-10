# Phase 3 Sprint 12 - PM Report 

## Team Information [10 points total]

### Team Information:

* Number: 24
* Name: Team dog
* Mentor: <Robert Kilsdonk, rok326@lehigh.edu>
* Weekly live & synchronous meeting:
    * without mentor: Thursdays at 4:00
    * with mentor: Tuesdays at 7:15

### Team Roles:

* Project Manger: <Monica Fornaszewski, mof226@lehigh.edu>
    * Has this changed from last week (if so, why)? No, we are still in the same phase.
* Backend developer: <Matyas Negash, mkn325@lehigh.edu>
* Admin developer: <Mohamed Elhefnawy, mae226@lehigh.edu>
* Web developer: <Sean Hazard, sph325@lehigh.edu>
* Mobile developer: <Evan Qi, evq226@lehigh.edu>

### Essential links for this project:

* Team's Dokku URL(s)
    * <https://team-dog.dokku.cse.lehigh.edu/>
* Team's software repo (bitbucket)
    * <https://bitbucket.org/sml3/cse216_fa24_team_24>
* Team's Jira board
    * <https://cse216-fa24-mkn325.atlassian.net/jira/software/projects/T2/boards/34>

Screenshot of Jira board:
![Alt text](<Screenshot 2024-12-02 at 11.25.56 PM.png>)


## General questions [15 points total]

1. Did the PM for this week submit this report (If not, why not?)? 
    Yes

2. Has the team been gathering for a weekly, in-person meeting(s)? If not, why not?
    Yes

3. Summarize how well the team met the requirements of this sprint.

    There was a lot of progress since the design phase. Every person on the team worked to implement all the critera from our design phase as well as the functionality described in the rubric for the implementation phase. After asking each member of the team how their work was progressing throughout the sprint, each one of them said they were making positive progress. Most of the functionality for this sprint was achieved but there are still some things missing that we've addded to the backlog.

4. Report on each member's progress (sprint and phase activity completion) – "what is the status?"
    * If incomplete, what challenges are being overcome, how are they being overcome, and by when will the team member be able to finish?
    * If complete, how do you know everyone completed the work, and at a satisfactory level?

    Backend: Backend was able to achieve full functionality. Authentication, authorization, caching, links, and files were all implemented.
    Admin: Admin also made a lot of progress, but had some trouble with invalidating links and being able to remove files permanently. Admin is currently working through these and hopes to get it complete this week.
    Web: Web made good progress but still needs to work on the implementation of uploading files, which is added to the backlog.
    Mobile: Mobile was able to achieve a lot of the functionality, but still needs to work on displaying images in comments.
    I was able to learn the status of the team by setting up a meeting with everyone and having a code review and discussion.


5. Summary of "code review" during which each team member discussed and showed their progress – "how did you confirm the status?"

    We met once for our weekly meeting and we met again on zoom for the code review. It was done together as a team and we all discussed the current functionality together. There was a lot of activty in both slack and our group chat in messages throughout the entirety of the sprint. Each member would consistenlty send updates on their progress in these group chats, so everyone was aware of where we were at in regards to the functionality. It also made it known when a code review was needed and they were planned accordingly. Multiple small code reviews happened during the week, and then we had a final one before submission.

6. What did you do to encourage the team to be working on phase activities "sooner rather than later"?

I made sure that everyone knew the tasks they needed to complete by assigning them in the Jira board. I also followed up regularly in either slack or imessage to see how everyone was progressing.

7. What did you do to encourage the team to help one another?

I encouraged the team to send any questions or bugs they were having in our group chat, so that we could help each other solve any issues. I also encouraged the team to have consistent and clear communication.

8. How well is the team communicating?

    The team’s communication remains strong and constructive. Conversations include both project updates and friendly interactions, creating a collaborative atmosphere. Slack is actively used, and response times are quick.

9. Discuss expectations the team has set for one another, if any. Please highlight any changes from last week.

    The expectations remain the same from last week. Attendance is required at meetings, updates on progress are expected frequently, and regular communication is expected as well.

10. If anything was especially challenging or unclear, please make sure this is [1] itemized, [2] briefly described, [3] its status reported (resolved or unresolved), and [4] includes critical steps taken to find resolution.
    * Challenge: 1
        * Status: unresolved
        * Description: One thing that was especially challenging during this sprint was testing. Since we deployed our web and backend on dokku together, it is very difficult to test the backend and web because our oAuth relies on cookies, and cookies does not work when running locally. 
        * Critical steps taken to find resolution: The steps taken were not very easy, since backend and web had to keep redeploying to dokku in order to test.

        

11. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next sprint?
    
    Something I suggest the team start doing in the next sprint is starting earlier and using slack more than our personal group chat.


## Role reporting [75 points total, 15 points each (teams of 4 get 15 free points)
Report-out on each role, from the PM perspective.
You may seek input where appropriate, but this is primarily a PM related activity.

### Back-end

1. Overall evaluation of back-end development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
2. List your back-end's REST API endpoints
    * /login (POST), /logout (POST), /gdrive (GET), /ideas (GET), /ideas/{id} (GET), /ideas (POST), /ideas/{id}/upvote (PUT), /ideas/{id}/downvote (PUT),  
    /ideas/{id}/comments (POST), /comments/{comment_id} (PUT), /dashboard (GET), /profile/{user_id} (GET), /profile/{id} (PUT), /comments/{idea_id} (GET) 

3. Assess the quality of the back-end code
The quality of the backend code is very good. It has all the functionality and it is very organized and readable. 

4. Describe the code review process you employed for the back-end
I had the backend send a pull request and I looked at different aspects of the code such as readability, functionality, and other potential issues.

5. What was the biggest issue that came up in code review of the back-end server?
There weren't any major issues, but we did come up with some minor things that needed to be addressed in the backlog, such as more unit tests and using http-response headers to aid caching.

6. Is the back-end code appropriately organized into files / classes / packages?
Yes, The code is very well organized.

7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
Yes, the dependencies in the pom.xml are appropriate. There were two newly added dependencies for this sprint (google-api-services-drive and xmemcached). These were important for the new functionality of this phase.

8. Evaluate the quality of the unit tests for the back-end
The backend made a few tests that tested the validation and access to the google drive. These were quality tests.

9. Describe any technical debt you see in the back-end
Some technical debt I see in the backend is the need for some more refactoring. 


### Admin

1. Overall evaluation of admin app development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
The Admin app made good progress during this sprint. All the necessary tables were created and for every additional task that was completed, the admin updated the jira board. I would be able to see which of the tasks were completed by chacking the jira board regularly.

2. Describe the tables created by the admin app
For this sprint, some new tables were added in order to handle files, links, and access time.
Here are the tables and the corresponding fields:
commentmediatable: comment_media_id, comment_id, file_name, mime_type, drive_file_id, access_time, validcommentmedia
ideamediatable: idea_media_id, idea_id, file_name, mime_type, drive_file_id, access_time, validideamedia
There were also tables that were edited (ideatable and commenttable), which now include an idea_link field and a comment_link field.

3. Assess the quality of the admin code
The quality of the admin code is good. There are many different classes and the code is very organized. The code also satisfies most of the functionality for this phase.

4. Describe the code review process you employed for the admin app
The admin sent me a pull request and we reviewed the code together. Any issues that came up were adressed and backlog items were listed on the jira board.

5. What was the biggest issue that came up in code review of the admin app?
The biggest issue in the code review was assessing the functionality of invalidating links and removing files permanently, which was not completed for this sprint. But the admin added these items to the backlog and plans on completing them for the next sprint.

6. Is the admin app code appropriately organized into files / classes / packages?
Yes, the admin app has many different files, classes and packages to keep the code modular. 

7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
Yes, all the dependencies in the pom.xml are necessary and there are no dependedncies that don't add any value.

8. Evaluate the quality of the unit tests for the admin app
The admin app added one additional unit test to test adding links and files to ideas and comments. This unit test is good because it tests the new functionality of this phase. 

9. Describe any technical debt you see in the admin app
Some technical debt includes the need for some refactoring and additional tests. 

### Web

1. Overall evaluation of Web development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
The progress on web frontend was very good. There was complete transparancy on the completion of tasks because the tasks I added to the jira board were moved by the web developer to the "done" column when complete. 

2. Describe the different models and other templates used to provide the web front-end's user interface
Html templates were used for the dashboard, which handles dynamic data, allowing the UI to be responsive and updated with new data. 

3. Assess the quality of the Web front-end code
The web code is well structured and follows the conventions of modularity. 

4. Describe the code review process you employed for the Web front-end
The web developer sent me a pull request and we reviewed the code together and worked out any issues that needed to be addressed.

5. What was the biggest issue that came up in code review of the Web front-end?
The biggest issue was the functionality aspect because the web was missing functionality for uploading files.

6. Is the Web front-end code appropriately organized into files / classes / packages?
Yes, the web code is very organized and separated into the appropriate files and classes.

7. Are the dependencies in the `package.json` file appropriate? Were there any unexpected dependencies added to the program?
Yes, the dependencies are appropriate and did not change from last phase. No additional dependencies were needed.

8. Evaluate the quality of the unit tests for the Web front-end
Unit tests were not completed for this sprint. They were added to the backlog.

9. Describe any technical debt you see in the Web front-end
The technical debt I saw includes the need for unit tests and creating more classes for organization.

### Mobile

1. Overall evaluation of Mobile development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
The mobile app had good progression this sprint. I added the tasks that needed to be done to the jira board and the mobile developer updated their tasks as they progressed.

2. Describe the activities that comprise the Mobile app
The activities that make up the app include logging in, viewing comments/ideas, and being able to upload links or files to them.

3. Assess the quality of the Mobile code
The quality of the code is good. It is very modular and organized.

4. Describe the code review process you employed for the Mobile front-end
A pull request was sent to me and I reviewed the code with the mobile developer. We discussed what needed to be discussed and addressed things that needed to be resolved.

5. What was the biggest issue that came up in code review of the Mobile front-end?
The biggest issue was the lack of a certain functionality, rendering of images in comments.

6. Is the Mobile front-end code appropriately organized into files / classes / packages?
Yes, the code is very well organized and separated into the appropriate files and classes.

7. Are the dependencies in the `pubspec.yaml` (or build.gradle) file appropriate? Were there any unexpected dependencies added to the program?
Yes, all the dependencies are appropriate and needed. There are no unexpected dependencies that were added.

8. Evaluate the quality of the unit tests for the Mobile front-end here
Unit tests were created to test caching, and they were well written and good quality tests.

9. Describe any technical debt you see in the Mobile front-end here
There is still room for improvement in the refactoring of the code as well as additional unit tests. 

### Project Management
Self-evaluation of PM performance

1. When did your team meet with your mentor, and for how long?
We met with our mentor on Tuesday during recitation for about a half hour.

2. Describe your use of Jira.  Did you have too much detail?  Too little?  Just enough? Did you implement policies around its use (if so, what were they?)?
Jira was used effectively, with tasks clearly documented and assigned. Most tasks were moved to "Done" by the assigned member.

3. How did you conduct team meetings?  How did your team interact outside of these meetings?
Our weekly meeting time was planned as usual, with reminders sent in advance. Outside of meetings, communication occurred on Slack and a zoom call.

4. What techniques (daily check-ins/scrums, team programming, timelines, Jira use, group design exercises) did you use to mitigate risk? Highlight any changes from last week.
I made sure to assign jira tasks early on, I checked in on the team regularly, and I planned different times to debug as a team together.

5. Describe any difficulties you faced in managing the interactions among your teammates. Were there any team issues that arose? If not, what do you believe is keeping things so constructive?
The interactions among my teamates are very good. We have good cummunication with each other and we are all very responsible with the tasks that we need to get done. 

6. Describe the most significant obstacle or difficulty your team faced.
The most significant difficulty we faced is figuring out how we can test the frontend and backend. There was no easy solution except for deploying to dokku each time.

7. What is your biggest concern as you think ahead to the next phase of the project? To the next sprint?
My biggest concern is being able to get all the backlog items completed and with thorough testing.

8. How well did you estimate time during the early part of the phase?  How did your time estimates change as the phase progressed?
The team accurately estimated the time needed for most tasks but underestimated debugging and testing time for file/image handling. Our time estimates changed toward the end of the sprint when we realized some things would need to be added to the backlog. 

9. What aspects of the project would cause concern for your customer right now, if any?
The missing functionalities (file uploads, image rendering, permanent file deletion) could concern the customer, but the team has plans to address these in the next sprint.