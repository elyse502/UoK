/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.util.Scanner;

/**
 *
 * @author elyse
 */
public class Functions {
    static void information(String fName, String lName, int age) {
        System.out.println("Hello Mr " + fName + " " + lName + "!!!\nYou're " + age + " years old!!!");
}
    static double average(double num1, double num2, double num3) {
        return (num1 + num2 + num3)/3;
    }
            

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        information("Elysee", "NIYIBIZI", 30);
        System.out.println(average(10, 20, 60));
        
        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter your name: ");
//        String name = scanner.nextLine();
//        System.out.println("Hello Mr " + name);

        System.out.println("\nLOG IN FORM:\n___________\n");
        String userName, passCode;
        while (true) {
            System.out.print(">> UserName: ");
            userName = scanner.nextLine();
            if (userName.length() > 3)
                break;
            System.out.println("Please, Enter at least 4 characters!!!");
        }
        
        while (true) {
            System.out.print(">> Password: ");
            passCode = scanner.nextLine();
            if (passCode.length() >= 3)
                break;
            System.out.println("Please, Enter at least 3 characters!!!");
        }
        
        if (userName.equals("Admin") && passCode.equals("12345"))
            System.out.println("\n\tLogged In successfully!!!");
        else
            System.out.println("\n\tInvalid Credentials!!! Try again.....");
        
    }
    
}
