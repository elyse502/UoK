/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBeans;

/**
 * Implementing Serializable
 * @author STUDENTS
 */

import java.io.*;

public class Student implements Serializable {
    
    private String name;
    
    public Student() {}
    
    public void setNom(String name){
        this.name = name;
    }
    
    public String getNom(){
        return name;
    }
    
}
