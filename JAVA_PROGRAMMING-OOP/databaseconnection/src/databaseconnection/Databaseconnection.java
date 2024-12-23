/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconnection;

import java.util.Scanner;

/**
 *
 * @author elyse
 */
public class Databaseconnection {
    
    public static int factorial(int n) {
        if (n == 0)
            return 1;
        else
            return n * factorial(n-1);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("ESTABLISHING THE CONNECTION WITH THE DATABASE!!!");
        
        System.out.println("\n");
//        Scanner scanner = new Scanner(System.in);
//        double num1, num2, num3;
//        char op;
//        while (true){
//            System.out.print("Enter First Number: ");
//            num1 = scanner.nextDouble();
//            if (num1 >= 0 && num1 <= 30)
//                break;
//            System.out.println("Enter a number between 0 and 30!!!");
//        }
//        
//        while (true){
//            System.out.print("Enter Second Number: ");
//            num2 = scanner.nextDouble();
//            if (num2 >= 0 && num2 <= 30)
//                break;
//            System.out.println("Enter a number between 0 and 30!!!");
//        }
//        
//        while (true){
//            System.out.print("Enter Third Number: ");
//            num3 = scanner.nextDouble();
//            if (num3 >= 0 && num3 <= 30)
//                break;
//            System.out.println("Enter a number between 0 and 30!!!");
//        }
//         System.out.print("Choose an operator(-,+,*,/): ");
//         op = scanner.next().charAt(0);
//        
//        switch(op) {
//            case '+':
//                System.out.printf("\nYou chose 'ADDITION':\n>> %.2f + %.2f + %.2f = %.2f%n", num1, num2, num3, num1+num2+num3);
//                break;
//            case '-':
//                System.out.printf("\nYou chose 'SUBTRACTION':\n>> %.2f - %.2f - %.2f = %.2f%n", num1, num2, num3, num1-num2-num3);
//                break;
//            case '*':
//                System.out.printf("\nYou chose 'MULTIPLICATION':\n>> %.2f * %.2f * %.2f = %.2f%n", num1, num2, num3, num1*num2*num3);
//                break;
//            case '/':
//                System.out.printf("\nYou chose 'DIVISION':\n>> %.2f / %.2f / %.2f = %.2f%n", num1, num2, num3, num1/num2/num3);
//                break;
//            default:
//                System.out.println("Invalid Operator!!!");
//        }
//        int[] evenNums = {2, 4, 6, 8, 10, 12, 14, 16};
//        for (int i = 0; i < evenNums.length; i++)
//            System.out.print(evenNums[i] + " ");
//        System.out.println("");
//        for (char ch = 'A'; ch <= 'z'; ch++)
//            System.out.print(ch + " ");
//        System.out.println("");
//        System.out.println("Factorial of 5 is: " + factorial(5));
        
        
    }
    
}
