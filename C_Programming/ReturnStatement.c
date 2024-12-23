#include <stdio.h>
#include <stdlib.h>

double cube(double num)
{
    double result = num * num * num; //You can also pass here just: return num * num * num;
    return result;
}

int main()
{
    printf("Answer: %f\n", cube(3.0));

    return 0;
}