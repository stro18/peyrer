FROM mongo:4.2

COPY ./ /

CMD mongoimport --host mongo --db peyrer --collection args --file /debatepedia.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id