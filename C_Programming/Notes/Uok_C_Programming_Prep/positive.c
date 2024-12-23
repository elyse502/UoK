/*
 * File: positive.c
 * Author: NIYIBIZI Elysée
 * REG NUMBER: 2305000921 
 */

#include <stdio.h>
/**
 * main - Prints the positive numbers.
 * 
 * Return: Always 0.
 */

int main()
{
    int n, i, a[50];
    printf("Specify the number of values u want =   ");
    scanf("%d", &n);
    for (i = 0; i < n; i++)
    {
        printf("\nENTER value %d..... ", i+1);
        scanf("%d", &a[i]);
    }
    printf("\n\n>> THE POSITIVE NUMBERS ARE: ");
    for (i = 0; i < n; i++)
    {
        if (a[i] >= 0)
        {
            printf("%d  ", a[i]);
        }
        
    }
    printf("\n");
    return 0;
}