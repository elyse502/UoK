package operators;

import java.util.Scanner;

public class arithOperator {
	public static void main(String[] args) {
		int a, b, result;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter the value of a and b: ");
		//take two values from user
		a = scanner.nextInt();
		b = scanner.nextInt();
		
		result = a + b; //arithmetic addition of a and b
		System.out.println("Result after addition: " + result);
		
		result = a - b; //arithmetic subtraction of a and b
		System.out.println("Result after subtraction: " + result);
		
		result = a * b; //arithmetic multiplication of a and b
		System.out.println("Result after multiplication: " + result);
		
		result = a / b; //arithmetic division of a and b
		System.out.println("Result after division: " + result);
		
		result = a % b; //remainder operator to get remainder after dividing a by b
		System.out.println("Remainder : " + result);
		
		scanner.close();
	}

}
