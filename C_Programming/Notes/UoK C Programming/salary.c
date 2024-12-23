#include<stdio.h>

// salary calculation based on taxes
main()
{
	double salary,takehome,tax;
	printf("ENTER YOUR GROSS SALARY: ");
	scanf("%lf",&salary);
	if(salary > 45000)
		tax = salary * 30/100;
	else
		tax = salary * 25/100;
		
	takehome = salary - tax;
	printf("MY TAKE HOME SALARY IS :%lf",takehome);
	printf("\nThe taxes took from me :%lf",tax);
}
