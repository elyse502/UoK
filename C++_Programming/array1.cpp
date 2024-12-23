/*
 * File: array1.cpp
 * Auth: Elysée NIYIBIZI
 */

#include <iostream>
/**
 * main - Prints The sum and Average using Array.
 *
 * Return: Always 0.
 */

using namespace std;

int main() {
  int marks[5];
  int i;
  int sum = 0;
  float average;
  cout << "Enter the marks of 5 students:" << endl;

  //read marks of 5 students
  for (i = 0; i < 5; i++) {
    cout << "- Marks of student " << i + 1 << ": ";
    cin >> marks[i];
  }

  // compute sum and average
  for (i = 0; i < 5; i++) {
    sum += marks[i];
  }
  average = (float) sum / 5;
  cout << "\nSum = " << sum << endl;
  cout << "Average = " << average << endl;

    return 0;
}