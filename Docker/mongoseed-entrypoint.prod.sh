#!/bin/sh

mongo --host mongo --eval 'db.args.createIndex({id: 1})' --username root --password example --authenticationDatabase admin peyrer
mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/debateorg.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/debatepedia.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/debatewise.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/idebate.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/parliamentary.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id