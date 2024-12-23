#include <iostream>

int main() {
    char c;
    int n;

    std::cout << "Address of variable 'c': " << static_cast<void*>(&c) << std::endl;
    std::cout << "Address of variable 'n': " << static_cast<void*>(&n) << std::endl;

    return 0;
}
