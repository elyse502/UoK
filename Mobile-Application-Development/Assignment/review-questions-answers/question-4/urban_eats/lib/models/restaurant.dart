import 'menu_item.dart';

class Restaurant {
  final String id;
  final String name;
  final String cuisine;
  final String address;
  final double rating;
  final String imageUrl;
  final List<MenuItem> menu;

  Restaurant({
    required this.id,
    required this.name,
    required this.cuisine,
    required this.address,
    required this.rating,
    required this.imageUrl,
    required this.menu,
  });
}
