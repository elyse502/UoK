#include<stdio.h>

int main()
{
	int a,b,c;
	printf("Enter the numbers: ");
	scanf("%d%d",&a,&b);
	c=(a>b)?a:b;
		printf("The biggest number is %d",c);
	c=(a<b)?a:b;
		printf("\nThe smallest number is %d\n",c);
	return 0;
	
}
