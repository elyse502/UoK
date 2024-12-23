#include <stdio.h>

int main(){
	int a,sum = 0,i;
	printf("How many numbers?:");
	scanf("%d",&a);
	int array[a];
	for(i = 0;i<a;i++){
		printf("Enter value %d: ",(i+1));
		scanf("%d",&array[i]);
	}
	for(i=0;i<a;i++){
		if(array[i] % 2 == 0){
			sum+=array[i];
		}
	}
	printf("Sum of even numbers = %d",sum);
	return 0;
}