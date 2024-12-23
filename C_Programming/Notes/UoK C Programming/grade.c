#include<stdio.h>

main()
{
	int number;
	
	printf("Enter a number: ");
	scanf("%d", &number);
	
	printf("%s\n", (number == 10) ? "ok" : "bad");
}
