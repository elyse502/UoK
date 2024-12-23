#include <iostream>
using namespace std;
/*
    cout << "Age: " << &age << endl;
    cout << "Gpa: " << &gpa << endl;
    cout << "Name: " << &name << endl;
*/

int main() {
  int age = 19;
  int *pAge = &age;
  double gpa = 2.7;
  double *pGpa = &gpa;
  string name = "Mike";
  string *pName = &name;


  cout << pAge << endl; // Pointer(Memomoy Address)
  cout << "Age = " << *pAge << endl; // dereferencing a pointer
  cout << &gpa << endl; // Memomoy Address
  cout << "Gpa = " << *&gpa << endl; // dereferencing(We get value of gpa)
  cout << &*&gpa << endl; // We get the Memomoy Address again...
  return 0;
}