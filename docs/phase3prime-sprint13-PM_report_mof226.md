# Phase 3' Sprint 13 - PM Report 

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


## Beginning of Phase 3' [20 points total]
Report out on the Phase 3 backlog and any technical debt accrued during Phase 3.

1. What required Phase 3 functionality was not implemented and why? 

    * Web FE
        1. Editing comments:
            * Why: We were not able to implement editing comments during phase 2 and never had enough time to implement it once phase 3 started.
        2. Uploading files:
            * Why: Web was unable to finish this functionality, so it was added to the backlog.
        3. Attaching media in comments remain unimplemented:
            * why: Web ran out of time during the week to spend implementing this piece of functionality.
    * Backend: 
        1. using http-response headers to aid caching:
            * why: Backend achieved full functionality so this is something extra to improve the code.
    * Mobile FE
        1. Displaying images in comments:
            * why: Did not have enough time during sprint 12 to implement this part.

    * Admin-cli: 
        1. invalidating links and being able to remove files permanently 
            * why: Admin also did not have enough time to implement these components during sprint 12.

2. What technical debt did the team accrue during Phase 3?
    
    * Admin-cli
        1. Tech debt item 1: need for refactoring 
        2. Tech debt item 2: need for more unit tests
    * Backend
        1. Tech debt item 1: need for refactoring 
    * Mobile FE
        1. Tech debt item 1: need for refactoring 
    * Web FE
        1. Tech debt item 1: Needs more classes
        2. Tech debt item 2: Needs unit tests


## End of Phase 3' [20 points total]
Report out on the Phase 3' backlog as it stands at the conclusion of Phase 3'.

1. What required Phase 3 functionality still has not been implemented or is not operating properly and why?
    
    * Web FE
        1. Editing comments:
            * Why: We were not able to implement editing comments during phase 2 and never had enough time to implement it once phase 3 started.
        2. Viewing media:
            * Why: We are able to upload to the database but viewing on web is not working.
        3. Attaching media in comments remain unimplemented:
            * why: Web ran out of time during the week to spend implementing this piece of functionality.
    * Backend: Full functionality, but can make routes more uniform.
    * Mobile FE: full functionality, but can add more unit tests.
    * Admin-cli: full functionality, but can have a faster way of removing an image from the database and drive.

2. What technical debt remains?
    
    * Admin-cli
        1. Tech debt item 1: need for refactoring 
        2. Tech debt item 2: need for more unit tests
    * Backend
        1. Tech debt item 1: need for refactoring 
    * Mobile FE
        1. Tech debt item 1: need for refactoring 
    * Web FE
        1. Tech debt item 1: some errors 
        2. Tech debt item 2: Needs unit tests
        3. Tech debt item 3: needs documentation

3. If there was any remaining Phase 3 functionality that needed to be implemented in Phase 3', what did the PM do to assist in the effort of getting this functionality implemented and operating properly?

    I maintained close communication with each team member, encouraged early starts on tasks, and organized additional code review sessions. I also set clear expectations via Jira tasks, and ensured any issues were quickly addressed by bringing them to the entire team’s attention.

4. Describe how the team worked together in Phase 3'. Were all members engaged? Was the work started early in the week or was there significant procrastination?

    All team members remained engaged. I encouraged early starts by assigning tasks at the beginning of the sprint and checking in mid-week. This reduced last-minute rushes. Slack discussions and code review sessions helped identify and resolve issues sooner rather than later.

5. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next Phase (if there were one)?
    * Start: Scheduling a retrospective at the end of each sprint to identify and address process issues.
    * Continue: Maintaining transparent, frequent communication and using Jira to track tasks clearly.

## Role reporting [50 points total]
Report-out on each team members' activity, from the PM perspective (you may seek input where appropriate, but this is primarily a PM related activity).
**In general, when answering the below you should highlight any changes from last week.**

### Back-end
What did the back-end developer do during Phase 3'?
1. Overall evaluation of back-end development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)

The Backend completed all required functionalities this sprint. Jira was used effectively, with tasks clearly defined and marked done upon completion. Code reviews and functionality tests verified progress.

2. List your back-end's REST API endpoints

(No changes from last sprint)

/login (POST), /logout (POST), /gdrive (GET), /ideas (GET, POST), /ideas/{id} (GET), /ideas/{id}/upvote (PUT), /ideas/{id}/downvote (PUT), /ideas/{id}/comments (POST), /comments/{comment_id} (PUT), /comments/{idea_id} (GET), /dashboard (GET), /profile/{user_id} (GET), /profile/{id} (PUT).

3. Assess the quality of the back-end code

The code remains well-structured and maintainable. The developer addressed earlier feedback, improving readability and adhering to best practices.

4. Describe the code review process you employed for the back-end

A pull request was reviewed, focusing on clarity and functionality. The backend developer and I collaborated to resolve any concerns.

5. What was the biggest issue that came up in code review of the back-end server?

No significant new issues came up. The main feedback was about adding more test coverage, which remains on the backlog.

6. Is the back-end code appropriately organized into files / classes / packages?

Yes, the backend is very well organzied.

7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?

Dependencies remain appropriate. No unexpected additions were made this sprint.

8. Evaluate the quality of the unit tests for the back-end

Although additional unit tests are still needed, the existing ones are of good quality. Additional test needed are tests for idea posting and retrieval.

9. Describe any technical debt you see in the back-end

Technical debt includes the need for more tests and making the routes more uniform.


### Admin
What did the admin front-end developer do during Phase 3'?
1. Overall evaluation of admin app development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)

The Admin developer completed all functionalities this sprint. Jira tasks were well managed, and completion was verified via code review and demonstration.

2. Describe the tables created by the admin app

No new tables this sprint; the existing tables for comment media, idea media, and links remain as previously documented.
commentmediatable: comment_media_id, comment_id, file_name, mime_type, drive_file_id, access_time, validcommentmedia
ideamediatable: idea_media_id, idea_id, file_name, mime_type, drive_file_id, access_time, validideamedia
There were also tables that were edited (ideatable and commenttable), which now include an idea_link field and a comment_link field.

3. Assess the quality of the admin code

The code is stable and satisfies all required functionalities. It’s modular and clear.

4. Describe the code review process you employed for the admin app

The code was reviewed with a pull request. I ensured that newly implemented functionalities worked correctly.

5. What was the biggest issue that came up in code review of the admin app?

No major issues came up. The main ongoing task is to add more unit tests to increase coverage.

6. Is the admin app code appropriately organized into files / classes / packages?

Yes, the code structure is well organized.

7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?

All dependencies are appropriate and necessary. No unexpected dependencies were introduced this sprint.

8. Evaluate the quality of the unit tests for the admin app

While some tests exist, more are needed to cover the newly implemented functionalities thoroughly.

9. Describe any technical debt you see in the admin app

The main technical debt is the need for additional unit tests to ensure long-term reliability as well as having a faster way of removing an image from the database and the drive itself.

### Web
What did the web front-end developer do during Phase 3'?
1. Overall evaluation of Web development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)

The Web component progressed well, implementing most functionalities. Tasks were managed in Jira, and completion was verified through code reviews. However, editing comments, viewing media, and attaching media in comments remain unfinished.

2. Describe the different models and other templates used to provide the web front-end's user interface

The Web uses HTML5 to represent user profiles, ideas, and comments. Dynamic rendering enables partial data updates and responsiveness.

3. Assess the quality of the Web front-end code

The code is structured and functional for the features that are working, though unfinished functionalities and tests show that improvement is still needed.

4. Describe the code review process you employed for the Web front-end

I reviewed the pull request from web, focusing on adherence to requirements and documenting any functionality that was missing.

5. What was the biggest issue that came up in code review of the Web front-end?

The main issue remains the incomplete functionalities (editing comments, viewing media, attaching media) and the lack of unit tests.

6. Is the Web front-end code appropriately organized into files / classes / packages?

Yes, the code is organized into appropriate files and folders.

7. Are the dependencies in the `package.json` file appropriate? Were there any unexpected dependencies added to the program?

Dependencies remain appropriate. No unexpected additions were made.

8. Evaluate the quality of the unit tests for the Web front-end 

Unit tests are still absent and remain a backlog item. Improved coverage is needed.

9. Describe any technical debt you see in the Web front-end

Technical debt includes missing unit tests, and completing the unimplemented functionalities.

### Mobile
What did the mobile front-end developer do during Phase 3'?
1. Overall evaluation of Mobile development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)

The Mobile developer completed all required functionalities this sprint. Jira was used effectively, and tasks were updated regularly. Verification happened with code review and demonstration.

2. Describe the activities that comprise the Mobile app

Activities include logging in, viewing and interacting with ideas and comments, and now fully implemented media functionality.

3. Assess the quality of the Mobile code

The code is modular, clear, and fully functional. The only backlog item involves adding more unit tests.

4. Describe the code review process you employed for the Mobile front-end

I reviewed the pull request and had the mobile developer demonstrate the app to ensure all functionalities worked correctly.

5. What was the biggest issue that came up in code review of the Mobile front-end?

There were no major new issues. The mobile code now meets all functional requirements.

6. Is the Mobile front-end code appropriately organized into files / classes / packages?

Yes, it is well-organized into files, classes, and packages.

7. Are the dependencies in the `pubspec.yaml` (or build.gradle) file appropriate? Were there any unexpected dependencies added to the program?

Dependencies remain appropriate with no unexpected changes.

8. Evaluate the quality of the unit tests for the Mobile front-end here

Some tests exist, but the backlog calls for additional unit tests to improve coverage and ensure long-term stability.

9. Describe any technical debt you see in the Mobile front-end here

The only technical debt is the need for more unit tests to have more test coverage.

### Project Management
Self-evaluation of PM performance
1. When did your team meet with your mentor, and for how long?

We met with our mentor during the scheduled recitation for about 30 minutes, discussing the progress we've made.

2. Describe your use of Jira.  Did you have too much detail?  Too little?  Just enough? Did you implement policies around its use (if so, what were they?)?

Jira was used effectively to track tasks. The level of detail was appropriate and I assigned items without overwhelming the team. The policy remained that every new task or backlog item must be documented, assigned, and moved to “Done” upon completion.

3. How did you conduct team meetings?  How did your team interact outside of these meetings?

Meetings were held weekly in-person, with Slack and a group chat used for daily communication.

4. What techniques (daily check-ins/scrums, team programming, timelines, Jira use, group design exercises) did you use to mitigate risk? Highlight any changes from last week.

I continued using Jira for early assignment of tasks and frequent check-ins. Encouraging earlier starts and running multiple small code reviews prevented scrambling at the end of the sprint.

5. Describe any difficulties you faced in managing the interactions among your teammates. Were there any team issues that arose? If not, what do you believe is keeping things so constructive?

Interactions remained positive, with no major conflicts. Clear communication, personal responsibility, and respect for one another’s time and work kept things constructive.

6. Describe the most significant obstacle or difficulty your team faced.

The largest obstacle remained the same for this sprint, the redeployments to Dokku, which was time consuming.

7. What is your biggest concern as you think of the future of the project? To the next sprint, if there were one? What steps can the team take to reduce your concern?

If the project continued, finishing the remaining Web functionalities and improving test coverage across all components would be my biggest concern. To reduce this concern, the team could allocate more time to test planning and make sure functionality gets completed earlier in the sprint.

8. How well did you estimate time during the early part of the phase?  How did your time estimates change as the phase progressed?

Time estimates were mostly accurate, but some testing and functional tasks took longer than anticipated.

9. What aspects of the project would cause concern for your customer right now, if any?

The customer might be concerned about the missing Web functionalities (editing comments, viewing, and attaching media) and the limited unit test coverage.