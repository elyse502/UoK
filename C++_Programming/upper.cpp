#include<iostream>
#include<cstring>

using namespace std;

int main()
{
    char str1[] = "new zealand";
    cout<<"Original String value : " << str1 << "\n";
    strupr(str1);
    cout<< "String's upper case value : " << str1;

    return 0;
}