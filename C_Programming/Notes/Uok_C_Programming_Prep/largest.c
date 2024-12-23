/*
 * File: largest.c
 * Author: NIYIBIZI Elysée
 * REG NUM: 2305000921
 */

#include <stdio.h>
/**
 * main - prints the largest number and the smallest number
 * 
 * Return: Always 0.
 */
int main()
{
    int n, i, largest = 0, smallest = 0;
    printf("HOW MANY NUMBERS DO U WANT 4R COMPARISON?   ");
    scanf("%d", &n);
    int a[n];
    for (i = 0; i < n; i++)
    {
        printf("\nENTER number %d:    ", i + 1);
        scanf("%d",&a[i]);
    }
    largest = a[0];
    smallest = a[0];
    for (i = 0; i < n; i++)
    {
        if (a[i] > largest)
        {
            largest = a[i];
        }
        else if (a[i] < smallest)
        {
            smallest = a[i];
        } 
    }
    printf("\n\n>> THE LARGEST NUMBER IS ... %d\n>> THE SMALLEST NUMBER IS ... %d\n", largest, smallest);  
    return 0;
}