This script converts JSON files from https://webis.de/data/args-me-corpus.html
to NDJSON (newline delimited JSON) files. This is useful for importing the
argument objects to a MongoDB database.

Dependencies:
  - Jackson JSON Parser (https://github.com/FasterXML/jackson)

Compilation:
  - javac -cp /path/to/jackson-core-2.x.x.jar:/path/to/jackson-databind-2.x.x.jar:/path/to/jackson-annotations-2.x.x.jar ParseArgumentJson.java

Usage:
  - java -cp /path/to/jackson-core-2.x.x.jar:/path/to/jackson-databind-2.x.x.jar:/path/to/jackson-annotations-2.x.x.jar ParseArgumentJson /path/to/file.json
  - this will create a file /path/to/file.ndjson containing all argument
    objects from the input file without the surrounding JSON object and
    seperated by newlines

