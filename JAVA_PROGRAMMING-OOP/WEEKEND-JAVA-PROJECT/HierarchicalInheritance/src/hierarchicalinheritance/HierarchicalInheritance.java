/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hierarchicalinheritance;

class Student {
    String name = "Elysee";
    void sing() {
        System.out.println("Student is Singing....");
    } 
}

class Science extends Student {
    
}

class Commerce extends Student {
    
}

/**
 *
 * @author elyse
 */
public class HierarchicalInheritance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Science scnc1 = new Science();
        Commerce comm1 = new Commerce();
        
        scnc1.sing();
        comm1.sing();
        
    }
    
}
