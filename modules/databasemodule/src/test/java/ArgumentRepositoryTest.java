import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentRepositoryTest {

    @Test
    public void testReadById(){
        ArgumentRepository argumentRepository = new ArgumentRepository();

        Argument argument = argumentRepository.readById("S96f2396e-A2f68e3d2");

        Assert.assertEquals(argument.id, "S96f2396e-A2f68e3d2");
    }

    @Test
    public void testCreate(){
        ArgumentRepository argumentRepository = new ArgumentRepository();
        Argument argumentWrite = new Argument("1", "Abortion", new String[]{"Every woman has the right to decide", "Every life counts"});

        argumentRepository.create(argumentWrite);

        Argument argumentRead = argumentRepository.readById("1");

        Assert.assertEquals(argumentRead.id, argumentWrite.id);
    }
}
