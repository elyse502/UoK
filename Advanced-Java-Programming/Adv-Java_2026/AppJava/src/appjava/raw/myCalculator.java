package appjava.raw;

import java.util.Scanner;

public class myCalculator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scan = new Scanner(System.in);
		
		double result = 0.0;
		
		System.out.println("Enter number 1: ");
		double num1 = scan.nextDouble();
		
		System.out.println("Enter number 2: ");
		double num2 = scan.nextDouble();
		
		System.out.println("Choose sign: ");
		System.out.println("1 Add ");
		System.out.println("2 Subtract ");
		System.out.println("3 Divide ");
		System.out.println("4 Multiply ");
		int choice = scan.nextInt();
		
		if (choice == 1) {
			result = num1 + num2;
		}
		else if (choice == 2) {
			result = num1 - num2;			
		}
		else if (choice == 3) {
			result = num1 / num2;			
		}
		else if (choice == 4) {
			result = num1 * num2;			
		}
		else {
			result = -1;			
		}
		
		if (result == -1) {
			System.out.println("Invalid Choice");
		}
		else {
			System.out.println("The result is " + result);
		}
		
		scan.close();
		
	}

}
