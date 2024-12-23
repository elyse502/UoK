#include <stdio.h>

int main()
{
    FILE * fpointer = fopen("Employees.txt", "w");

    fprintf(fpointer, "Bruno, Salesman\nCalvin, Receptionist\nPlacide, Accounting\n");

    fclose(fpointer);
    return 0;
}