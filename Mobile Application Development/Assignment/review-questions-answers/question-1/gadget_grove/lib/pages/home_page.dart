import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../data/mock_products.dart';
import '../widgets/product_tile.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[100],
      appBar: AppBar(
        elevation: 0,
        backgroundColor: Colors.white,
        foregroundColor: Colors.black87,
        title: Row(
          children: [
            Image.asset(
              'assets/images/logo.png', // Add a logo in assets/images folder
              width: 120,
              height: 80,
            ),
            const SizedBox(width: 8),
            const Text(
              "GadgetGrove",
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.shopping_cart_outlined),
            onPressed: () => context.push('/checkout'),
          ),
        ],
      ),
      body: Column(
        children: [
          // Optional promotional banner
          Container(
            margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            padding: const EdgeInsets.all(20),
            decoration: BoxDecoration(
              color: Colors.teal[100],
              borderRadius: BorderRadius.circular(16),
              image: const DecorationImage(
                image: AssetImage(
                  'assets/images/banner.jpg',
                ), // optional banner image
                fit: BoxFit.cover,
                opacity: 0.2,
              ),
            ),
            child: Row(
              children: [
                const Icon(Icons.discount, color: Colors.teal, size: 32),
                const SizedBox(width: 12),
                Expanded(
                  child: Text(
                    "Get 10% off on your first order!",
                    style: Theme.of(context).textTheme.titleMedium,
                  ),
                ),
              ],
            ),
          ),

          // Section Title
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 16, 8),
            child: Row(
              children: [
                const Icon(Icons.star, color: Colors.amber),
                const SizedBox(width: 8),
                Text(
                  "Top Picks For You",
                  style: Theme.of(
                    context,
                  ).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
                ),
              ],
            ),
          ),

          // Grid of Products
          Expanded(
            child: GridView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                mainAxisSpacing: 12,
                crossAxisSpacing: 12,
                childAspectRatio: 0.75,
              ),
              itemCount: mockProducts.length,
              itemBuilder: (context, index) {
                return ProductTile(product: mockProducts[index]);
              },
            ),
          ),
        ],
      ),
    );
  }
}
