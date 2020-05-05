import com.mongodb.client.MongoCollection;
import com.peyrer.indexmodule.PremiseIndexer;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;


public class IndexerTest {

    @Test
    public void testDatabaseConnection() {
        PremiseIndexer indexer = new PremiseIndexer();

        Document d = indexer.coll.find().first();

        Assert.assertNotNull(d);
    }
}
