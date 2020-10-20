# peyrer

This argument search engine was built as part of the Information Retrieval lecture 2020 at the University of Leipzig. 
It uses an adaption of the original PageRank algorithm to rank the arguments of the args.me corpus for each query. To 
build the argument graph, the search engine employs several "matching" methods to find conclusions and premises of 
arguments that contain the same core statement.        
You can find more detailed information about our approach in our paper 'Applying “PageRank for Argument Relevance” to 
the args.me Corpus'.  
  
**Prerequisites:**  
You need to have Docker and Docker-Compose installed on your local machine.  

**Quick start:**  
1. Put your .ndjson files into the directory $YOUR_PROJECT_DIR/argument_ndjson. You may generate these 
files by using the provided json_conversion script.
2. `cd $YOUR_PROJECT_DIR/Docker`  
3. `docker-compose -f docker-compose.base.dev.yml -f docker-compose.mongoseed.dev.yml up -d --build` (loads data into 
database)  
4. `docker-compose -f docker-compose.base.dev.yml -f docker-compose.application.dev.yml up -d` (executes search 
engine for 50 topics and writes results to your out directory; depending on your hardware and the size of your argument 
corpus this can take up to 10 minutes)  

Show logs:  
`docker logs -f docker_searchengine_1`

Quit database server and application:  
`docker-compose -f docker-compose.base.dev.yml -f docker-compose.application.dev.yml down --remove-orphans`  

**Important Notes:**  
* Currently, available 'matching' methods are Identity, TFIDF and BM25. You can change that method and other settings 
inside the .env file.
* This argument search engine is built mainly with Java 11, Apache Lucene and MongoDB.  
* Please pay attention to your runtime environment, as there are duplicate docker files for production environment.
* The use of this argument search engine is not restricted to the args.me corpus. The only condition for the argument
structure is that it must contain a part for the conclusion and one for premises.
