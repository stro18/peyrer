package de.peyrer.graph;

import de.peyrer.analyzermodule.AnalyzerModule;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PhraseMatcherTest {

    @InjectMocks
    PhraseMatcher matcher;
    {
        matcher = new PhraseMatcher();
    }

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMatch() {
        Iterable<Map<String,String>> result = null;
        IndexWriter iw = null;
        Map<String,String> premise = new HashMap<String,String>();
        premise.put("1","Abortion is not murder");
        premise.put("2","Abortion is Murder");
        premise.put("3","Abortion is the murder");

        AnalyzerModule analyzerModule = new AnalyzerModule();
        matcher.setArgumentId("4");

        try {
            matcher.setStringToMatch(analyzerModule.analyze("premiseText", "Abortion is murder"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc1 = new Document();
        doc1.add(new StoredField("argumentId", 1));
        doc1.add(new StoredField("premiseId", Integer.toString(1)));
        try {
            doc1.add(new TextField("premiseText", new AnalyzerModule().analyze("premiseText",premise.get("1")), Field.Store.YES));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc2 = new Document();
        doc2.add(new StoredField("argumentId", 2));
        doc2.add(new StoredField("premiseId", Integer.toString(2)));
        try {
            doc2.add(new TextField("premiseText", new AnalyzerModule().analyze("premiseText",premise.get("2")), Field.Store.YES));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc3 = new Document();
        doc3.add(new StoredField("argumentId", 3));
        doc3.add(new StoredField("premiseId", Integer.toString(3)));
        try {
            doc3.add(new TextField("premiseText", new AnalyzerModule().analyze("premiseText",premise.get("3")), Field.Store.YES));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Directory dir = FSDirectory.open(Paths.get(System.getProperty("user.dir") + "/src/main/resources/testindex"));
            Analyzer analyzer = (new AnalyzerModule().getAnalyzer());
            iw = new IndexWriter(dir, new IndexWriterConfig(analyzer));
            iw.addDocument(doc1);
            iw.addDocument(doc2);
            iw.addDocument(doc3);
            iw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            matcher.setDirectoryName(System.getProperty("user.dir") + "/src/main/resources/testindex");
            result = matcher.match();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<Map<String,String>> indexIterator = result.iterator();
        //first map of iterable should be not null and should contain argumentId = 2
        Assert.assertEquals(indexIterator.hasNext(),true);
        Map<String,String> firstMap = indexIterator.next();
        Assert.assertNotEquals(firstMap.get("argumentId"),"1");
        Assert.assertEquals(firstMap.get("argumentId"),"2");
        //second map of iterable should be not null and should contain argumentId = 3
        Assert.assertEquals(indexIterator.hasNext(),true);
        Map<String,String> secondMap = indexIterator.next();
        Assert.assertEquals(secondMap.get("argumentId"),"3");
        try {
            Directory dir = FSDirectory.open(Paths.get(System.getProperty("user.dir") + "/src/main/resources/testindex"));
            Analyzer analyzer = (new AnalyzerModule().getAnalyzer());
            iw = new IndexWriter(dir, new IndexWriterConfig(analyzer));
            iw.deleteAll();
            iw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
