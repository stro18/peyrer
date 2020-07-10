#!/bin/sh

mongo --host mongo --db peyrer --eval 'db.args.createIndex({id: 1})' --username root --password example --authenticationDatabase admin
mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/debatepedia.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id