#include <stdio.h>

int main(){
	int a,sum = 0,i;
	printf("Numbers:");
	scanf("%d",&a);
	int array[a];
	for(i = 0;i<a;i++){
		printf("Enter number %d: ",(i+1));
		scanf("%d",&array[i]);
	}
	for(i=0;i<a;i++){
		sum+=array[i];
	}
	printf("The sum is: %d",sum);
	return 0;
}