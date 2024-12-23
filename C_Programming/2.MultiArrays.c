#include <stdio.h>

void printArray(int arr[], int size)
{
    for(int i =0; i < size; i++)
    {
        printf("%d ", arr[i]);
    }
}

int main()
{
    int const columns = 3;
    int const rows = 2;
    int grades[][/*columns*/3] = {
        {12, 23, 45},
        {64, 78, 89}
    };
        
    for (int i = 0; i < rows; i++)
    {
       printArray(grades[i], columns);
       printf("\n");
    }
    return 0;
}