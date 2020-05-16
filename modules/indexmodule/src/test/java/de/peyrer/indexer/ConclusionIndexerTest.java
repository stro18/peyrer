package de.peyrer.indexer;

import de.peyrer.model.Argument;
import de.peyrer.model.ArgumentIterable;
import de.peyrer.repository.ArgumentRepository;
import de.peyrer.repository.IArgumentRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

public class ConclusionIndexerTest {

    @Test
    public void testIndex(){
        ConclusionIndexer indexer = null;

        try {
            indexer = new ConclusionIndexer("conclusionindex");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mocking of argumentRepository
        Argument argument = new Argument("1", "Abortion is good", new String[]{"Every woman has the right to decide for herself"});
        Argument argument2 = new Argument("2", "Abortion is bad", new String[]{"Abortion is murder"});

        Iterator<Argument> iterator = (Iterator<Argument>) Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(argument).thenReturn(argument2);

        Iterable<Argument> iterable = (Iterable<Argument>) Mockito.mock(Iterable.class);
        Mockito.when(iterable.iterator()).thenReturn(iterator);

        indexer.argumentRepository = Mockito.mock(IArgumentRepository.class);
        Mockito.when(indexer.argumentRepository.readAll()).thenReturn(iterable);

        try {
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

        // Creates a boolean query that searches for "regulated militia":
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("conclusionText", analyzer);
        Query query = parser.createBooleanQuery("conclusionText", "Abortion is bad", BooleanClause.Occur.MUST);

        ScoreDoc[] hits = new ScoreDoc[0];

        try {
            hits = isearcher.search(query,10).scoreDocs;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(1 , hits.length);

        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = null;
            try {
                hitDoc = isearcher.doc(hits[i].doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assert.assertEquals(hitDoc.get("argumentId"), "2");
        }

        try {
            ireader.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
