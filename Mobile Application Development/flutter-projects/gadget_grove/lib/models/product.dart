class Product {
  final String id;
  final String name;
  final String brand;
  final double price;
  final String imageUrl;
  final bool inStock;

  Product({
    required this.id,
    required this.name,
    required this.brand,
    required this.price,
    required this.imageUrl,
    required this.inStock, 
  });

  String checkAvailability() => inStock ? 'In Stock' : 'Out of Stock';
}