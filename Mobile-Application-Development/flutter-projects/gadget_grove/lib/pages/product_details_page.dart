import 'package:flutter/material.dart';
import 'package:gadget_grove/models/product.dart';
import 'package:gadget_grove/data/mock_data.dart';

class ProductDetailsPage extends StatelessWidget {
  final String productId;

  const ProductDetailsPage({super.key, required this.productId});

  @override
  Widget build(BuildContext context) {
    final Product prodt = mockData.firstWhere(
      (prod) => prod.id == productId,
      orElse: () {
        return Product(
          id: 'error',
          name: 'Gadget Not Found',
          brand: 'Unknown',
          price: 0.0,
          imageUrl: '',
          inStock: false,
        );
      },
    );
    return Scaffold(
      appBar: AppBar(title: Text(prodt.name)),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Image.network(
              prodt.imageUrl,
              width: double.infinity,
              height: 300,
              fit: BoxFit.cover,
            ),
            Padding(
              padding: EdgeInsets.all(12.0),
              child: Text(
                'Product Details for ID: $productId',
                style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
