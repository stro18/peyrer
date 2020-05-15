import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ArgumentRepositoryTest {

    @Test

    public void test_A_ReadById(){
        ArgumentRepository argumentRepository = new ArgumentRepository();

        Argument argument = argumentRepository.readById("S96f2396e-A2f68e3d2");

        Assert.assertEquals(argument.id, "S96f2396e-A2f68e3d2");
    }

    @Test
    public void test_B_Create(){
        ArgumentRepository argumentRepository = new ArgumentRepository();
        Argument argumentWrite = new Argument("1", "Abortion", new String[]{"Every woman has the right to decide", "Every life counts"});

        argumentRepository.create(argumentWrite);

        Argument argumentRead = argumentRepository.readById("1");

        Assert.assertEquals(argumentRead.id, argumentWrite.id);
    }

    @Test
    public void test_C_FindAll(){
        ArgumentRepository argumentRepository = new ArgumentRepository();

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
        ArgumentRepository argumentRepository = new ArgumentRepository();
        Argument argumentInitial = argumentRepository.readById("1");
        Argument argumentUpdated = new Argument("2", "Abortion", new String[]{"Every woman has the right to decide", "Every life counts", "Abortion is murder"});

        argumentRepository.replace(argumentInitial,argumentUpdated);

        Assert.assertNotEquals(argumentInitial.id,argumentUpdated.id);
    }

    @Test
    public void test_E_UpdatePagerank(){
        ArgumentRepository argumentRepository = new ArgumentRepository();
        String id = "2";

        double value = 3.0;

        argumentRepository.updatePageRank(id,value);

        Assert.assertEquals(argumentRepository.readById(id).pageRank,value,3.0);
    }

    @Test
    public void test_F_UpdateRelevance(){
        ArgumentRepository argumentRepository = new ArgumentRepository();
        String id = "2";

        double value = 5.0;

        argumentRepository.updateRelevance(id,value);

        Assert.assertEquals(argumentRepository.readById(id).relevance,value,5.0);
    }

    @Test
    public void test_G_Delete() {

        ArgumentRepository argumentRepository = new ArgumentRepository();

        Argument argument = argumentRepository.readById("2");

        Argument argumentDelete= argumentRepository.delete(argument);

        Assert.assertEquals(argumentDelete.id,argument.id);
    }

}

