#include <iostream>

using namespace std;

int main() {
	int food, size, quantity, price;
	cout << "____________UNIVERSITY OF KIGALI CANTEEN____________\n";
	cout << "______________________________________________________\n";
	cout << "  Choice   |  Food    |  Big Plate  |  Medium Plate  |\n";
	cout << "  No.      |          |  price      |  price         |\n";
	cout << "___________|__________|_____________|________________|\n";
	cout << "1.         |  Chips   |  2000       |  1000          |\n";
	cout << "___________|__________|_____________|________________|\n";
	cout << "2.         |  Rice    |  7500       |  3500          |\n";
	cout << "___________|__________|_____________|________________|\n";
	cout << "3.         |  Matooke |  8000       |  4500          |\n";
	cout << "___________|__________|_____________|________________|\n";
	
	cout << endl << "HERE IS THE MENU BAR:\n____________________\n\n";
	cout << "1. Chips.\n2. Rice.\n3. Matooke.";
	cout << "\n\nWhat do you want to order?	";
	cin >> food;
	
	
	switch(food) {
		case 1: 
			cout << "\nChoose 1 for Big Plate OR Choose 2 For Medium Plate!\n";
			cout << "Enter Size of your choice:	";
	        cin >> size;
			switch(size)
			{
				case 1: 
					cout << "Quantity: ";
					cin >> quantity;
					
					price = 2000 * quantity;
			        cout << "YOUR TOTAL PRICE IS : " << price << endl;
			        break;
			        
			    case 2:
			    	cout << "Quantity: ";
	                cin >> quantity;
	                
			    	price = 1000 * quantity;
			        cout << "Your Total Price is : " << price << endl;
		    		break;
		    		
		    	default:
		    		cout << "Wrong choice! Try again...";
			}
			break;
		case 2:
			cout << "\nChoose 1 for Big Plate OR Choose 2 For Medium Plate!\n";
			cout << "Enter Size of your choice:	";
	        cin >> size;
			switch(size)
			{
				case 1: 
					cout << "Quantity: ";
					cin >> quantity;
					
					price = 7500 * quantity;
			        cout << "YOUR TOTAL PRICE IS : " << price << endl;
			        break;
			        
			    case 2:
			    	cout << "Quantity: ";
	                cin >> quantity;
	                
			    	price = 3500 * quantity;
			        cout << "Your Total Price is : " << price;
		    		break;
		    		
		    	default:
		    		cout << "Wrong choice! Try again...";
			}
			break;
		case 3:
			cout << "\nChoose 1 for Big Plate OR Choose 2 For Medium Plate!\n";
			cout << "Enter Size of your choice:	";
	        cin >> size;
			switch(size)
			{
				case 1: 
					cout << "Quantity: ";
					cin >> quantity;
					
					price = 8000 * quantity;
			        cout << "YOUR TOTAL PRICE IS : " << price << endl;
			        break;
			        
			    case 2:
			    	cout << "Quantity: ";
	                cin >> quantity;
	                
			    	price = 4500 * quantity;
			        cout << "Your Total Price is : " << price;
		    		break;
		    		
		    	default:
		    		cout << "Wrong choice! Try again...";
	
			}
			break;
		default:
			cout << "No such Food Type! Choose again..." << endl;
	}
	return 0;
}











