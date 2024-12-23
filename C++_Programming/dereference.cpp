#include <iostream>

/**
 * main - derefencing pointers, example with int and char types
 *
 * Return: Always 0.
 */
int main() {
    int n;
    int *p;
    char c;
    char *pp;

    c = 'H';
    pp = &c;
    n = 98;
    p = &n;
    
    std::cout << "Value of 'c': " << static_cast<int>(c) << " ('" << c << "')" << std::endl;
    std::cout << "Address of 'c': " << static_cast<void*>(&c) << std::endl;
    std::cout << "Value of 'pp': " << static_cast<void*>(pp) << std::endl;
    std::cout << "Value of 'n': " << n << std::endl;
    std::cout << "Address of 'n': " << static_cast<void*>(&n) << std::endl;
    std::cout << "Value of 'p': " << static_cast<void*>(p) << std::endl;

    *p = 402;
    *pp = 'o';

    std::cout << "Value of 'n': " << n << std::endl;
    std::cout << "Value of '*pp': " << static_cast<int>(*pp) << " ('" << *pp << "')" << std::endl;
    std::cout << "Value of 'c': " << static_cast<int>(c) << " ('" << c << "')" << std::endl;
    std::cout << "Value of '*pp': " << static_cast<int>(*pp) << " ('" << *pp << "')" << std::endl;

    return 0;
}
