/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBeans.BeanInfo;

/**
 *
 * @author STUDENTS
 */
import java.beans.*;

public class StudentUokBeanInfo extends SimpleBeanInfo {
    
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor nameProp = new PropertyDescriptor("Name", StudentUok.class);
        } catch (Exception e) {
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        StrudentUok s = new StudentUok();
        
        s.setName("Alice");
        s.setAge(20);
    }
    
}
