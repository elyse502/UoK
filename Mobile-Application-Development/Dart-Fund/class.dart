class Product {
  String name;
  double price;
  int _inventoryCount;
  
  Product(this.name, this.price, this._inventoryCount);
  
  void displayInfo() {
    print('Name: $name \nPrice: $price \nIn Stock: ${_inventoryCount > 0}');
  }
}

class Book extends Product {
  String author;
  
  Book(String name, double price, int stock, this.author)
    :super(name, price, stock);
  
  @override
  void displayInfo(){
    super.displayInfo();
    print('Author: $author');
  }
}

void main(){
  var myBook = Book('The Dart Appentice', 29.99, 15, 'Danny');
  
  myBook.displayInfo();
}