/* #include <stdio.h>

int main()
{
  int arr[]={1,2,3,4,5,6,7,8,9,10};
  int *ptr;
  ptr=arr;
  int i;
  for (i=0;i<10;i++){
    printf("Address %p \t value %d \n ",&arr[i], *ptr); // %p give u numbers in "Hexadecimal" while %u give u "normal values"!. 
    ptr++;
//		printf("\n\n");
//		printf("Address %u \t value %d \n ",&arr[i], *ptr); // %p give u numbers in "Hexadecimal" while %u give u "normal values"!. 
//		ptr++;
  }
  return 0;
} */

#include <stdio.h>

int main()
{
  int arr[3]={20,225,100};
  int *p;				// A pointer to an array.
  p=arr;				// Arrays doesn't require & while declaring a pointer.
  printf("Address: %p - array value:%d\n",p, *p);
  p++;
  printf("Address: %p - array value:%d\n",p, *p);
  p++;
  printf("Address: %p - array value:%d\n",p, *p);

  return 0;
}