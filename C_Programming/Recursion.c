#include <stdio.h>
int factorial(int x);

int main()
{
    int a = factorial(5);
    printf("%d \n", a);
    return 0;
}
int factorial(int x)
{
    if (x <= 1)
    {
        return (1); //Base case
    } else
    {
        return x * factorial(x-1);
    }
}