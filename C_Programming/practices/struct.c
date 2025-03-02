#include <stdio.h>

struct date			// Global definition of type date.
{
  int day;
  int month;
  int year;
} today;
int main()
{
//	struct date today; // U can use this method when never named "today" ur struct initial.
  today.day=20;
  today.month=8;
  today.year=2023;
  printf("\n>>> Today's date is: %d/%d/%d\n",today.day,today.month,today.year);
  getchar();
  return 0;
} 


/*
#include <stdio.h>
#include <string.h>

struct Classroom
{
  char reg[16];
  char name[50];
  int age;
}st;

int main()
{
  printf("RECORD STUDENT INFORMATION\n____________________________\n");
  //student st=new student(); 
  printf("\nEnter reg number: ");
  scanf("%s",st.reg);
  printf("\nEnter student name: ");
  scanf("%s",st.name);
//	printf("\nEnter student name: ");gets(st.name);
  printf("\nEnter age: ");
  scanf("%d",&st.age);

  printf("\nSTUDENT HAS THE FOLLOWING INFORMATION:\n_______________________________________");
  printf("\n\n>>> REG NUMBER: %s\t",st.reg);
  printf("NAME: %s\t",st.name);
  printf("AGE: %d",st.age);
  return 0;
} */