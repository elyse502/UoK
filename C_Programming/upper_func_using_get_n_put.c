#include <stdio.h>
#include <string.h>

main() {
  char name[80];
  /* declare an array of characters 0-79 */
  printf("Enter in a name in lower case\n");
  gets(name);

  strupr(name);
  printf("The name in uppercase is...");
  puts(name);

  strrev(name);
  printf("\nThe name Reversed is...");
  puts(name);

  printf("\nThe length of the name is:%d", strlen(name));
}