/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Scanner;

/**
 *
 * @author elyse
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /**System.out.println("SIMPLE DYNAMIC CALCULATOR:\n__________________________\n");
        Scanner scanner = new Scanner(System.in);
        double result, num1, num2;
        while (true) {
            System.out.print("Enter num1: ");
            num1 = scanner.nextDouble();
            if (num1 >= 0 && num1 <= 30)
                break;
            System.out.println("Enter a number between 0 and 30");
        }
        
        while (true) {
            System.out.print("Enter num2: ");
            num2 = scanner.nextDouble();
            if (num2 >= 0 && num2 <= 30)
                break;
            System.out.println("Enter a number between 0 and 30");
        }
        
        System.out.print("Enter an operator(+, -, *, /, %: ");
        char op = scanner.next().charAt(0);
        
        switch(op) {
            case '+':
                result = num1 + num2;
                System.out.printf("%n>> %.2f %s %.2f = %.2f%n", num1, op, num2, result);
                break;
            case '-':
                result = num1 - num2;
                System.out.printf("%n>> %.2f %s %.2f = %.2f%n", num1, op, num2, result);
                break;
            case '*':
                result = num1 * num2;
                System.out.printf("%n>> %.2f %s %.2f = %.2f%n", num1, op, num2, result);
                break;
            case '/':
                result = num1 / num2;
                System.out.printf("%n>> %.2f %s %.2f = %.2f%n", num1, op, num2, result);
                break;
            case '%':
                result = num1 % num2;
                System.out.printf("%n>> %.2f %s %.2f = %.2f%n", num1, op, num2, result);
                break;
                
        }**/
        Scanner scanner = new Scanner(System.in);
        System.out.print("Number: ");
        int num = scanner.nextInt();
        
        if (num <= 98) {
            for (int i = num; i <= 98; i++)
                if (i % 5 == 0 && i % 3 == 0)
                    System.out.print("FizzBuzz ");
                else if (i % 5 == 0)
                    System.out.print("Fizz ");
                else if (i % 3 == 0)
                    System.out.print("Buzz ");
                else
                    System.out.printf("%d ", i);
            System.out.println("");
        }
        else {
            for (int i = num; i >= 98; i--)
                if (i % 5 == 0 && i % 3 == 0)
                    System.out.print("FizzBuzz ");
                else if (i % 5 == 0)
                    System.out.print("Fizz ");
                else if (i % 3 == 0)
                    System.out.print("Buzz ");  
                else
                    System.out.printf("%d ", i);
            System.out.println("");
        }
    }
}
