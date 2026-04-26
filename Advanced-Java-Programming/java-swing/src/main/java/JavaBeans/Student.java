/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaBeans;

import java.io.Serializable;

/**
 *
 * @author Elysee NIYIBIZI
 */

public class Student implements Serializable {

    // Private property
    private String name;

    // No-argument constructor
    public Student() {
    }

    // Getter method
    public String getName() {
        return name;
    }

    // Setter method
    public void setName(String name) {
        this.name = name;
    }
}
