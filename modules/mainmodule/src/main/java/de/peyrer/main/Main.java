package de.peyrer.main;

import de.peyrer.indexmodule.Indexmodule;
import de.peyrer.querybuilder.DocValueFieldQueryBuilder;
import de.peyrer.querybuilder.FeatureFieldQueryBuilder;
import de.peyrer.querybuilder.IQueryBuilder;
import de.peyrer.retrievalmodule.Result;
import de.peyrer.retrievalmodule.Results;
import de.peyrer.retrievalmodule.RetrievalModule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import de.peyrer.indexmodule.InvalidSettingValueException;

public class Main {

    private static final boolean FEATURE_FIELD_QUERY = Boolean.parseBoolean(System.getenv("FEATURE_FIELD_QUERY"));
    private static final float QUERY_COEFFICIENT = Float.parseFloat(System.getenv("QUERY_COEFFICIENT"));
    private static final String OUTPUT_FILE_PATH = String.format("/out/out_%s.trec", java.time.ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    private static final String OUTPUT_TAG = System.getenv("OUTPUT_TAG");
    private static final int RESULT_AMOUNT = Integer.parseInt(System.getenv("RESULT_AMOUNT"));
    private static final boolean INDEX_ARGUMENTS = Boolean.parseBoolean(System.getenv("INDEX_ARGUMENTS"));

    public static void main (String[] args)
    {
        String indexPath;
        try {
            Indexmodule indexmodule = new Indexmodule();
            indexPath = indexmodule.getIndexPath();
            if(indexPath == null || INDEX_ARGUMENTS){
                indexmodule.indexWithRelevance();
                indexPath = indexmodule.getIndexPath();
            }
        } catch (IOException | InvalidSettingValueException e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        IQueryBuilder queryBuilder = (FEATURE_FIELD_QUERY) ? new FeatureFieldQueryBuilder(QUERY_COEFFICIENT) : new DocValueFieldQueryBuilder(QUERY_COEFFICIENT);
        RetrievalModule retrievalModule = null;
        try {
            retrievalModule = new RetrievalModule(queryBuilder, indexPath);
        } catch (IOException e) {
            System.err.println(String.format("Cannot open index: %s", e.getMessage()));
            System.exit(-1);
            return;
        }

        NodeList topics;
        try {
            topics = Main.readTopicsFromXml("topics/topicsTest.xml");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        PrintWriter writer;
        try {
            writer = new PrintWriter(OUTPUT_FILE_PATH);
        } catch (IOException e) {
            System.err.println(String.format("Cannot open output file ('%s') for writing: %s", OUTPUT_FILE_PATH, e.getMessage()));
            System.exit(-1);
            return;
        }

        for(int i = 0; i < topics.getLength(); i++) {
            Element topic;
            if(topics.item(i) instanceof Element){
                topic = (Element) topics.item(i);
            } else {
                System.err.println("Input file has wrong format!");
                System.exit(-1);
                return;
            }
            if(topic.getElementsByTagName("title").getLength() != 1 || topic.getElementsByTagName("number").getLength() != 1) {
                System.err.println("Input file has wrong format!");
                System.exit(-1);
                return;
            }

            Results results;
            try {
                results = retrievalModule.getResults(topic.getElementsByTagName("title").item(0).getTextContent(), RESULT_AMOUNT);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
                return;
            }

            int topicId;
            try {
                topicId = Integer.parseInt(topic.getElementsByTagName("number").item(0).getTextContent());
            } catch (NumberFormatException e) {
                System.err.println("Input file has wrong format!");
                System.exit(-1);
                return;
            }

            writeResultsToOutputFile(writer, topicId, results);
        }
        writer.close();
    }

    private static NodeList readTopicsFromXml(String ... directory) throws Exception {
        String path = Main.buildSourcePath(directory);

        File inputFile = new File(path);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);

        doc.getDocumentElement().normalize();
        NodeList topicList = doc.getElementsByTagName("topic");

        return topicList;
    }

    private static String buildSourcePath(String ... directory){
        Path wantedIndexPath = Paths.get(System.getProperty("user.dir"));

        while(directory.length > 0 && directory[0].equals("..")){
            wantedIndexPath = wantedIndexPath.getParent();
            directory = Arrays.copyOfRange(directory, 1, directory.length);
        }

        return Paths.get(wantedIndexPath.toString(), directory).toString();
    }

    private static void writeResultsToOutputFile(PrintWriter writer, int topicId, Results results) {
        for(Result result : results) {
            try {
                writeResultToOutputFile(writer,
                        topicId,
                        result,
                        OUTPUT_TAG);
            } catch (IOException e) {
                System.err.println(String.format("An error occurred while writing to the output file: %s", e.getMessage()));
                writer.close();
                System.exit(-1);
                return;
            }
        }
    }

    private static void writeResultToOutputFile(PrintWriter writer, int topicId, Result result, String tag) throws IOException {
        writer.printf("%d Q0 %s %d %f %s\n",
                topicId,
                result.getArgument().id,
                result.getRank(),
                result.getScore(),
                tag);
    }
}
