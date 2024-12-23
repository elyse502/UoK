// An object function is a function that we can put inside of one of our classes and then the different objects of that class can use that function in order to either you know, find out information about themselves, or modify information about themselves. 

#include <iostream>
using namespace std;

class Student {
  public:
    string name;
    string major;
    double gpa;
    Student(string aName, string aMajor, double aGpa) {
      name = aName;
      major = aMajor;
      gpa = aGpa;
    }

    bool hasHonors() {
      if (gpa >= 3.5) {
        return true;
      }
      return false;
    }
    
};

int main() {
  Student student1("Jim", "Business", 2.4);
  Student student2("Pam", "Art", 3.6);

  cout << student1.hasHonors() << endl;
  cout << student2.hasHonors() << endl;
  
  return 0;
}