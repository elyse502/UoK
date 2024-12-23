/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bmi_calculator;

import java.util.Scanner;

/**
 *
 * @author Elysée_NIYIBIZI
 */
public class BMI_CALCULATOR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter weight and height
        System.out.print("Enter your weight in kilograms: ");
        double weight = scanner.nextDouble();

        System.out.print("Enter your height in meters: ");
        double height = scanner.nextDouble();

        // Calculate BMI
        double bmi = weight / (height * height);

        // Determine BMI category
        System.out.printf("\n>> Your BMI is: %.2f%n", bmi);

        if (bmi < 18.5) {
            System.out.println(">> Category: Underweight");
        } else if (bmi >= 18.5 && bmi < 24.9) {
            System.out.println(">> Category: Normal weight");
        } else if (bmi >= 25 && bmi < 29.9) {
            System.out.println(">> Category: Overweight");
        } else {
            System.out.println(">> Category: Obesity");
        }

        scanner.close();
    }
    
}