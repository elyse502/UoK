#include <iostream>
#include <cstring>
#include <cctype>
#include <algorithm>

int main() {
    char name[80];
    std::cout << "Enter a name in lowercase: ";
    std::cin.getline(name, sizeof(name));

    std::string nameUppercase = name;  // Convert to a string for easy transformation
    std::transform(nameUppercase.begin(), nameUppercase.end(), nameUppercase.begin(), ::toupper);

    std::cout << "The name in uppercase is " << nameUppercase << std::endl;

    std::string reversedName(nameUppercase);
    std::reverse(reversedName.begin(), reversedName.end());
    std::cout << "The name reversed is " << reversedName << std::endl;

    std::cout << "The length of the name is: " << nameUppercase.length() << std::endl;

    return 0;
}
