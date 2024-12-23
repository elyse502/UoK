// A constructor is basically a special function that is going to get called whenever an object of a class is created.

/*#include <iostream>
using namespace std;

class Book {
  public:
    string title;
    string author;
    int pages;
    Book(string name) { // A constructor. It's going to be invoked each time we create an object. 
      // cout << "Creating object of Book class using the default constructor function." << endl;
      cout << name << endl;
    }
};

int main() {
  Book book1("Harry Potter"); 
  book1.title = "Harry Potter";
  book1.author = "JK Rowling";
  book1.pages = 500;

  Book book2("Lord of the Rings");
  book2.title = "Lord of the Rings";
  book2.author = "Tolkein";
  book2.pages = 700;
  


  return 0;
}*/

#include <iostream>
using namespace std;

class Book {
  public:
    string title;
    string author;
    int pages;

    Book() {
      title = "No title";
      author = "No author";
      pages = 0;
    }

    Book(string aTittle, string aAuthor, int aPages) {
      title = aTittle;
      author = aAuthor;
      pages = aPages;
    } 
};

int main() {
  Book book1("Harry Potter", "JK Rowling", 500);
  Book book2("Lord of the Rings", "Tolkein", 700);


  cout << book1.title << endl;
  cout << book1.author << endl;
  cout << book1.pages << "\n\n" << endl;

  Book book3;

  cout << book3.title << endl;
  cout << book3.author << endl;
  cout << book3.pages << endl;
  
  return 0;
}