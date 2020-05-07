import com.peyrer.indexmodule.PremiseIndexer;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;


public class IndexerTest {

    @Test
    public void testDatabaseConnection() {
        PremiseIndexer indexer = new PremiseIndexer(new ArgumentRepository());

        Argument a = indexer.repository.readById("S96f2396e-A2f68e3d2");

        Assert.assertNotNull(a);
    }
}
