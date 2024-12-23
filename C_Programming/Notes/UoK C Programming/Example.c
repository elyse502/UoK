#include<stdio.h>
#define PI 3.14

main()
{
	int number1, number2;
	printf("Enter the first number: ");
	scanf("%d", &number1);
	
	printf("Enter the second number: ");
	scanf("%d", &number2);
	
	int sum = number1+number2;
	
	
	printf("TOTAL= %d", sum);
}
