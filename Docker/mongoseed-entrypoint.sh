#!/bin/sh

CMD mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/debateorg.ndjson.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
CMD mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/debatepedia.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
CMD mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/debatewise.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
CMD mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/idebate.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id
CMD mongoimport --host mongo --db peyrer --collection args --file /argument_ndjson/parliamentary.ndjson --username root --password example --authenticationDatabase admin --mode=upsert  --upsertFields=id