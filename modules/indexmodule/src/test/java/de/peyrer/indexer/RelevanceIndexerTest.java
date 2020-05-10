package de.peyrer.indexer;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class RelevanceIndexerTest {

    @Test
    public void testIndexPrem(){
        PremiseIndexer indexer = null;
        try {
            indexer = new PremiseIndexer("index");
            indexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(indexer);
    }
}
