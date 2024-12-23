/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author elyse
 */
public class HelloWorld {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello World!!!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        
        byte age = 30;
        Date now = new Date();
        long phoneNumber = 250_784_652_570L;
        System.out.println(String.format("My name is %s and I'm %d years old!!!\n>> My phone number: +%d%n", name, age, phoneNumber));
        System.out.println(now);
        System.out.println("");
        
        String studentName = "Anna";
        String address = "KIGALI";
        int studentAge = 25;
        long phoneNum = 250_780_111_222L; 
       
        System.out.println("STUDENT INFO:\n_______________\n\n>> Student Name: " + studentName + "\n>> ADDRESS: " + address + "\n>> Age: " + studentAge + "years old!!!\n>> Phone Number = " + phoneNum);
        System.out.println("");
        
        double mark1 = 90;
        double mark2 = 67.5;
        double mark3 = 80.5;
        double sum = mark1 + mark2 + mark3;
        double avg = sum / 3;
        System.out.println(String.format("AVERAGE = %.2f", avg));
        
        if (studentAge != 25)
            System.out.println("The age is not 25");
        else
            System.out.println("The age is equal to 25");
        
    }
    
}
