#include<stdio.h>

main(){
	float marks;
	printf("ENTER YOUR MARKS OUT OF 100: ");
	scanf("%f",&marks);
	if(marks >= 80)
		printf("Your Grade is :		A");
	else if(marks >= 70)
		printf("Your Grade is :		B");
	else if(marks >= 60)
		printf("Your Grade is :		C");
	else if(marks >= 50)
		printf("Your Grade is :		D");
	else
		printf("Your Grade is :		F");
}

