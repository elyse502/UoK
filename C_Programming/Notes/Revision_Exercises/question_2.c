#include <stdio.h>

int main(){
	int a,product = 1,i;
	printf("Enter n:");
	scanf("%d",&a);
	int arrayone[a];
	for(i = 0;i<a;i++){
		printf("Enter number %d: ",(i+1));
		scanf("%d",&arrayone[i]);
	}
	for(i=0;i<a;i++){
		product*=arrayone[i];
	}
	printf("The product is = %d",product);
}