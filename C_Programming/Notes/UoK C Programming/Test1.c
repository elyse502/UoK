#include<stdio.h>

int main()
{
	int a, b, c;
	printf("Enter two numbers to compare: ");
	scanf("%d%d", &a, &b);
	
	c=(a>b)?a:b;
	printf("The biggest number is %d\n", c);
	return 0;
}
