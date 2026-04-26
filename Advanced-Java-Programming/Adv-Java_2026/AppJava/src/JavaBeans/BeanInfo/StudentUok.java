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

import java.io.Serializable;

public class StudentUok implements Serializable {
    private String name;
    private int age;
    
    public StudentUok(){}
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge (int age) {
        this.age = age;
    }
}
