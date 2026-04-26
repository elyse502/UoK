package appjava.raw;

import java.util.Scanner;

public class myArray {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scan = new Scanner(System.in);
		
		int[] num = new int[5];
		
		System.out.println("Enter 5 elements to store: ");
		
		for (int i = 0; i < 5; i++) {
			num[i] = scan.nextInt();
		}

		System.out.print("The array is [ ");
		
		for (int i = 0; i < 5; i++) {
			System.out.print(num[i] + " ");
		}
		
		System.out.println("]");
		
		scan.close();
	}

}
