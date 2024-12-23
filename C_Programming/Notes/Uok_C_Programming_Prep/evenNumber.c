#include <stdio.h>

int main(){
int b;
printf("Enter a value:");
scanf("%d", &b);
if (b % 2 == 0)
printf("\nThe value is even\n");
else
printf("\nThe value is odd\n");
}