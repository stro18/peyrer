import com.mongodb.client.MongoCollection;
import de.peyrer.connection.MongoConnector;
import org.bson.BsonDocument;
import org.junit.Assert;
import org.junit.Test;

public class ConnectorTest {

    @Test
    public void testGetCollection(){
        MongoConnector connector = new MongoConnector();
        MongoCollection<BsonDocument> collection = connector.getCollection("args");

        BsonDocument document = collection.find().first();

        Assert.assertNotNull(document);
    }
}
