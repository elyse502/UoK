#include <iostream>
#include <cmath>

using namespace std;

int main() {
  int x = pow(2, 3);
  cout << "2^3 = " << x << endl;
  double y = sqrt(49);
  cout << "SquareRoot of 49 = " << y << endl;
  float z {8.9f};
  cout << floor(z) << endl;
  cout << ceil(z) << endl;
  cout << round(z) << endl;
  cout << "THE MAX = " << fmax(3, 10) << endl;
  cout << "THE MIN = " << fmin(3, 10) << endl;
  
  return 0;
}