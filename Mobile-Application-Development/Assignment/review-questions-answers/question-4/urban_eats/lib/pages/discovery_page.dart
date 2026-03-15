import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../data/mock_restaurants.dart';
import '../widgets/restaurant_card.dart';

class DiscoveryPage extends StatelessWidget {
  const DiscoveryPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'UrbanEats 🍽️',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 30.0),
        ),
        centerTitle: true,
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Header Section
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Find Your Next Meal',
                  style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                    fontWeight: FontWeight.bold,
                    color: Theme.of(context).primaryColor,
                  ),
                ),
                const SizedBox(height: 16.0),
                TextField(
                  decoration: InputDecoration(
                    hintText: 'Search restaurants...',
                    prefixIcon: const Icon(Icons.search),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(12.0),
                      borderSide: BorderSide.none,
                    ),
                    filled: true,
                    fillColor: Colors.grey[100],
                    contentPadding: const EdgeInsets.symmetric(vertical: 12.0),
                  ),
                ),
              ],
            ),
          ),
          // List of Restaurants
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.symmetric(vertical: 8.0),
              itemCount: mockRestaurants.length,
              itemBuilder: (context, index) {
                final restaurant = mockRestaurants[index];
                return RestaurantCard(
                  key: ValueKey(
                    restaurant.id,
                  ), // Unique key for efficient updates
                  restaurant: restaurant,
                );
              },
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          context.go('/add-restaurant'); // Navigate to AddRestaurantPage
        },
        backgroundColor: Theme.of(context).primaryColor,
        child: const Icon(Icons.add, color: Colors.white),
        tooltip: 'Add New Restaurant',
      ),
    );
  }
}
