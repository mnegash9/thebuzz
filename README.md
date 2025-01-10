# CSE 216 Team Repo
This is a team repository.  It is intended for use during phase 1 and beyond.

## Details
- Semester: Fall 2024
- Team Number: 24
- Team Name: team_24
- Bitbucket Repository: https://bitbucket.org/sml3/cse216_fa24_team_24
- Jira Board: https://cse216-fa24-mkn325.atlassian.net/jira/software/projects/T2/boards/34
- Backend URL: https://team-dog.dokku.cse.lehigh.edu/

## Team Members, Roles, and links to jira & repos:
- Admin: <Mohamed Elhefnawy, mae226@lehigh.edu> 
- Mobile: <Evan Qi, evq226@lehigh.edu>
- Frontend: <Sean Hazard, sph325@lehigh.edu>
- Backend: <Matyas Negash, mkn325@lehigh.edu>
- Project Manager: <Monica Fornaszewski, mof226@lehigh.edu>  

## Brief Description of Release
This release includes all the functionality from last phase with some additional features. Users can post "ideas" and can upvote/downvote on other's ideas. OAuth is 
implemented for unique user identification and profiles. Users are also able to upload links and files and attach them to their ideas and comments.
Missing functionality: Editing posted comments on web frontend, media viewing on web frontend, and attaching media in comments on web.
## Instructions on building and running backend locally and on dokku
1. Go to the root of the repository
2. To run locally, go to the command line and run:
```DATABASE_URI="[your database uri]" mvn exec:java```
3. To run on Dokku, go to the command line and run:
```git push dokku deploy-dokku:main```
## Instructions on building and running mobile locally
1. Go to the mobile folder of the repository
2. To run locally, go to the command line and run:
```flutter run```
## Instructions on building and running web locally
1. Go to the web folder of the repository
2. To run locally, go to the command line and run:
```sh deploy-local.sh```
## Instructions on building and running admin-cli locally
1. Go to the admin-cli folder of the repository
2. To run the cli program, go to the command line and run:
```DATABASE_URI="[your db uri]" mvn exec:java```

## Developer Documentation Links
[Admin-Cli Documentation](admin-cli/javadoc/)  
[Backend Documentation](backend/docs/)  
[Mobile Documentation](mobile/flutter/the_buzz/documentation/api/)  
[Web Documentation](web/docs/)  