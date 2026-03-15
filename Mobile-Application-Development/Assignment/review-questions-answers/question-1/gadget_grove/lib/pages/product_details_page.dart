import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../data/mock_products.dart';

class ProductDetailsPage extends StatefulWidget {
  final String id;
  const ProductDetailsPage({required this.id});

  @override
  State<ProductDetailsPage> createState() => _ProductDetailsPageState();
}

class _ProductDetailsPageState extends State<ProductDetailsPage> {
  final TextEditingController _quantityController = TextEditingController(
    text: '1',
  );

  @override
  Widget build(BuildContext context) {
    final product = mockProducts.firstWhere((p) => p.id == widget.id);

    return Scaffold(
      appBar: AppBar(
        title: Text(product.name),
        actions: [
          IconButton(
            icon: Icon(Icons.shopping_cart),
            onPressed: () => context.push('/checkout'),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Card(
          elevation: 4,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Product Image
              ClipRRect(
                borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
                child: Image.network(product.imageUrl, fit: BoxFit.cover),
              ),

              // Product Info
              Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Name & Brand
                    Text(
                      product.name,
                      style: Theme.of(context).textTheme.titleLarge,
                    ),
                    Text(
                      product.brand,
                      style: TextStyle(color: Colors.grey[700]),
                    ),

                    SizedBox(height: 10),

                    // Price
                    Text(
                      "\$${product.price.toStringAsFixed(2)}",
                      style: TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                        color: Colors.teal,
                      ),
                    ),

                    SizedBox(height: 10),

                    // Description
                    Text(
                      "This is a high-tech gadget ideal for modern users looking to enhance productivity and connectivity.",
                      style: TextStyle(fontSize: 14, color: Colors.grey[800]),
                    ),

                    SizedBox(height: 20),

                    // Quantity input
                    Row(
                      children: [
                        Expanded(
                          child: TextFormField(
                            controller: _quantityController,
                            keyboardType: TextInputType.number,
                            decoration: InputDecoration(
                              labelText: 'Quantity',
                              border: OutlineInputBorder(),
                              prefixIcon: Icon(Icons.confirmation_number),
                            ),
                          ),
                        ),
                        SizedBox(width: 16),
                        // Stock Badge
                        Chip(
                          label: Text(
                            product.inStock ? "In Stock" : "Out of Stock",
                          ),
                          backgroundColor: product.inStock
                              ? Colors.green[100]
                              : Colors.grey[300],
                          avatar: Icon(
                            product.inStock ? Icons.check_circle : Icons.block,
                            color: product.inStock ? Colors.green : Colors.grey,
                          ),
                        ),
                      ],
                    ),

                    SizedBox(height: 20),

                    // Add to Cart Button
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton.icon(
                        icon: Icon(Icons.shopping_bag),
                        label: Text("Add to Cart"),
                        onPressed: product.inStock
                            ? () {
                                final qty =
                                    int.tryParse(_quantityController.text) ?? 1;
                                ScaffoldMessenger.of(context).showSnackBar(
                                  SnackBar(
                                    content: Text(
                                      "Added $qty x ${product.name} to cart.",
                                    ),
                                    backgroundColor: Colors.green,
                                  ),
                                );
                              }
                            : null,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
