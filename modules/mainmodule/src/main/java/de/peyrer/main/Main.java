package de.peyrer.main;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import de.peyrer.indexmodule.Indexmodule;
import de.peyrer.model.Argument;
import de.peyrer.querybuilder.DocValueFieldQueryBuilder;
import de.peyrer.querybuilder.FeatureFieldQueryBuilder;
import de.peyrer.querybuilder.IQueryBuilder;
import de.peyrer.retrievalmodule.RetrievalModule;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Main {

    private static final boolean FEATURE_FIELD_QUERY = false;
    private static final float QUERY_COEFFICIENT = 1.0f;
    private static final String OUTPUT_FILE_PATH = "./out.trec";
    private static final String OUTPUT_TAG = "peyrer";
    private static final int RESULT_AMOUNT = 1000;

    public static void main (String[] args)
    {
        String indexPath;
        try {
            Indexmodule indexmodule = new Indexmodule();
            indexPath = indexmodule.getIndexPath();
            if(indexPath == null){
                indexmodule.indexWithRelevance();
                indexmodule.getIndexPath();
            }
        } catch (IOException e) {
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

            TopDocs results = null;
            try {
                results = retrievalModule.getResults(topic.getElementsByTagName("title").item(0).getTextContent(), RESULT_AMOUNT);
            } catch (ParseException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
                return;
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
                return;
            }
            ScoreDoc[] sortedResults = results.scoreDocs;
            Arrays.sort(sortedResults, new CompareScoreDocs());
            int topicId;
            try {
                topicId = Integer.parseInt(topic.getElementsByTagName("number").item(0).getTextContent());
            } catch (NumberFormatException e) {
                System.err.println("Input file has wrong format!");
                System.exit(-1);
                return;
            }

            for(int j = 0; j < sortedResults.length; j++) {
                try {
                    writeResultToOutputFile(writer,
                            topicId,
                            retrievalModule.getArgument(sortedResults[j].doc),
                            j,
                            sortedResults[j].score,
                            OUTPUT_TAG);
                } catch (IOException e) {
                    System.err.println(String.format("An error occurred while writing to the output file: %s", e.getMessage()));
                    writer.close();
                    System.exit(-1);
                    return;
                }
            }
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

    private static void writeResultToOutputFile(PrintWriter writer, int topicId, String argId, int rank, float score, String tag) throws IOException {
        writer.printf("%d Q0 %s %d %f %s",
                topicId,
                argId,
                rank,
                score,
                tag);
    }

    private static class CompareScoreDocs implements Comparator<ScoreDoc> {
        @Override
        public int compare(ScoreDoc doc1, ScoreDoc doc2) {
            if(doc1.score > doc2.score) return -1;
            else if(doc1.score < doc2.score) return 1;
            else return 0;
        }
    }
}
