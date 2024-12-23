#include <stdio.h>

/* Function to swap two numbers using call by reference. */
void swap(int *a,int *b){
  int temp = *a;
  *a = *b;
  *b = temp; // Temporary valiable.
}

int main()
{
  int num1, num2;

  printf("Enter two numbers: ");
  scanf("%d %d",&num1,&num2);

  printf("\n> Before swapping: num1 = %d, num2 = %d\n", num1, num2);
  // Call the swap function to swap the numbers.
  swap(&num1, &num2);

  printf("\n>> After swapping: num1 = %d, num2 = %d\n", num1, num2);

  return 0;
}