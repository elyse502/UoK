package appjava.raw;

import java.util.Scanner;

public class myBubbleSort {
	
	public static void bubbleSort(int[] arr, int b) {
		for (int i = 1; i < b; i++) {
			for (int j = 0; j < b-1; j++) {
				if (arr[j] > arr[j+1]) {					
					int temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
		}
	}
	
	public static void printArray(int[] arr, int b) {
		System.out.print("The array is [ ");
		
		for (int i = 0; i < b; i++) {
			System.out.print(arr[i]+" ");
		}
		
		System.out.println("]");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter size of array to sort: ");
		int size = scan.nextInt();
		
		int[] num = new int[size];
		
		System.out.println("Enter "+size+" elements to sort: ");
		
		for (int i = 0; i < size; i++) {
			num[i] = scan.nextInt();
		}
		
		printArray(num, size);
		
		bubbleSort(num, size);

		System.out.println("After bubble sort:  ");
		
		printArray(num, size);
		
		scan.close();
	}

}
