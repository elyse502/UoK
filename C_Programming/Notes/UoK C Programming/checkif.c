#include<stdio.h>

main()
{
	int b;
	printf("Enter a value: ");
	scanf("%d",&b);
	if(b < 0)
		printf("The value is negative\n");
	else
		printf("The value is positive\n");
}
