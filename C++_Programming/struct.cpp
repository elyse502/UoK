/*
 * File: struct.cpp
 * Auth: Elysée NIYIBIZI
 */

#include <iostream>
/**
 * main - Shows how Struct works.
 *
 * Return: Always 0.
 */

using namespace std;

struct Students {
int Roll_number;
char First_name[20];
char Second_name[16];
char Gender;
char Department_code[5];
};

int main() {
  Students student_data;
  cout << "Enter student's roll number: ";
  cin >> student_data.Roll_number;
  cout << "First name: ";
  cin >> student_data.First_name;
  cout << "Second name: ";
  cin >> student_data.Second_name;
  cout << "Gender: ";
  cin >> student_data.Gender;
  cout << "The department: ";
  cin >> student_data.Department_code;
  cout << "\nStudent's data:" << endl;
  cout << "Roll number: " << student_data.Roll_number << endl;
  cout << "First name: " << student_data.First_name << endl;
  cout << "Second name: " << student_data.Second_name << endl;
  cout << "Gender: " << student_data.Gender << endl;
  cout << "Department: " << student_data.Department_code << endl;
  
  return 0;
}