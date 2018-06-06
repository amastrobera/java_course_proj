package university;

import java.util.HashSet;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


public class ProfessorTest {


    private Professor p1;
    
    @Before
    public void setUp() {
        p1 = new Professor("eugene", "wise");
        p1.setAddress(new Address("Hardstrasse 201", "8005", "Zurich"));
        
        HashSet<String> allergies = new HashSet<>();
        allergies.add("pomodoro");
        allergies.add("burro");
        p1.setAllergies(allergies);
    }

    @Test 
    public void testString() {
        Assert.assertEquals("professor"+
                            "\neugene wise Hardstrasse 201,8005,Zurich" +
                            "\nallergies: burro,pomodoro",
                            p1.toString());
    }

    
}
