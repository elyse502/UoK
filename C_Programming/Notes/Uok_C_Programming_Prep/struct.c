#include <stdio.h>

int main()
{
    struct Books
    {
    char book[30];
    int pages;
    float price;
    };
    struct Books bk1={"C++",300,278.5},bk2={"Java",300,700},bk3={"C",400,1000.50};

    printf("\nBOOK 1 INFO:\n_____________");
    printf("\n> Book name: %s",bk1.book);
    printf("\n>> No of pages: %d",bk1.pages);
    printf("\n>>> Book price: %.2f$",bk1.price);

    printf("\n\nBOOK 2 INFO:\n_____________");
    printf("\n> Book name: %s",bk2.book);
    printf("\n>> No of pages: %d",bk2.pages);
    printf("\n>>> Book price: %.2f$",bk2.price);

    printf("\n\nBOOK 3 INFO:\n_____________");
    printf("\n> Book name: %s",bk3.book);
    printf("\n>> No of pages: %d",bk3.pages);
    printf("\n>>> Book price: %.2f$\n\n",bk3.price);
    return 0;
}