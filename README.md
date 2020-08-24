# peyrer

Repository of group Peyrer of IR lecture 2020.  

You can adapt settings (e.g. graph building method) of this application in the `.env` file or you can use the provided 
default settings.  

Quick start:  
1. `cd Docker`  
2. `docker-compose -f docker-compose.base.dev.yml -f docker-compose.mongoseed.dev.yml up -d --build` (loads data into database)  
3. `docker-compose -f docker-compose.base.dev.yml -f docker-compose.application.yml up -d --build` (starts application)  

Show logs:  
`docker logs -f docker_searchengine_1`

Quit database server and application:  
`docker-compose -f docker-compose.base.dev.yml -f docker-compose.application.yml down`  

Please pay attention to your runtime environment. There are duplicate docker files for production environment.  
