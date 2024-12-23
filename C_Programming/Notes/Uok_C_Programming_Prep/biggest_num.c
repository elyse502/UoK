#include<stdio.h>

int main()
{
int a,b,c;
printf("Enter the numbers: ");
scanf("%d %d",&a,&b);
c=(a>b)?a:b;
printf("\nThe biggest number is %d\n\n", c);
c=(a<b)?a:b;
printf("The smallest number is %d\n\n", c);
int x, y;
printf("Enter the value of x = ");
scanf("%d", &x);
y=(x>5)?3:4;
printf("\nThe Right Answer is... %d\n\n",y);
int l,m,n;
// printf("Enter the values = ");
// scanf("%d%d",&l,&m);
n=(l<m)?4:5;
printf("The least value is... %d\n",n);
getchar();
}