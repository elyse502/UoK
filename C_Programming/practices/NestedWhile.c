/*
0
1 0
2 1 0
3 2 1 0
4 3 2 1 0
5 4 3 2 1 0
6 5 4 3 2 1 0
7 6 5 4 3 2 1 0
8 7 6 5 4 3 2 1 0
9 8 7 6 5 4 3 2 1 0
10 9 8 7 6 5 4 3 2 1 0
*/


#include <stdio.h>

int main ()
{
    int i = 0, k;
    while(i < 11)
    {
        k = i - 1;
        printf("%d ", i);
        i++;
        
        while(k >= 0)
        {
            printf("%d ", k);
            k--;
        }


        printf("\n");
    }

    return 0;
}