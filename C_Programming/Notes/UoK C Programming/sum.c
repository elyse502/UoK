#include <stdio.h>

main()
{
	int num1, num2;
	printf("Enter your First Number: ");
	scanf("%d", &num1);
	printf("Enter your Second Number: ");
	scanf("%d", &num2);
	
	int sum = num1+num2;
	
	printf("Total = %d", sum);
}
