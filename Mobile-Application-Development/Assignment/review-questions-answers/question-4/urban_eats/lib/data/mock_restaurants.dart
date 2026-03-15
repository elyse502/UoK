import '../models/restaurant.dart';
import '../models/menu_item.dart';

final List<Restaurant> mockRestaurants = [
  Restaurant(
    id: 'res-001',
    name: 'The Golden Spoon',
    cuisine: 'Italian',
    address: '123 Main St, Metropolis',
    rating: 4.7,
    imageUrl:
        'https://images.unsplash.com/photo-1612733493411-898828d89df0?q=80&w=1191&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    menu: [
      MenuItem(id: 'menu-001', name: 'Spaghetti Carbonara', price: 18.99),
      MenuItem(id: 'menu-002', name: 'Margherita Pizza', price: 15.50),
      MenuItem(id: 'menu-003', name: 'Tiramisu', price: 8.00),
    ],
  ),
  Restaurant(
    id: 'res-002',
    name: 'Spice Route',
    cuisine: 'Indian',
    address: '456 Oak Ave, Gotham City',
    rating: 4.5,
    imageUrl:
        'https://plus.unsplash.com/premium_photo-1671639169260-18cec0fa6a51?q=80&w=1333&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    menu: [
      MenuItem(id: 'menu-004', name: 'Chicken Tikka Masala', price: 22.00),
      MenuItem(id: 'menu-005', name: 'Garlic Naan', price: 4.50),
      MenuItem(id: 'menu-006', name: 'Vegetable Biryani', price: 17.50),
    ],
  ),
  Restaurant(
    id: 'res-003',
    name: 'Sushi Zen',
    cuisine: 'Japanese',
    address: '789 Pine Ln, Star City',
    rating: 4.8,
    imageUrl:
        'https://images.unsplash.com/photo-1726824863833-e88146cf0a72?q=80&w=1169&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    menu: [
      MenuItem(id: 'menu-007', name: 'Assorted Sushi Platter', price: 35.00),
      MenuItem(id: 'menu-008', name: 'Ramen Noodle Soup', price: 16.00),
      MenuItem(id: 'menu-009', name: 'Green Tea Ice Cream', price: 7.00),
    ],
  ),
  Restaurant(
    id: 'res-004',
    name: 'The Burger Joint',
    cuisine: 'American',
    address: '101 Elm St, Central City',
    rating: 4.2,
    imageUrl:
        'https://images.unsplash.com/photo-1695606392987-9635caaf7f74?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    menu: [
      MenuItem(id: 'menu-010', name: 'Classic Cheeseburger', price: 14.00),
      MenuItem(id: 'menu-011', name: 'Fries', price: 5.00),
      MenuItem(id: 'menu-012', name: 'Milkshake', price: 7.50),
    ],
  ),
  Restaurant(
    id: 'res-005',
    name: 'La Boulangerie',
    cuisine: 'French',
    address: '222 Maple Dr, Bludhaven',
    rating: 4.6,
    imageUrl:
        'https://images.unsplash.com/photo-1703529027195-ebb6229791c9?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    menu: [
      MenuItem(id: 'menu-013', name: 'Croissant', price: 4.00),
      MenuItem(id: 'menu-014', name: 'Quiche Lorraine', price: 12.00),
      MenuItem(id: 'menu-015', name: 'Cafe au Lait', price: 5.00),
    ],
  ),
];

// Function to add a new restaurant (for demonstration purposes)
void addRestaurant(Restaurant newRestaurant) {
  mockRestaurants.add(newRestaurant);
}
