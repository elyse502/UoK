#include <stdio.h>
#include <stdlib.h>

void sayHi();
int main()
{
    printf("Here is your age Declaration:\n\n");
    sayHi("Mike", 40);
    sayHi("Tom", 70);
    sayHi("Jerry", 23);
    printf("\nFor now we are done. Thank you!\n");
    return 0;
}

void sayHi(char name[], int age)
{
    printf("Hello %s, you are %d!\n", name, age);
}  