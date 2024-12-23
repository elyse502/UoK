#include<stdio.h>

int main()
{
	int length,width,perimeter,area;
	printf("ENTER BOTH WIDTH AND LENGTH: ");
	scanf("%d%d",&length,&width);
	perimeter = (length+width)*2;
	area = length*width;
	printf("\nPERIMETER IS =%d", perimeter);
	printf("\nAND THE AREA IS =%d", area);
	
}
