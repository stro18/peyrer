package de.peyrer.indexer;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class RelevanceIndexerTest {

    @Test
    public void testIndexPrem(){
        RelevanceIndexer indexer = null;
        try {
            indexer = new RelevanceIndexer("index");
            indexer.indexPrem();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(indexer);
    }
}
