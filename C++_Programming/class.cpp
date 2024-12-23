// A class is like a new data type. It's a specification/ blueprint of an object.
#include <iostream>
using namespace std;

class Book {
  public:
    string title;
    string author;
    int pages;
};

int main() {
  Book book1; // An object of the Book class(book1). In other words it's an instance of a class.
  book1.title = "Harry Potter";
  book1.author = "JK Rowling";
  book1.pages = 500;

  cout << "Book1 name: " << book1.title << endl;
  cout << "Book1 author: " << book1.author << endl;
  cout << "Book1 pages: " << book1.pages << endl;


  Book book2;
  book2.title = "Lord of the Rings";
  book2.author = "Tolkein";
  book2.pages = 700;
  book2.title = "Hunger Games"; // You can even modify the value of the title.

  cout << "\n\nBook2 name: " << book2.title << endl; // Book2 name is going to be updated to Hunger Games.
  cout << "Book2 author: " << book2.author << endl;
  cout << "Book2 pages: " << book2.pages << endl;
  
  return 0;
}