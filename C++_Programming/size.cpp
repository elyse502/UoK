#include <iostream>

using namespace std;

/**
 * main - using sizeof to dynamically determine the size of types char, int and float
 *
 * Return: Always 0.
 */
int main(void)
{
   int n;
   cout << "Size of type 'char' on my computer: " << sizeof(char) << " bytes" << endl;
   cout << "Size of type 'string' on my computer: " << sizeof(string) << " bytes\n";
   cout << "Size of type 'int' on my computer: " << sizeof(int) << " bytes\n";
   cout << "Size of type 'short' on my computer: " << sizeof(short) << " bytes\n";
   cout << "Size of type 'long' on my computer: " << sizeof(long) << " bytes\n";
   cout << "Size of type 'float' on my computer: " << sizeof(float) << " bytes\n";
   cout << "Size of type 'double' on my computer: " << sizeof(double) << " bytes\n";
   cout << "Size of my variable n on my computer: " << sizeof(char) << " bytes" << endl;
   
   return (0);
}