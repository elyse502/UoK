/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBeans;

import java.io.Serializable;

public class Employee implements Serializable {
    private int id;
    private String name;
    
    public Employee(){
        
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
