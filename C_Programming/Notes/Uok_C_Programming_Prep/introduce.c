/* program which introduces keyboard input */
#include <stdio.h>

int main()
{
int number;
printf("Type in a number \n");
scanf("%d", &number);
printf("The number you typed was %d\n", number);
getchar();
}