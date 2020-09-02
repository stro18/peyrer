package de.peyrer.indexer;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.indexmodule.Indexmodule;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RelevanceIndexerTest {
    @Mock(name = "argumentRepository")
    ArgumentRepository argumentRepository;

    @InjectMocks
    RelevanceIndexer indexer;
    {
        try {
            indexer = new RelevanceIndexer("src", "main", "resources", "relevanceindex");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndex() {
        Argument argument = new Argument("1", "Abortion is bad", new String[]{"Health risks for the mother"});
        Argument argument2 = new Argument("2", "Abortion is bad", new String[]{"Abortion is murder"});

        Map<String,Double> relevance = new HashMap<>();
        relevance.put("1",0.5);
        relevance.put("2",1.5);

        // Mocking of argumentRepository
        Iterator<Argument> iterator = (Iterator<Argument>) Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(argument).thenReturn(argument2);

        Iterable<Argument> iterable = (Iterable<Argument>) Mockito.mock(Iterable.class);
        Mockito.when(iterable.iterator()).thenReturn(iterator);

        Mockito.when(argumentRepository.readAll()).thenReturn(iterable);

        try {
            indexer.setRelevance(relevance);
            indexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now search the index:
        Directory directory = null;
        DirectoryReader ireader = null;
        try {
            directory = FSDirectory.open(Paths.get(indexer.getIndexPath()));
            ireader = DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IndexSearcher isearcher = new IndexSearcher(ireader);

        AnalyzerModule analyzerModule = new AnalyzerModule();
        // Creates a boolean query that searches for "regulated militia":
        QueryParser parser = new QueryParser("conclusion", analyzerModule.getAnalyzer());
        Query query = null;
        try {
            query = parser.createBooleanQuery("conclusion", analyzerModule.analyze("conclusion", "Abortion is bad"), BooleanClause.Occur.MUST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SortField sortField = new SortField("relevance", SortField.Type.DOUBLE, true);
        Sort sortByRelevance = new Sort(sortField);
        ScoreDoc[] hits = new ScoreDoc[0];

        try {
            hits = isearcher.search(query, 3, sortByRelevance).scoreDocs;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(hits.length , 2);
        Document hitDoc = null;
        try {
            hitDoc = isearcher.doc(hits[0].doc);
            Assert.assertEquals(hitDoc.get("argumentId"), "2");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ireader.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
