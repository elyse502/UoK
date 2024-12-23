#include <ctype.h>
#include <stdio.h>
#include <string.h>

int main() {
  char name[80];
  /* declare an array of characters 0-79 */
  printf("Enter in a name in lower case\n");
  scanf("%s", name);

  char Name = 'e';
  printf("The name in uppercase is %c\n\n", toupper(Name));

  strupr(name);
  printf("The name in uppercase is %s", name);

  strrev(name);
  printf("\nThe name Reversed is %s", name);

  printf("\nThe length of the name is:%d", strlen(name));
  return 0;
}


/*
#include <ctype.h>
#include <stdio.h>
#include <string.h>

void reverseString(char *str) {
    int len = strlen(str);
    for (int i = 0; i < len / 2; i++) {
        char temp = str[i];
        str[i] = str[len - 1 - i];
        str[len - 1 - i] = temp;
    }
}

int main() {
    char name[80];

    printf("Enter a name in lowercase: ");
    scanf("%s", name);

    char Name = 'e';
    printf("The name in uppercase is %c\n\n", toupper(Name));

    // Convert to uppercase
    for (int i = 0; name[i] != '\0'; i++) {
        name[i] = toupper((unsigned char)name[i]);
    }
    printf("The name in uppercase is %s\n", name);

    // Reverse the string
    reverseString(name);
    printf("The name Reversed is %s\n", name);

    printf("The length of the name is: %lu\n", (unsigned long)strlen(name);

    return 0;
} */