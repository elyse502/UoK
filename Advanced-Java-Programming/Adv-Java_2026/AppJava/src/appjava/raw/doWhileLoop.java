package appjava.raw;

//Importing Scanner class for reading the user input ...
import java.util.Scanner;

public class doWhileLoop {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Making object named 'scan' for reading user input ...
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Welcome to Shop, Enter any number to continue: ");
		int n = scan.nextInt();
		
		if (n == 1) {
			System.out.println("Now... Passed");
//			do {
//				n += 1;
//			}while(n < 3);
		}
		else {
			System.out.println("There Failed...");
		}
		// Closing scanning from user input
		scan.close(); 
	}

}
