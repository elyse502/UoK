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

public class User {
    private String name;
    
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    
    public User() {}
    
    public String getName(){
        return name;
    }
    
    // Setting for 'name' property with event notification
    public void setName(String name) {
        String oldName = this.name; // Store old name value
        this.name = name; //Set new value to name
        support.firePropertyChange("Name", oldName, name); // Notify listeners
    }
    
    // Track the change happening on the property
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    
    // Remove a tracker from the beans for the change happening on the property
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
    
}
