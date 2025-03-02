#include <stdio.h>

int main()
{
    int number = 23232;
    int i = 0;
    while(1)
    {
        //do something indefinately
        printf("%d", i);
        if (i == number)
        {
            printf(" We found it!\n");
            break;
        }
        i++;
    }
    return 0;
}