/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineshoppingcart;

/**
 *
 * @author elyse
 */
public class OnlineShoppingCart {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        double totalPrice = 150.0;
        
        if (totalPrice > 100) {
            totalPrice *= 0.9; // 10% discount
            System.out.println("Discount applied. New total: " + totalPrice);
        } else {
            System.out.println("No discount applied.");
        }
        
        for (int i = 0; i <= 100; i += 10) 
            System.out.println(i);
        System.out.println("");
        
        for (int j = 1; j <= 30; j += 1) {
                if(j == 2 || j == 7 || j == 10 || j == 20 || j % 2 != 0){
                    continue;
                }
            System.out.println("ITERATION " + j);
        }
        System.out.println("\n\nPRACTICES:\n________________________");
        int myInt = 9;
        double myDouble = myInt; // Automatic casting: int to double

        System.out.println(myInt);      // Outputs 9
        System.out.println(myDouble);   // Outputs 9.0
        
        }   
}

