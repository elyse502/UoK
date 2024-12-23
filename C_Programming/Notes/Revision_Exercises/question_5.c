#include <stdio.h>

int main(){
	int a,small = 0,large = 0,i;
	printf("How many numbers for comparision?:");
	scanf("%d",&a);
	int array[a];
	for(i = 0;i<a;i++){
		printf("Enter number %d: ",(i+1));
		scanf("%d",&array[i]);
	}
	large = array[0];
	small = array[0];
	for(i=0;i<a;i++){
		if(array[i] > large){
			large = array[i];
		}
		if(array[i] < small){
			small = array[i];
		}
	}
	printf("> The small number is %d\n> The large number is %d",small,large);
	return 0;
}


