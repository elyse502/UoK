#include <stdio.h>

int main()
{
    /*int i = 1;
    while (i <= 5)
    {
        printf("%d\n", i);
        i++;
    }*/

    /*int i;
    for ( i = 1; i <= 5; i++)  //This ForLoop is the same as the WhileLoop above...
    {
        printf("%d\n", i);
    }*/

    int luckyNumbers[] = {4, 8, 15, 16, 23, 42};

    int i;
    for ( i = 1; i <= 5; i++)  
    {
        printf("%d\n", luckyNumbers[i]);
    }

    return 0;
}