#include <iostream>

/**
 * main - Accessing the different elements of an array
 *
 * Return: Always 0.
 */
int main() {
    int a[5];

    a[0] = 98;
    a[1] = 198;
    a[2] = 298;
    a[3] = 398;
    a[4] = 498;

    std::cout << "Value of a[0]: " << a[0] << std::endl;
    std::cout << "Value of a[1]: " << a[1] << std::endl;
    std::cout << "Value of a[2]: " << a[2] << std::endl;
    std::cout << "Value of a[3]: " << a[3] << std::endl;
    std::cout << "Value of a[4]: " << a[4] << std::endl;
    std::cout << "Address of 'a[0]': " << static_cast<void*>(&a[0]) << std::endl;
    std::cout << "Address of 'a[1]': " << static_cast<void*>(&a[1]) << std::endl;
    std::cout << "Address of 'a[2]': " << static_cast<void*>(&a[2]) << std::endl;
    std::cout << "Address of 'a[3]': " << static_cast<void*>(&a[3]) << std::endl;
    std::cout << "Address of 'a[4]': " << static_cast<void*>(&a[4]) << std::endl;

    return 0;
}
