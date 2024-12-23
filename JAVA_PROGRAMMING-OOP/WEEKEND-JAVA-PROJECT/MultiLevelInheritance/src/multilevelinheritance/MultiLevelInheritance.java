/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multilevelinheritance;

class Animal {
    String name;
    
    void move() {
        System.out.println("Animal is moving....");
    }
}

class Mammals extends Animal {
    int age = 30;
    
    void feed() {
        System.out.println("Mammal is feeding....");
    }
}

class Dog extends Mammals {

}

/**
 *
 * @author elyse
 */
public class MultiLevelInheritance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Dog dog1 = new Dog();
        dog1.feed();
        dog1.move();
        
        dog1.name = "SIMBA";
        System.out.println("Dog name: " + dog1.name);
        System.out.println("Dog age: " + dog1.age);
    }
    
}
