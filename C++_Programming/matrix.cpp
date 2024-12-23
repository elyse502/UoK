#include <iostream>
using namespace std;

int main() {
  int mat[3][3];
  int i, j;
  cout << "Enter the matrix elments row-wise:" << endl;
  for (i = 0; i < 3; i++) {
    for (j = 0; j < 3; j++) {
      cout << "mat[" << i << "]["<< j << "] = ";
      cin >> mat[i][j];
    }
  }
  // display the matrix
  cout << "\n>> You have entered the matrix:\n";
  for (i = 0; i < 3; i++) {
    for (j = 0; j < 3; j++) {
      cout << mat[i][j] << " ";
    }
    cout << endl;
  }
  return 0;
}