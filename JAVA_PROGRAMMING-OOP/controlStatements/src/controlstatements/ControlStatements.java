/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlstatements;

import java.util.Scanner;

/**
 *
 * @author elyse
 */
public class ControlStatements {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("BASIC CALCULATOR:\n_________________\n\n");
        Scanner scanner = new Scanner(System.in);
        System.out.println("> Enter First Number: ".trim());
        String num1 = scanner.nextLine();
        System.out.println("> Enter Second Number: ".trim());
        String num2 = scanner.nextLine();
        System.out.println("Enter an operator: ".trim());
        char op = scanner.next().charAt(0);
        double result;
        switch (op){
            case '+':
                result = Double.parseDouble(num1) + Double.parseDouble(num2);
                System.out.println("\n>> RESULT: " + num1 + " + " + num2 + " = " + result);
                break;
            case '-':
                result = Double.parseDouble(num1) - Double.parseDouble(num2);
                System.out.println("\n>> RESULT: " + num1 + " - " + num2 + " = " + result);
                break;
            case '*':
                result = Double.parseDouble(num1) * Double.parseDouble(num2);
                System.out.println("\n>> RESULT: " + num1 + " * " + num2 + " = " + result);
                break;
            case '/':
                result = Double.parseDouble(num1) / Double.parseDouble(num2);
                System.out.println("\n>> RESULT: " + num1 + " / " + num2 + " = " + result);
                break;
            case '%':
                result = Double.parseDouble(num1) % Double.parseDouble(num2);
                System.out.println("\n>> RESULT: " + num1 + " % " + num2 + " = " + result);
                break;
            default:
                System.out.println("Invalid Operator!!!");
        }
        
    }
    
}
