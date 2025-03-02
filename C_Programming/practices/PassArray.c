#include <stdio.h>

void printArray(int arr[], int size)
{
    for(int i =0; i < size; i++)
    {
        arr[i]++;
        printf("%d ", arr[i]);
    }
}
int main()
{
    int slicesEachMeal[] = {4, 5, 4, 6, 3, 2};

    printArray(slicesEachMeal, 6);
    printf("\n");
    for(int i =0; i < 6; i++)
    {
        printf("%d ", slicesEachMeal[i]);
    }

    return 0;
}