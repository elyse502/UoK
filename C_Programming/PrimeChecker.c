#include <stdio.h>
#include <stdbool.h>

int main()
{
    int input = 23;
    int isPrime = true;

    for(int i = input - 1; i > 1; i--)
    {
        printf("%d ", i);
        if(input % i == 0)
        {
            isPrime = false;
        }
    }

    if (isPrime)
    {
        printf("\nIs Prime!\n");
    } else
    {
        printf("\nNot Prime!\n");
    }
}