/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package human;

/**
 *
 * @author elyse
 */
public class Human {
    String name, gender, occupation;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Human myObj = new Human();
        myObj.name = "Elysee NIYIBIZI";
        myObj.gender = "MALE";
        myObj.occupation = "Software Engineer";
        System.out.printf("- Name: %s%n- Gender: %s%n- Occuption: %s%n", myObj.name, myObj.gender, myObj.occupation);
        
        System.out.println("");
        Human myObj1 = new Human();
        myObj1.name = "Jane Smith";
        myObj1.gender = "FEMALE";
        myObj1.occupation = "Senior Software Engineer";
        System.out.printf("- Name: %s%n- Gender: %s%n- Occuption: %s%n", myObj1.name, myObj1.gender, myObj1.occupation);
    }
}
