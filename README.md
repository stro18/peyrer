# peyrer

Repository of group Peyrer of IR lecture 2020.  

Quick start:  
1. `cd Docker`  
2. `docker-compose -f docker-compose.base.dev.yml -f docker-compose.mongoseed.dev.yml up -d --build` (loads data into database)  
3. `docker-compose -f docker-compose.base.dev.yml -f docker-compose.application.yml up -d --build` (starts application)