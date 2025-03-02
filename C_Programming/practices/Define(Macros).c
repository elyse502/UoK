#include <stdio.h>
#define MAX 20
#define PI 3.14
#define print printf("PRINT THIS!\n");
#define inc(x) x + 1
#define area(base,height) 0.5 * base * height
#define min(x,y) ((x < y) ? x : y)
#define output(s1) printf("%s\n", s1);
#define find_max(array,length) ({ \
    int current_max = array[0]; \
    for (int i = 1; i < length; i++) \
        if (array[i] > current_max) \
            current_max = array[i]; \
    current_max; \
})

int main(void)
{
    printf("Max: %d\n", MAX);

    printf("Pi= %f\n", PI);

    print

    int a = 2;
    a = inc(a);
    printf("a: %d\n", a);

    int base1 = 20;
    int height1 = 20;
    int area1 = area(base1,height1);
    printf("area1: %d\n", area1);

    double base2 = 10.5;
    double height2 = 5.2;
    double area2 = area(base2, height2);
    printf("area2: %f\n", area2);

    int min_num = min(2,min(6,3));
    printf("min_num: %d\n", min_num);

    char string[] = "test2";
    output(string);

    int int_array[] = {3,5,2,1,8,3,2};
    int max_int = find_max(int_array,7);
    printf("max_int: %d\n", max_int);
    
    return 0;
}