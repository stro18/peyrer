import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.bson.BsonDocument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ArgumentRepositoryTest {

    private ArgumentRepository argumentRepository;

    @Before
    public void setup(){
        this.argumentRepository = new ArgumentRepository("localhost");
    }

    @Test
    public void test_A_ReadById(){
        Argument argument = argumentRepository.readById("S96f2396e-A2f68e3d2");

        Assert.assertEquals(argument.id, "S96f2396e-A2f68e3d2");
    }

    @Test
    public void test_B_Create(){
        Argument argumentWrite = new Argument("1", "Abortion", new String[]{"Every woman has the right to decide", "Every life counts"});

        argumentRepository.create(argumentWrite);

        Argument argumentRead = argumentRepository.readById("1");

        Assert.assertEquals(argumentRead.id, argumentWrite.id);
    }

    @Test
    public void test_C_FindAll(){
        Argument[] arguments = new Argument[2];
        int count = 0;
        for(Argument argument: argumentRepository.readAll()){
            arguments[count] = argument;
            count++;

            if(count==2){
                break;
            }
        }

        Assert.assertNotEquals(arguments[0].id, arguments[1].id);
    }

    @Test
    public void test_D_Replace(){
        Argument argumentInitial = argumentRepository.readById("1");
        Argument argumentUpdated = new Argument("2", "Abortion", new String[]{"Every woman has the right to decide", "Every life counts", "Abortion is murder"});

        argumentRepository.replace(argumentInitial,argumentUpdated);

        Assert.assertNotEquals(argumentInitial.id,argumentUpdated.id);
    }

    @Test
    public void test_E_UpdatePagerank(){
        String id = "2";

        double value = 3.0;

        double returnedValue = argumentRepository.updatePageRank(id,value);

        Assert.assertEquals(returnedValue,value,3.0);
    }

    @Test
    public void test_F_UpdateRelevance(){
        String id = "2";

        double value = 5.0;

        double returnedValue = argumentRepository.updateRelevance(id,value);

        Assert.assertEquals(returnedValue,value,5.0);
    }

    @Test
    public void test_G_GetNumberOfPremises(){
        ArgumentRepository argumentRepository = new ArgumentRepository();
        String id = "2";

        int returnedValue = argumentRepository.getNumberofPremises(id);

        Assert.assertEquals(returnedValue,3);
    }

    @Test
    public void test_F_Delete() {

        ArgumentRepository argumentRepository = new ArgumentRepository();

        Argument argument = argumentRepository.readById("2");

        Argument argumentDelete= argumentRepository.delete(argument);

        Assert.assertEquals(argumentDelete.id,argument.id);
    }
}

