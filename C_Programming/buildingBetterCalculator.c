#include <stdio.h>

int main()
{
    double num1, num2;
    char op;

    printf("Enter First number: ");
    scanf("%lf", &num1);
    printf("Enter Operator: ");
    scanf(" %c", &op);
    printf("Enter Second number: ");
    scanf("%lf", &num2);

    if (op == '+')
    {
        printf("\n%f\n", num1 + num2);
    } else if (op == '-')
    {
        printf("\n%f\n", num1 - num2);
    } else if (op == '/')
    {
        printf("\n%f\n", num1 / num2);
    } else if (op == '*')
    {
        printf("\n%f\n", num1 * num2);
    } else
    {
        printf("\nInvalid Operator\n");
    }
    
    
    return 0;
}