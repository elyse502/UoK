package appjava.raw;

import java.util.Scanner;

public class myFibonacci {

	public static void fib(int b) {
		int[] fibTerms = new int[b];
		
		fibTerms[0] = 0;
		fibTerms[1] = 1;
		
		for (int i = 2; i < b; i++) {
			fibTerms[i] = fibTerms[i-1] + fibTerms[i-2];
		}
		
		System.out.print("The first "+ b + " Fib Terms are [ ");
		
		for (int i = 0; i < b; i++) {
			System.out.print(fibTerms[i] + " ");
		}
		
		System.out.println("]");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Fibo Series of how many? ");
		int limit = input.nextInt();
		
		fib(limit);
		
		input.close();

	}

}
