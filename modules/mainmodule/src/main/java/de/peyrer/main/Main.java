package de.peyrer.main;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import de.peyrer.indexmodule.Indexmodule;
import de.peyrer.indexmodule.InvalidSettingValueException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Main {

    public static void main (String[] args)
    {
        try {
            Indexmodule indexmodule = new Indexmodule();
            String indexPath = indexmodule.getIndexPath();
            if(indexPath == null){
                try {
                    indexmodule.indexWithRelevance();
                } catch (InvalidSettingValueException e) {
                    e.printStackTrace();
                }
                indexmodule.getIndexPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] titles;
        try {
            titles = Main.readTitlesFromXml("topics/topicsTest.xml");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for(String title : titles){
            System.out.println(title);
        }
    }

    private static String[] readTitlesFromXml(String ... directory) throws Exception {
        String path = Main.buildSourcePath(directory);

        File inputFile = new File(path);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);

        doc.getDocumentElement().normalize();
        NodeList topicList = doc.getElementsByTagName("topic");

        String[] titles = new String[topicList.getLength()];
        for (int temp = 0; temp < topicList.getLength(); temp++) {
            Element topic;

            if(topicList.item(temp) instanceof Element){
                topic = (Element) topicList.item(temp);
            }else{
                throw new Exception("Input file has wrong format!");
            }

            NodeList titleList = topic.getElementsByTagName("title");

            titles[temp] = titleList.item(0).getTextContent();
        }

        return titles;
    }

    private static String buildSourcePath(String ... directory){
        Path wantedIndexPath = Paths.get(System.getProperty("user.dir"));

        while(directory.length > 0 && directory[0].equals("..")){
            wantedIndexPath = wantedIndexPath.getParent();
            directory = Arrays.copyOfRange(directory, 1, directory.length);
        }

        return Paths.get(wantedIndexPath.toString(), directory).toString();
    }
}
