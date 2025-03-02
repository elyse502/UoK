#include <stdio.h>

int main()
{
    char grade;
    printf("Enter your Grade: ");
    scanf(" %c", &grade);

    switch (grade)
    {
    case 'A' :
        printf("You did Great!\n");
        break;
    case 'B' :
        printf("You did Alright!\n");
        break;
    case 'C' :
        printf("You did Poorly!\n");
        break;
    case 'D' :
        printf("You did Very Bad!\n");
        break;
    case 'F' :
        printf("You Failed!\n");
        break;
    default:
        printf("Invalid Grade\n");
        break;
    }
    return 0;
}