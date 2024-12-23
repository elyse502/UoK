#include <stdio.h>

//Function Prototype
int check_positive(int);

int main()
{
	int num, pos_or_neg;

	printf("please enter a positive integer: ");
	scanf("%d", &num);

	pos_or_neg = check_positive(num);
	if (pos_or_neg == 0)
	{
		printf("You didn't enter a positive integer.\n");
	}
    else
    {
        printf("The number was positive.\n");
    }
    return 0;
}

int check_positive(int number)  //If I put this at the top where there is a FunctionProtype it will work the same... 
{
	if (number > 0)
	{
		return 1;
	}
	else
	{
	    return 0;
	}
}
