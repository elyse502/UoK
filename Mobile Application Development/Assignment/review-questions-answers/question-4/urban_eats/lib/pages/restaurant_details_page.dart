import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../data/mock_restaurants.dart';
import '../models/restaurant.dart';
import 'package:intl/intl.dart'; // For currency formatting

class RestaurantDetailsPage extends StatelessWidget {
  final String restaurantId;

  const RestaurantDetailsPage({Key? key, required this.restaurantId})
    : super(key: key);

  @override
  Widget build(BuildContext context) {
    final Restaurant? restaurant = mockRestaurants.firstWhere(
      (r) => r.id == restaurantId,
      orElse: () =>
          throw Exception('Restaurant not found'), // Handle invalid ID
    );

    if (restaurant == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Error')),
        body: const Center(child: Text('Restaurant not found!')),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: Text(restaurant.name),
        centerTitle: true,
        leading: IconButton(
          // Explicit back button
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () {
            if (context.canPop()) {
              context.pop();
            } else {
              context.go('/'); // Fallback to DiscoveryPage
            }
          },
        ),
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Restaurant Image
            Image.network(
              restaurant.imageUrl,
              height: 250,
              width: double.infinity,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) {
                return Container(
                  height: 250,
                  color: Colors.grey[300],
                  child: const Center(
                    child: Icon(
                      Icons.broken_image,
                      color: Colors.grey,
                      size: 50,
                    ),
                  ),
                );
              },
            ),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Name, Cuisine, Address
                  Text(
                    restaurant.name,
                    style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                      fontWeight: FontWeight.bold,
                      color: Theme.of(context).primaryColor,
                    ),
                  ),
                  const SizedBox(height: 8.0),
                  Text(
                    restaurant.cuisine,
                    style: Theme.of(context).textTheme.titleMedium?.copyWith(
                      fontStyle: FontStyle.italic,
                      color: Colors.grey[800],
                    ),
                  ),
                  const SizedBox(height: 4.0),
                  Text(
                    restaurant.address,
                    style: Theme.of(
                      context,
                    ).textTheme.bodyLarge?.copyWith(color: Colors.grey[700]),
                  ),
                  const SizedBox(height: 16.0),
                  // Rating
                  Row(
                    children: [
                      Icon(Icons.star, color: Colors.amber[700], size: 24),
                      const SizedBox(width: 8.0),
                      Text(
                        '${restaurant.rating.toStringAsFixed(1)} / 5.0',
                        style: Theme.of(context).textTheme.headlineSmall,
                      ),
                    ],
                  ),
                  const SizedBox(height: 24.0),
                  // Menu Section Title
                  Text(
                    'Menu',
                    style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                      fontWeight: FontWeight.bold,
                      color: Theme.of(context).primaryColor,
                    ),
                  ),
                  const SizedBox(height: 16.0),
                  // Menu List
                  ListView.separated(
                    shrinkWrap:
                        true, // Important for ListView inside SingleChildScrollView
                    physics:
                        const NeverScrollableScrollPhysics(), // Disable ListView's own scrolling
                    itemCount: restaurant.menu.length,
                    itemBuilder: (context, index) {
                      final menuItem = restaurant.menu[index];
                      return ListTile(
                        title: Text(
                          menuItem.name,
                          style: Theme.of(context).textTheme.titleMedium,
                        ),
                        trailing: Text(
                          NumberFormat.currency(
                            locale: 'en_US',
                            symbol: '\$',
                          ).format(menuItem.price),
                          style: Theme.of(context).textTheme.titleMedium
                              ?.copyWith(
                                fontWeight: FontWeight.bold,
                                color: Theme.of(context)
                                    .colorScheme
                                    .secondary, // Use secondary color for price
                              ),
                        ),
                      );
                    },
                    separatorBuilder: (context, index) => const Divider(),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
