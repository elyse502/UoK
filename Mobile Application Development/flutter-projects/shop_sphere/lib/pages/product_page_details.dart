import 'package:flutter/material.dart';
import 'package:shop_sphere/models/product.dart';
import 'package:shop_sphere/data/mock_data.dart';
import 'package:go_router/go_router.dart';

class ProductPageDetails extends StatelessWidget {
  final String productId;

  const ProductPageDetails({super.key, required this.productId});

  @override
  Widget build(BuildContext context) {
    final Product prod = mockData.firstWhere(
      (p) => p.id == productId,
      orElse: () => const Product(
        id: 'error',
        name: 'Product Not Found',
        price: 0,
        imageUrl: '',
        description: 'No description available.',
        returnPolicy: 'N/A',
        shippingCost: 0,
      ),
    );

    return Scaffold(
      appBar: AppBar(
        title: Text(prod.name),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.black),
          onPressed: () {
            if (context.canPop()) {
              context.pop();
            } else {
              context.go('/');
            }
          },
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Image or fallback
            prod.imageUrl.isNotEmpty
                ? Image.network(
                    prod.imageUrl,
                    width: double.infinity,
                    height: 250,
                    fit: BoxFit.cover,
                  )
                : Container(
                    width: double.infinity,
                    height: 250,
                    color: Colors.grey[300],
                    child: const Icon(Icons.image_not_supported, size: 48),
                  ),
            const SizedBox(height: 20),

            // Product Name
            Text(
              prod.name,
              style: Theme.of(
                context,
              ).textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),

            // Price & Shipping
            Text(
              "Price: \$${prod.price.toStringAsFixed(2)}",
              style: Theme.of(context).textTheme.bodyLarge,
            ),
            Text(
              "Shipping: \$${prod.shippingCost.toStringAsFixed(2)}",
              style: Theme.of(context).textTheme.bodyLarge,
            ),
            const SizedBox(height: 8),
            Text(
              "Total: \$${prod.totalPrice.toStringAsFixed(2)}",
              style: Theme.of(context).textTheme.titleMedium?.copyWith(
                color: Colors.green[700],
                fontWeight: FontWeight.w600,
              ),
            ),
            const SizedBox(height: 20),

            // Description
            Text("Description", style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 4),
            Text(
              prod.description,
              style: Theme.of(context).textTheme.bodyMedium,
            ),
            const SizedBox(height: 20),

            // Return Policy
            Text(
              "Return Policy",
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 4),
            Text(
              prod.returnPolicy,
              style: Theme.of(context).textTheme.bodyMedium,
            ),
            const SizedBox(height: 30),

            // Add to cart (for future functionality)
            Center(
              child: ElevatedButton.icon(
                onPressed: () {
                  // Placeholder action
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text("${prod.name} added to cart.")),
                  );
                },
                icon: const Icon(Icons.shopping_cart),
                label: const Text("Add to Cart"),
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 24,
                    vertical: 12,
                  ),
                  backgroundColor: Colors.teal,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
