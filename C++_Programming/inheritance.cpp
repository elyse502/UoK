#include <iostream>
using namespace std;

class Chef { // A superclass
  public:
    void makeChicken() {
      cout << "The chef makes yummy chicken" << endl;
    }
    void makeSalad() {
      cout << "The chef makes salad" << endl;
    }
    void makeSpecialDish() {
      cout << "The chef makes bbq ribs" << endl;
    }
};

class ItalianChef : public Chef { // A subclass (inherits from the Chef class).
  public:
    void makePasta() {
      cout << "The chef makes pasta" << endl;
    }
    void makeSpecialDish() { // Overriding the makeSpecialDish() function from the Chef class.
      cout << "\nThe chef makes chicken parm" << endl;
    }

};

int main() {
  Chef chef;
  chef.makeChicken();
  chef.makeSpecialDish();
  
  ItalianChef italianChef;
  italianChef.makeSpecialDish();
  italianChef.makePasta();
  
  return 0;
}