package de.peyrer.indexer;

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

import java.io.IOException;
import java.nio.file.Paths;

public class RelevanceIndexerTest {

    @Test
    public void testIndex() {
        RelevanceIndexer indexer = null;
        try {
            indexer = new RelevanceIndexer("..", "..", "index");
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
        QueryParser parser = new QueryParser("conclusion", analyzer);
        Query query = parser.createBooleanQuery("conclusion", "regulated militia", BooleanClause.Occur.MUST);

        SortField sortField = new SortField("relevance", SortField.Type.DOUBLE, true);
        Sort sortByRelevance = new Sort(sortField);
        ScoreDoc[] hits = new ScoreDoc[0];

        try {
            hits = isearcher.search(query, 3, sortByRelevance).scoreDocs;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = null;
            try {
                hitDoc = isearcher.doc(hits[i].doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assert.assertNotNull(hitDoc);
        }

        try {
            ireader.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(hits.length , 2);
    }
}
