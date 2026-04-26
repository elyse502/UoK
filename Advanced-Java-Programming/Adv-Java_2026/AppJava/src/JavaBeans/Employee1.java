 
package JavaBeans;

public class Employee1 {

    public static void main(String[] args) {
        Employee s = new Employee();
        
        s.setId(101);
        s.setName("Danny");
        
        System.out.println("ID: " + s.getId());
        System.out.println("Name: " + s.getName());
    }
    
}
