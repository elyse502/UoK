/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animals;

/**
 *
 * @author elyse
 */
public class Animals {
    String name, color;
    double weight;
    
    // constructor
    public Animals(String name, String color, double weight) {
        this.name = name;
        this.color = color;
        this.weight = weight;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Animals dog = new Animals("Bobby", "White / Black", 10.5);
//        dog.name = "Bobby";
//        dog.color = "White / Black";
//        dog.weight = 10.5;
        
        Animals cat = new Animals("Poppy", "Black", 11.5);
//        cat.name = "Poppy";
//        cat.color = "Black";
//        cat.weight = 11.5;
        
        System.out.println("Dog Info:\n- Name: " + dog.name + "\n- Color: " + dog.color + "\n- Weight: " + dog.weight);
        System.out.println("");
        System.out.println("Cat Info:\n- Name: " + cat.name + "\n- Color: " + cat.color + "\n- Weight: " + cat.weight);
        
        char alph = 'A';
        System.out.printf("%c%n", alph);
        
    }
    
}
