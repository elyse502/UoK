#include <stdio.h>

int main() 
{
    int a = 5;
    int b = 10;
    int c = 15;
    
    //Printing address without pointers.
    printf("%p\n", &a);
    printf("%p\n", &b);
    printf("%p\n", &c);
    
    printf("\n");
    //Declare and assign the pointers.
    int *ptA = &a;
    int *ptB = &b;
    int *ptC = &c;
    
    printf("%p\n", ptA);
    printf("%p\n", ptB);
    printf("%p\n", ptC);
    
    printf("\n");
    //Dereferencing of pointers using %d Or %i!.
    printf("%d\n", *ptA);
    printf("%d\n", *ptB);
    printf("%d\n", *ptC);
    
    printf("\n");
    //Dereferencing of pointers using %p gives answers in Hexadecimals!.
    printf("%p\n", *ptA);
    printf("%p\n", *ptB);
    printf("%p\n", *ptC);

    return 0;
}