/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oop;

/**
 *
 * @author elyse
 */
public class OOP {
    String name = "Elysee NIYIBIZI";
    String gender = "MALE";
    String occupation = "Software Engineer";
    
    @Override
    public String toString() {
        return String.format("Name: %s%nGender: %s%nOccupation: %s", name, gender, occupation);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        OOP myObj = new OOP();
        System.out.println(myObj);
        System.out.printf("- Name: %s%n- Gender: %s%n- Occuption: %s%n", myObj.name, myObj.gender, myObj.occupation);
    }
    
}
