#include <iostream>
#include <cmath>

using namespace std;

// Define the elliptic curve parameters
const int a = 0; // Replace with the actual 'a' coefficient of your curve
const int b = 1; // Replace with the actual 'b' coefficient of your curve
//const int prime = 499; // Replace with the prime modulus of your finite field

// Define a structure to represent points on the elliptic curve
struct Point {
    int x, y;
};

int InverseModulo(int u, int m) {
    for (int n = 1; n < m; n++) {
        if ((u * n) % m == 1)
            return n;
    }
    return -1; // Indicate failure to find a modular inverse.
}

int gcd(int qq, int q) {
if (q == 0)
return qq;
else
return gcd(q, qq % q);
}

int modularExponentiation(int base, int exp, int mod) {
    int result = 1;
    base = base % mod;
    while (exp > 0) {
        if (exp % 2 == 1)
            result = (result * base) % mod;
        base = (base * base) % mod;
        exp = exp / 2;
    }
    return result;
}

int main() 
{ 

int pm,e,d, message,Decrypted_Message,Encrypted_Message,summation,difference, bas,count= 0;
cout<<" \t"; cout<<" enter prime number pm:"<<endl;
 cin>>pm;
int kk=pm-1;
//cout<<" numbers less than "<<p<<" and coprime to "<<kk<<" are:"<<endl;

	
	for (int message=0; message<pm; message++)
	
{
  
	//Decrypted_Message=(( Encrypted_Message*d)%p);

	
//............................................................................................
    int square,k,Mx,My,Bx,By, t,check=0,senderPkey,receiverPkey;
Mx=message;

square=(( Mx*Mx*Mx + a*Mx +b)% pm);

for (int t=0;t<pm; t++ ) // checking whether a y-coordinate corresponding to x
{
	if (((t*t)%pm)==square)
	{
	check++;
 
	My=t;
if (check==1)
{
	//cout<<check<<")"<<message<<") "<< Mx<<","<<My<<endl;
cout<<" \t"; cout<<"Message= "<<message<<" Generates  ("<< Mx<<","<<My<<")   which satisfies the equation of an elliptic curve"<<endl;//without displaying the second point related to the point
}
	
	}
}
    


  
}     
 
	
}

