#include <stdio.h>
#include <stdbool.h>

int main()
{
    bool PizzaIsHealthy = true; //This a true or false on whether you like pizza
    int temp; //This a temp variable
    printf("Do you beleive in the power of pizza? 1 is true and 0 is false: ");
    scanf("%d", &temp);
    PizzaIsHealthy = temp;

    if(PizzaIsHealthy) //true as long as not zero(0)!
    {
        //Will this code run?
        printf("Welcome to my pizza APP\n");

    }
}