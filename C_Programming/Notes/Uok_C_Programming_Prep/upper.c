#include <stdio.h>
#include <ctype.h>

int main()
{
    int j = 0;
    char name[80];
    /* declare an array of characters 0-79 */
    printf("Enter in a name in lowercase\n");
    scanf("%s", name);
    
    printf("\n\n>>> The name in Upper case is: ");
    char ch;
    while (name[j]) {
        ch = name[j];
        putchar(toupper(ch));
        j++;
    }
    printf("\n");
    return 0;
}