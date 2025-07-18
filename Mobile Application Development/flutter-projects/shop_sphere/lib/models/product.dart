class Product {
  final String id;
  final String name;
  final double price;
  final String description;
  final String returnPolicy;
  final double shippingCost;
  final String imageUrl;

  const Product({
    required this.id,
    required this.name,
    required this.price,
    required this.description,
    required this.returnPolicy,
    required this.shippingCost,
    required this.imageUrl,
  });

  double get totalPrice => price + shippingCost;
}
