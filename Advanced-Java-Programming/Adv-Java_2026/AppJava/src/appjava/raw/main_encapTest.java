
package appjava.raw;

public class main_encapTest {
    
    public static void main(String[] args) {
        
        encapTest person = new encapTest();
        
        person.setName("Danny");
        person.setAge(35);
        person.setIdNum("1198586454645");
        
        System.out.println("The name: " + person.getName()
                        + "\nThe age: " + person.getAge()
                        + "\nThe ID Number: " + person.getIdNum());
    }
    
}
