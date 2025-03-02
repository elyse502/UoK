#include <stdio.h>

int factorial(int number)
{
    int factorial = 1;

    for (int i = number; i > 1; i--)
    {
        factorial *= i;
        //factorial = factorial * i;
    }

    return factorial;
}

int main()
{   
    /*int fact = factorial(5);
    int fact2 = factorial(8);
    printf("%d ", fact);
    printf("%d ", fact2);*/

    printf("%d ", factorial(5));
    printf("%d ", factorial(8));

    printf("%d ", factorial(factorial(3)));
    printf("\n");
    return 0;
}