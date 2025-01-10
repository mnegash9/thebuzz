# Phase 2 Sprint 8 - PM Report Template
Use this form to provide your project manager report

## Team Information
### Team Information:

* Number: 24
* Name: "Team-Dog"
* Mentor: <Robert Kilsdonk, rok326@lehigh.edu>
* Weekly live & synchronous meeting:
    * without mentor: Thursdays at 16:00
    * with mentor: Tuesdays at 19:15

### Team Roles:

* Project Manger:  <Evan Qi, evq226@lehigh.edu>
    * PM has since been changed because we are now in phase 2
* Backend developer: <Sean Hazard, sph325@lehigh.edu>
* Admin developer: <Matyas Negash, mkn325@lehigh.edu>
* Web developer: <Monica Fornaszewski, mof226@lehigh.edu>
* Mobile developer: <Mohamed Elhefnawy, mae226@lehigh.edu>

### Essential links for this project:

* Team's Dokku URL(s)
    * <https://team-dog.dokku.cse.lehigh.edu/>
* Team's software repo (bitbucket)
    * <https://bitbucket.org/sml3/cse216_fa24_team_24>
* Team's Jira board
    * <https://cse216-fa24-mkn325.atlassian.net/jira/software/projects/T2/boards/34>


## General questions

1. Did the PM for this week submit this report (If not, why not?)? 

**Answer:** The PM for this week did submit this report. 

2. Has the team been gathering for a weekly, in-person meeting(s)? If not, why not?

**Answer:** The team has been gathering for a weekly, in-person meeting. 

3. Summarize how well the team met the requirements of this sprint.

**Answer:** The whole sprint went well. During this design phase we assigned the different group members each a few artifacts. After we finished the different components, we came together to discusss it and make sure everything was right. The app design required us to iterate on our previous design to support upvotes, downvotes, and comments on ideas. Additionally, we were required to create a profile state where the user can view their own profile and edit parts of it. To ensure we do this smoothly we updated our artifacts to support this (ERD, State Machine, Routes, UI, and adequate testing). All the different group members were able to report being able to balance their time across individual work well. They thought that the allotted time was sufficient to complete everything we needed in this phase in a comfortable manner. 

4. Report on each member's progress (sprint and phase activity completion) – "what is the status?"
   * If incomplete, what challenges are being overcome, how are they being overcome, and by when will the team member be able to finish? 
   * If complete, how do you know everyone completed the work, and at a satisfactory level?

**Answer:** We know that everyone completed the work at a satisfactory level because we came together after all the individual work was done to review each others' work. 

5. Summary of "code review" during which each team member discussed and showed their progress – "how did you confirm the status?"

**Answer:** We discussed our progress multiple times asynchronously (through slack messages) and came together one final time before the deadline to review each others work before submission. This was done with the entire team. We did not make individual videos. We did look at the differernt jira boards. There is not git history for this sprint. We did notify each other as soon as we finished our parts in the artifacts by sending it in the chat. These reviews were scheduled to be all together all at once. 

6. What did you do to encourage the team to be working on phase activities "sooner rather than later"?

**Answer:** I encouraged the team by offering help if needed. 

7. What did you do to encourage the team to help one another?

**Answer:** The team members were more than willing to help each other already. 

8. How well is the team communicating?

**Answer:** The team members have good conversations both getting to know each other and on the project needs. They have a good balance talking about the project and getting to know each other better. All the team members are actively engaging in productive conversations. Communication in our team has always been good. 

9. Discuss expectations the team has set for one another, if any. Please highlight any changes from last week.

**Answer:** The team has set expectations that each memeber completes all their individual work in a timely manner. Unacceptable behaviors include not helping each other out or not listening to each others ideas. We still have frequent asynchronous communication through slack and have regular check-ins in person. We don't have many disagreements, but we talk them out and make agreements easily when we do. 

10. If anything was especially challenging or unclear, please make sure this is [1] itemized, [2] briefly described, [3] its status reported (resolved or unresolved), and [4] includes critical steps taken to find resolution.

**Answer:** Challenge: How to ensure a user can't upvote or down vote unlimited times
Status: Resolved
Description: How do we know if a user already upvoted or downvoted and how do we prevent them from being able to do it again. 
Critical steps to find resolution: Talk over it with the team, do research online, before finally updating the ERD diagram to ensure that there is a unique identifier associated with the upvoting/downvoting

## Project Management

Self-evaluation of PM performance

0. When did your team meet with your mentor, and for how long?

**Answer:** We talked to our mentor asynchronously this week over Slack for guidance.  

1. Describe your use of Jira. Did you have too much detail? Too little? Just enough? Did you implement policies around its use (if so, what were they?)?

**Answer:** Our Jira boards are made with just enough details that make it understandable for the team to know what to do for the week. We make sure that every task is checked over by the team before marking it as complete. 

2. How did you conduct team meetings? How did your team interact outside of these meetings?

**Answer:** We conducted our team meetings in person. Our first meeting was to discuss our individual assignments and our task for the week. Our second meeting was to go over each other's artifacts to ensure that they were of high quality before submitting. 

3. What techniques (daily check-ins/scrums, team programming, timelines, Jira use, slack use, group design exercises) did you use to mitigate risk? Highlight any changes from last week.

**Answer:** We used Jira to assign different tasks. We then use SCRUM to check in with each others progress. We often use Slack to ask and answer questions/problems and use it for guidance from our mentor. 

4. Describe any difficulties you faced in managing the interactions among your teammates. Were there any team issues that arose? If not, what do you believe is keeping thins so constructive?

**Answer:** There were no difficulties that arose. I believe our team is so constructive because we are all open minded and share the same common goal-- to make quality and deliverable software.

5. What is your biggest concern as you think ahead to the next sprint?

**Answer:** I think that handing off the different components to the next member will be challenging to adapt at first. We are planning to have a meeting where the last team member briefs the next one on all the features and implementation of that specific branch. 

6. Describe the most significant obstacle or difficulty your team faced.

**Answer:** The most significant obstacle our team faced was making the ERD diagram for this sprint. This is because by introducing OAuth, there will be a lot of extra states and we had to determine how would we organize that on the backend and how will the tables interconnect with one another. 

7. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next sprint?

**Answer:** Something that we will start doing will be briefing each other on the components so that when it is their turn to manage the lifecycle of it, they will have less complications. We will stop recording our deliverables seperately and combining them together and will instead meet on Zoom for efficiency. We might continue to eliminate our backlog tasks as soon as possible if time/bandwidth/requirements permits. 

8. How well did you estimate time during the early part of the sprint? How did your time estimates change as the sprint progressed?

**Answer:** We all started early by reading the requirements to access how much alloted time we would need and prepare meeting times in advance. As the sprint progress the time estimates stayed the same as we prepared early. 

9. What aspects of the project would cause concern for your customer right now, if any?

**Answer:** We have a solid implementation of the feed from the last sprint with an intuitive user interface. The customer should be concerned that we manage the same usability plus more after the implementation of user sign in.