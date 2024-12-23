#include <stdio.h>

int main()
{
    double money = 2000;
    double cost = 25;

    money > cost ? printf("You can afford it!\n") : printf("Sorry bro...You need %lf more money\n", cost - money);

    /*
    if(money > cost)
    {
        printf("You can afford it!\n");
    } else
    {
        printf("Sorry bro...You need %lf more money\n", cost - money);
    }
    */

   return 0;
}