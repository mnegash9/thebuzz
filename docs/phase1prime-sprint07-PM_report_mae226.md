# Phase 1' Sprint 7 - PM Report Template
Use this form to provide your project manager report for Phase 1' (Prime).

## Team Information [10 points total]

### Team Information:

* Number: 24
* Name: "Team-Dog"
* Mentor: <Robert Kilsdonk, rok326@lehigh.edu>
* Weekly live & synchronous meeting:
    * without mentor: Thursdays at 16:00
    * with mentor: Tuesdays at 19:15

### Team Roles:

* Project Manger: <Mohamed Elhefnawy, mae226@lehigh.edu>
    * No it did not change since we are in phase 1 still, but it will change in the next sprint
* Backend developer: <Monica Fornaszewski, mof226@lehigh.edu>
* Admin developer: <Sean Hazard, sph325@lehigh.edu>
* Web developer: <Evan Qi, evq226@lehigh.edu>
* Mobile developer: <Matyas Negash, mkn325@lehigh.edu>

### Essential links for this project:

* Team's Dokku URL(s)
    * <https://team-dog.dokku.cse.lehigh.edu/>
* Team's software repo (bitbucket)
    * <https://bitbucket.org/sml3/cse216_fa24_team_24>
* Team's Jira board
    * <https://cse216-fa24-mkn325.atlassian.net/jira/software/projects/T2/boards/34>


## Beginning of Phase 1' [20 points total]
Report out on the Phase 1 backlog and any technical debt accrued during Phase 1.

1. What required Phase 1 functionality was not implemented and why?  
    **Answer:** Everything that was required in the rubric and that we had planned for in Sprint 05, was implemented during Sprint 06 so had no missing functionality coming into Sprint 07. All components had full functionality before starting this sprint.


2. What technical debt did the team accrue during Phase 1?
    * List this based on a component-by-component basis, as appropriate.
        * Admin-cli
            1. Tech debt item 1: Tech debt item 1: A large amount of lines of code in this component, the Database file, which needs to be refactored for better code
            2. Tech debt item 2:Tech debt item 2: SQL prepared statements need to be constant variables for database
            3. Tech debt item 3: Needed to fix the fact that everytime the program ran for Admin component, it deleted and remade the table
        * Backend
            1. Tech debt item 1: Needs to refactor code by integrating classes and methods to have a more professional look and better code
        * Mobile FE
            1. Tech debt item 1: Needs to refresh everytime someone likes a post for it to appear on the app. It does hit the database table, but unfortunately does not refresh on its own
            2. Tech debt item 2: Needs to refactor widgets on the app for better modularity
        * Web FE
            1. Tech debt item 1: Needs to refactor to use model architecture in managing states and fetching data


## End of Phase 1' [20 points total]
Report out on the Phase 1' backlog as it stands at the conclusion of Phase 1'.

1. What required Phase 1 functionality still has not been implemented or is not operating properly and why?  
    **Answer:** Thankfully as I stated previously we had all our functionality in place and working in a successful manner. We did not need to add anything as we completed everything we planned and was required by the rubric. 

2. What technical debt remains?
    * List this based on a component-by-component basis, as appropriate.
        * Admin-cli
            1. Tech debt item 1: A large amount of lines of code in this component, the Database file, which needs to be refactored for better code
            2. Tech debt item 2: SQL prepared statements need to be constant variables for database
        * Backend
            1. Tech debt item 1: Needs to refactor code by integrating classes and methods to have a more professional look and better code
        * Mobile FE
            1. Tech debt item 1: Needs to refactor to use model architecture in managing states and fetching data
        * Web FE
            1. Tech debt item 1: Needs to add a delay or pause when auto-refreshing so that it shows up backend changes right away and does not need to be manually refreshed
            2. Tech debt item 2: Needs to refactor widgets on the app for better modularity

3. If there was any remaining Phase 1 functionality that needed to be implemented in Phase 1', what did the PM do to assist in the effort of getting this functionality implemented and operating properly?  

    **Answer:** As I said previously there was no functionality that still needed to be implemented in the beginning of this sprint, therefore I did not need to assist anyone in implementing functionality. We had technical debt, but all of our functionality was successful and able to be done.


4. Describe how the team worked together in Phase 1'. Were all members engaged? Was the work started early in the week or was there significant procrastination?  

    **Answer:** The team did not entirely work together to complete their individual components, but each member worked on their respective branch and fixed their respective components to remove as much technical debt as possible and improve, not add, any of the functionality in the components. We planned on what we were going to improve during the beginning of the sprint, but only started hands-on work halfway through the actual sprint. So there was no significant procrastination since each member followed a method in fixing their code.


5. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next Phase?  
    **Answer:**  
    - **Start:** Especially starting the next sprint 07 we need to start implementing code as early as possible so that we can take into account fixing and refactoring the code. This is instead of implementing code later and not having enough time to fix, update, or refactor the code.
    - **Stop:**  I do not believe that there is anything specifically that we need to stop.
    - **Continue:** I believe that something we needed to continue is something that our team has excelled at so far is our team dynamic. We are communicative, supportive of one another, and honest with each other. It helps us have a clear understanding of where we are as individuals but also a team.

## Role reporting [50 points total]
Report-out on each team members' activity, from the PM perspective (you may seek input where appropriate, but this is primarily a PM related activity).
**In general, when answering the below you should highlight any changes from last week.**

### Back-end
What did the back-end developer do during Phase 1'?
1. Overall evaluation of back-end development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)  
    **Answer:** There was no change from the previous sprint since there was full functionality before starting this prime sprint. How Jira was used is I created tasks that the backend needed to complete, or check again for completion, which when it was checked it was successful since everything had been functioning from before. Then I wanted them to complete the creation of all the appropriate html documentation for the programs that was required by the rubric. And lastly create a list of any backlog item or tasks. All these tasks were completed well and all the artifacts that they needed were created. The list of backlog items were subsequently placed on the Jira Board. 

2. List your back-end's REST API endpoints  
    **Answer:** The REST API endpoints did not change from last time so I will just provide those endpoints again. It is Get for post view, Post to create a new post, Put for both likes and then also for the posts to update them, and lastly Delete for deleting a post.

3. Assess the quality of the back-end code  
    **Answer:** The quality of the code is very well done and did improve from the previous sprint. Instead of the routes being all placed in the App file, they created a new file that was just for the routes that are implemented in our backend. It meets all necessary requirements and improved from the previous sprint.

4. Describe the code review process you employed for the back-end  
    **Answer:** After they completed the improvement of the code they had, they then did a pull request. I looked over the code individually and saw what differences there were between the new code and the previous one. They then walked me through the code as well, and after I saw that everything was implemented correctly and fixed successfully. I approved the pull request and merged into pre-main.

5. What was the biggest issue that came up in code review of the back-end server?  
    **Answer:** There was no issue that came up from the code review. At first I did not understand where some of the code went from the App file, but then I soon realized that it was placed in another file for modularity within the backend component.

6. Is the back-end code appropriately organized into files / classes / packages?  
    **Answer:** Yes the file structure was implemented correctly. All the appropriate files were in their appropriate location. It did not change from the previous sprint. 

7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?  
    **Answer:** Yes, all the dependencies that were in the pom.xml were necessary and were useful for compiling and running the code. There was no unnecessary addition of dependencies. It was not affected by this sprint.

8. Evaluate the quality of the unit tests for the back-end  
    **Answer:** The unit tests were very good and were important to test. What she tested was to see if that if connected it would get the right status code. She also checked that there was a successful response for any requests. There were no additional tests this sprint. So it is the exact same as last sprint. 

9. Describe any technical debt you see in the back-end  
    **Answer:** I would say the only technical debt I see from this component, is maybe refactoring the code, but they already added some modularity for their code, so it is already doing good.


### Admin
What did the admin front-end developer do during Phase 1'?
1. Overall evaluation of admin app development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)  
    **Answer:** The admin was trying to focus on fixing a technical debt issue they had in their code. The code was removing and recreating a table everytime the program was run, so the admin was adamant on resolving the issue. They were successful in their attempt and were able to completely fix their code. As for the Jira board, there was no difference between the tasks for both the backend and admin, as they were just there to check functionality, create the appropriate documentation, and create the list of backlog items. Which they also checked off successfully.

2. Describe the tables created by the admin app  
    **Answer:** The tables created by the admin app did not change from the last sprint, but I will provide the information for what the tables can do. The tables created by the admin were really well done. It was able to tell the id of the post, the message of the post as well as the amount of likes the posts had. Those were all really important for our functionality in our program.

3. Assess the quality of the admin code  
    **Answer:** The quality of the admin code is very good, and has improved from the previous sprint. First, the technical debt that we had accrued from the past sprint was completely fixed in this component. They also cleaned up some of the code and comments they had on their file. 

4. Describe the code review process you employed for the admin app  
    **Answer:** After they completed the improvement of the code they had, they then did a pull request. I looked over the code individually and saw what differences there were between the new code and the previous one. They then walked me through the code as well, and after I saw that everything was implemented correctly and fixed successfully. I approved the pull request and merged into pre-main.

5. What was the biggest issue that came up in code review of the admin app?  
    **Answer:** There was no issue that came up from the code review. Everything was thorough and they walked me through their changes and fixes and I was able to understand everything and everything was functioning perfectly.

6. Is the admin app code appropriately organized into files / classes / packages?  
    **Answer:** Yes the file structure was implemented correctly. All the appropriate files were in their appropriate location. It did not change from the previous sprint. 

7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?  
    **Answer:** Yes, all the dependencies that were in the pom.xml were necessary and were useful for compiling and running the code. There was no unnecessary addition of dependencies. It was not affected by this sprint.

8. Evaluate the quality of the unit tests for the admin app  
    **Answer:** The unit test was very well done, and tested multiple aspects of his code. The code tested adding and dropping a table, adding and deleting a row, and even updating a row. These tests were amazing and it was able to test everything so that we know all the code added working.  There were no additional tests this sprint. So it is the exact same as last sprint. 

9. Describe any technical debt you see in the admin app  
    **Answer:** The main technical debt that stood out to me was the fact that looking at both the App.java and Database.java that was in the admin component, it had over 500 lines of code and needed some refactoring and modularity so that there is not a lot of stress on just one or two files. 


### Web
What did the web front-end developer do during Phase 1'?
1. Overall evaluation of Web development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)  
    **Answer:** The web developer was trying to focus on creating more tests to test their wab UI to see if all the code was functioning well. They were able to create new important tests and test their component well. As for the Jira board, there was no difference between the tasks from the previous roles, as they were just there to check functionality, create the appropriate documentation, and create the list of backlog items. Which they also checked off successfully.

2. Describe the different models and other templates used to provide the web front-end's user interface  
    **Answer:** The models and templates used for the web user interface did not change from the previous sprint, therefore I will provide the information they had implemented last sprint. Web was able to use different templates for the UI that allowed for a richer interaction between the user and the web program. He used “widgets” for each post and added buttons for functionality purposes and it achieved a very desirable interface.

3. Assess the quality of the Web front-end code  
    **Answer:** The quality of the web-frontend code is very good, and has slightly improved from the previous sprint. By the fact that they add more comments to their files as to provide more information for the next person who inherits their code. They also successfully implemented new tests that tested the user interface.

4. Describe the code review process you employed for the Web front-end  
    **Answer:** After they completed the improvement of the code they had, they then did a pull request. I looked over the code individually and saw what differences there were between the new code and the previous one. They then walked me through the code as well, and after I saw that everything was implemented correctly and fixed successfully. I approved the pull request and merged into pre-main.

5. What was the biggest issue that came up in code review of the Web front-end?  
    **Answer:** There was no issue that came up from the code review. Everything was thorough and they walked me through their changes and fixes and I was able to understand everything and everything was functioning perfectly.

6. Is the Web front-end code appropriately organized into files / classes / packages?  
    **Answer:** Yes the file structure was implemented correctly. All the appropriate files were in their appropriate location. It did not change from the previous sprint. 

7. Are the dependencies in the `package.json` file appropriate? Were there any unexpected dependencies added to the program?  
    **Answer:** Yes that was something he made sure to show me and I made sure to check, the package.json had everything appropriate and all the dependencies were necessary and implemented correctly. The only real change was they added the “typedoc” in the package. json for the script, but it was well integrated.

8. Evaluate the quality of the unit tests for the Web front-end   
    **Answer:** The unit test was very well done, and tested multiple aspects of his code. The tests made sure to check that with no posts, there would be no posts available for viewing and if there was a post that had a message it would show up on the web. Everything was successful and tested for the most part the most important aspect of web front end. He also added testing to see that the like function was working well. He also checked for the functionality of deleting a post, and he wanted to also see if the user interface is able to sort the posts correctly. All of Jasmine tests were successful and important. 

9. Describe any technical debt you see in the Web front-end  
    **Answer:** The main technical debt that stood out to me was the fact that all the code was in the same files and there was no refactoring and the code was not entirely modular, so that needs to be improved for the future.


### Mobile
What did the mobile front-end developer do during Phase 1'?
1. Overall evaluation of Mobile development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)  
    **Answer:** The mobile developer was trying to focus on fixing a technical debt issue they had in their code. The code was unable to refresh whenever there was a like and there needed to be a manual refresh to show the updated likes. They were successful in their attempt and were able to completely fix their code. They also were trying to create more unit testing to test their user interface. As for the Jira board, there was no difference between the tasks for both the backend and admin, as they were just there to check functionality, create the appropriate documentation, and create the list of backlog items. Which they also checked off successfully.

2. Describe the activities that comprise the Mobile app  
    **Answer:** Mobile app is able to create posts, delete posts, like posts infinitely and is able to reload the app. The developer was also able to make an automatic refresh to update information right away without having to do it manually.

3. Assess the quality of the Mobile code  
    **Answer:** The quality of the mobile code is very good, and has slightly improved from the previous sprint. They removed unnecessary code and files that were not affecting the code. They also expanded on the user interface to make it better. They also successfully implemented new tests that tested the user interface.

4. Describe the code review process you employed for the Mobile front-end  
    **Answer:** After they completed the improvement of the code they had, they then did a pull request. I looked over the code individually and saw what differences there were between the new code and the previous one. They then walked me through the code as well, and after I saw that everything was implemented correctly and fixed successfully. I approved the pull request and merged into pre-main.

5. What was the biggest issue that came up in code review of the Mobile front-end?  
    **Answer:** There was no real issue that came up during the code review, the only thing was that when showing me the functionality of the code, we found a problem that the app had that needed to be fixed for the next sprint. The problem was that the app refreshed before the changes to backend were applied, which caused new posts to take awhile for it to show up. 

6. Is the Mobile front-end code appropriately organized into files / classes / packages?  
    **Answer:** Yes it has the appropriate organization structure and all the files were correctly created and implemented. There was even modularity within the code so that not all the code is just stuffed into one document. There was no difference in the structure of the files, except for the removal of the number_word_pair.dart file in the models.

7. Are the dependencies in the `pubspec.yaml` (or build.gradle) file appropriate? Were there any unexpected dependencies added to the program?  
    **Answer:** Yes, only the appropriate dependencies are in the pubspec.yaml. He included only the necessary dependencies and even added the dependencies for mockito for the unit testing. The only real difference between this sprint and the previous one is that both the title and the description of the app was updated to more accurately reflect our team. 

8. Evaluate the quality of the unit tests for the Mobile front-end here  
    **Answer:** The unit test was very well done, and tested multiple aspects of his code. The main aspects that we wanted to check for was that we can view the posts and create posts, so he tested those two aspects. To make sure that when the app is opened and connected that it shows a list of all the posts and is able to create a post within the app. He also added testing to see that the like function was working as expected. The same goes for the title of the app and checking for the presence of widgets.

9. Describe any technical debt you see in the Mobile front-end here  
    **Answer:** The main technical debt that stood out to me was the fact that all the code was in the same files and there was no refactoring and the code was not entirely modular except for the removal of the one unnecessary file. That needs to be improved for the future. They also have to fix the problem with automatic refresh happening before the changes to the backend. A delay needs to be implemented.


### Project Management
Self-evaluation of PM performance
1. When did your team meet with your mentor, and for how long?  
    **Answer:** We met with our mentor Robert during the recitation. We asked any questions we had and he was able to explain and guide us to having a better sprint. We had questions regarding the documentation and where they were supposed to be. It was not for a long time, maybe 10 minutes, but it was able to make us understand everything completely.

2. Describe your use of Jira.  Did you have too much detail?  Too little?  Just enough? Did you implement policies around its use (if so, what were they?)?  
    **Answer:** We made sure to have a balance between too much detail and not enough detail. We made sure that everything that was required in the rubric, that were the subtopics, were grouped together to create tasks. For example checking for functionality. It was made this way to ensure the team members were not stressed any clear enough for everyone to know what they needed to work on.

3. How did you conduct team meetings?  How did your team interact outside of these meetings?  
    **Answer:** We just follow the same structure as our previous team meetings. We do a quick check-in with everyone to see where they are mentally and phsyically. Then we speak about any concerns we had regarding the previous sprint. Then we look forward to the next sprint, dividing the work, and asking any last questions.

4. What techniques (daily check-ins/scrums, team programming, timelines, Jira use, group design exercises) did you use to mitigate risk? Highlight any changes from last week.  
    **Answer:** We use two useful resources that are both as important as each other to help mitigate risks. Those resources are Slack and the Jira Board. The Jira board, provides insight with the risk level labels that are attached and are used as reminders on what tasks we have that are higher or lower risk. Obviously higer risk tasks are to be done quicker to ensure we are able to complete the sprint. The second resource is that of Slack which is our main form of communication that we use to provide support, ask questions, and provide help to one another to reduce the chances of risks occuring.

5. Describe any difficulties you faced in managing the interactions among your teammates. Were there any team issues that arose? If not, what do you believe is keeping things so constructive?  
    **Answer:** Thankfully there were no team issues that arose, and I did not have to manage much of the interactions since we all have a similar support system and understand respecting each other.

6. Describe the most significant obstacle or difficulty your team faced.  
    **Answer:** The most siginificant challenge we faced as a team was not horrible, but I understand that we were slighly rushing towards the end, and especially when creating the video, having some of the work being done later than usual cause for some concern and worry in which we were working hard towards the end of the sprint to fix our technical debt.

7. What is your biggest concern as you think ahead to the next phase of the project? To the next sprint? What steps can the team take to reduce your concern?  
    **Answer:** There is no really big concern besides the main idea that since we are changing roles this sprint, it is slighly stressful knowing what code we will inherit and how we will expand on that code to integrate authentication.

8. How well did you estimate time during the early part of the phase?  How did your time estimates change as the phase progressed?  
    **Answer:** We knew that we needed to fix our technical debt and to give more time to make sure we were able to fix and remove our technical debt. We could have begun earlier to ensure even better fixes but our resolutions were good and in the end we fixed alot of our technical debt. 

9. What aspects of the project would cause concern for your customer right now, if any?  
    **Answer:** There are no aspects that should currently cause any concern since we are on track in our implementation of the code that we should have. The only thing that a customer might want is more functionality, but we also need to make sure that we are not coding beyond the specifications.
