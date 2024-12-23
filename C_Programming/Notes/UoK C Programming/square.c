#include<stdio.h>

main()
{
	double side,perimeter,area; // Square all sides are equal!
	printf("ENTER SIDE: ");
	scanf("%lf",&side);
	perimeter = side*4;
	area = side*side;
	printf("\nPERIMETER IS = %lf",perimeter);
	printf("\nAND THE AREA IS = %lf",area);
}
