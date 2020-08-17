package de.peyrer.graph;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.indexmodule.Indexmodule;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;

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

    /*
    @Test
    public void testMatch(){
        Iterable<Map<String,String>> result = null;
        IndexWriter iw = null;
        IndexSearcher searcher = null;
        Map<String,String> premise = new HashMap<String,String>();
        premise.put("1","Abortion is not murder");
        premise.put("2","Abortion is Murder");
        premise.put("3","Abortion is the murder");

        matcher.setArgumentId("4");
        matcher.setStringToMatch("Abortion is murder");

        TopDocs topDocs = new TopDocs(null,null);
        topDocs.scoreDocs = new ScoreDoc[3];
        topDocs.scoreDocs[0] = new ScoreDoc(1,1.0f);
        topDocs.scoreDocs[1] = new ScoreDoc(2,1.0f);
        topDocs.scoreDocs[2] = new ScoreDoc(3,1.0f);

        Document doc1 = new Document();
        doc1.add(new StoredField("argumentId", 1));
        doc1.add(new StoredField("premiseId", Integer.toString(1)));
        doc1.add(new TextField("premiseText", premise.get("1"), Field.Store.YES));
        Document doc2 = new Document();
        doc2.add(new StoredField("argumentId", 2));
        doc2.add(new StoredField("premiseId", Integer.toString(2)));
        doc2.add(new TextField("premiseText", premise.get("2"), Field.Store.YES));
        Document doc3 = new Document();
        doc3.add(new StoredField("argumentId", 3));
        doc3.add(new StoredField("premiseId", Integer.toString(3)));
        doc3.add(new TextField("premiseText", premise.get("3"), Field.Store.YES));

        try {
            Directory dir = FSDirectory.open(Paths.get("testPath"));
            Analyzer analyzer = (new AnalyzerModule().getAnalyzer());
            iw = new IndexWriter(dir, new IndexWriterConfig(analyzer));
            iw.addDocument(doc1);
            iw.addDocument(doc2);
            iw.addDocument(doc3);
            iw.close();
            DirectoryReader iReader = DirectoryReader.open(dir);
            searcher = new IndexSearcher(iReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            matcher.setDirectoryName("testPath");
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
    }
    */

}
