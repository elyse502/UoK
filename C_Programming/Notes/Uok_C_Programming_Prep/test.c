#include <stdio.h>

int main()
{
    int a[4] = {1, 2, 4, 5};
    int *ptA;
    ptA = a;
    printf("\nThe value at index 0 is: %d\n", a[0]);
    printf("The value at index 1 is: %d\n", a[1]);
    printf("The value at index 2 is: %d\n", a[2]);
    printf("The value at index 3 is: %d\n", a[3]);
    printf("\n\n");
    printf("The address of element at index 0 is: %p - The Element: %d\n", ptA, *ptA);
    ptA++;
    printf("The address of element at index 1 is: %p - The Element: %d\n", ptA, *ptA);
    ptA++;
    printf("The address of element at index 2 is: %p - The Element: %d\n", ptA, *ptA);
    ptA++;
    printf("The address of element at index 3 is: %p - The Element: %d\n", ptA, *ptA);
    ptA++;


    return 0;
}
