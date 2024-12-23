/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleInheritance;

class Animal {
    String name, color;
    
    void move() {
        System.out.println("Animal is moving");
    }
}

class Dog extends Animal {
    @Override
    void move() {
        System.out.println("Dog is moving");
    }
}

/**
 *
 * @author elyse
 */
public class SingleInheritance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Animal animal1 = new Animal();
        Dog dog1 = new Dog();
        
        // Accessing the methods(behaviors).....
        animal1.move();
        dog1.move();
        
        // Accessing the properties(state/variables/attributes).....
        dog1.name = "Puppy";
        System.out.println(dog1.name);
    }
    
}
