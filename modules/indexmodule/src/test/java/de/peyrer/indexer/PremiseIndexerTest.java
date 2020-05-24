package de.peyrer.indexer;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class PremiseIndexerTest {

    @Test
    public void testIndex(){
        PremiseIndexer indexer = null;
        try {
            indexer = new PremiseIndexer("src", "main", "resources", "premiseindex");
            indexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(indexer);
    }
}
