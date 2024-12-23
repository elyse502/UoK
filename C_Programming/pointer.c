#include <stdio.h>

int main()
{
  int A;
  int *ptr;				/* A pointer to an integer */
  A = 20;					/* & is called the address operator */
  ptr = &A;
  printf("The valuea of A = %d", A);
  printf("\nThe value of A = %d", *ptr);
  printf("\nThe Address pointed of A = %p", ptr);
  printf("\nthe Address of A = %p\n", &A);
  return 0;
}