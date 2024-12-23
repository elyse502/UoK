/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package methodoverriding;

class Animal {
    void sound(){
        System.out.println("Animal makes sound");
    }
}

class Dog extends Animal {
    @Override
    void  sound() {
        System.out.println("Dog barks...");
    }
}

class Cat extends Animal {
    @Override
    void sound() {
        System.out.println("Cat meows...");
    }
}

/**
 *
 * @author elyse
 */
public class MethodOverriding {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Animal myAnimal = new Animal();
        Animal myDog = new Dog();
        Animal myCat = new Cat();
        
        myAnimal.sound();
        myDog.sound();
        myCat.sound();
    }
    
}
