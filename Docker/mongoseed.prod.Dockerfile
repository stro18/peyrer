FROM mongo:4.2

COPY ./ /

CMD mongoimport --host mongo --db peyrer --collection args --file /debateorg.ndjson.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
CMD mongoimport --host mongo --db peyrer --collection args --file /debatepedia.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
CMD mongoimport --host mongo --db peyrer --collection args --file /debatewise.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
CMD mongoimport --host mongo --db peyrer --collection args --file /idebate.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
CMD mongoimport --host mongo --db peyrer --collection args --file /parliamentary.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id