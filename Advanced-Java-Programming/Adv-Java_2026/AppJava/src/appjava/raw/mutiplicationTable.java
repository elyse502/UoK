package appjava.raw;

import java.util.Scanner;

public class mutiplicationTable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Multiplication table of: ");
		int num = input.nextInt();
		
		System.out.println("Multiplication of " + num + " is: ");
		
		for (int i = 0; i <= 10; i++) {
			System.out.println(num + " x " + i + " = " + (num * i));
		}
		
		input.close();
	}
}
