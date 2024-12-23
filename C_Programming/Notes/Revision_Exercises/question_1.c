#include <stdio.h>

int main(){
	int a,i;
	printf("Enter n:");
	scanf("%d",&a);
	int array[a];
	for(i = 0;i<a;i++){
		printf("Enter number %d: ",(i+1));
		scanf("%d",&array[i]);
	}
	printf("\nPositive numbers: \n");
	for(i=0;i<a;i++){
		if(array[i] > 0){
			printf(" >> %d\n",array[i]);
		}
	}
}