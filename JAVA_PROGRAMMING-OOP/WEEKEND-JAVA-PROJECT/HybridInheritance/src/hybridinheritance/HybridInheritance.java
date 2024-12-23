/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hybridinheritance;

class GrandFather {
    String name = "NIYIBIZI";
    void move() {
        System.out.println("Is moving like....");
    }
}

class Father extends GrandFather {
    String name = "Charles";
    void speak() {
        System.out.println("Is speaking like....");
    }
}

class Son extends Father {
    
}

class Daughter extends Father {
    
}
/**
 *
 * @author elyse
 */
public class HybridInheritance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Son son1 = new Son();
        System.out.println(son1.name);
    }
    
}
