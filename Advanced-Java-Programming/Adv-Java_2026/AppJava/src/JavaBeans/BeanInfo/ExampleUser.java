package JavaBeans.BeanInfo;

/**
 *
 * @author STUDENTS
 */
public class ExampleUser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        User u = new User();
        
        u.addPropertyChangeListener(event -> {
           System.out.println("Property Changed: " + event.getPropertyName()); 
           System.out.println("Old Value: " + event.getOldValue()); 
           System.out.println("New Value: " + event.getNewValue()); 
        });
        
        u.setName("Alice");
        u.setName("Bob");
    }
    
}
