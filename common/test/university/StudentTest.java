package university;

import java.util.HashSet;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


public class StudentTest {


    private Student p1;
    
    @Before
    public void setUp() {
        p1 = new Student("pietro", "quarta");
        
        p1.setPhone("987654321");
        p1.setAddress(new Address("Piazza Azzarita 8", "40121", "Bologna"));
        p1.setNotes("intelligente ma non si impegna");
        
        Person[] parents = new Person[2];
        parents[0] = new Person("mario", "quarta");
        parents[0].setPhone("093124312");
        parents[1] = new Person("franca", "izzo");
        p1.setParents(parents);
        
        HashSet<String> allergies = new HashSet<>();
        allergies.add("pomodoro");
        allergies.add("burro");
        p1.setAllergies(allergies);
    }

    @Test 
    public void testString() {
        Assert.assertEquals("student"+
                            "\npietro quarta 987654321 Piazza Azzarita 8,40121,Bologna" +
                            "\nallergies: burro,pomodoro" + 
                            "\nparents: mario quarta 093124312,franca izzo" + 
                            "\nnotes: intelligente ma non si impegna",
                            p1.toString());
    }

    
}
