#include<stdio.h>

main()
{
	int num1,num2;
	printf("Enter two numbers: ");
	scanf("%d%d",&num1,&num2);
	if(num1 > num2)
	{
		printf("This first number entered is the biggest %d\n",num1);
	}
	if(num2 > num1)
	{
		printf("This second number entered is the biggest %d\n",num2);
	}
	if(num1 == num2)
	{
		printf("Both numbers entered are equal");
	}
	
}
