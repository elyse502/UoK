import 'package:flutter/material.dart';
import '../models/product.dart';
import 'package:go_router/go_router.dart';

class ProductTile extends StatelessWidget {
  final Product product;

  const ProductTile({required this.product});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => context.push('/product/${product.id}'),
      child: Card(
        child: Stack(
          children: [
            Column(
              children: [
                Expanded(
                  child: Image.network(product.imageUrl, fit: BoxFit.cover),
                ),
                ListTile(
                  title: Text(product.name),
                  subtitle: Text(product.brand),
                ),
              ],
            ),
            Positioned(
              right: 8,
              bottom: 8,
              child: Container(
                width: 16,
                height: 16,
                decoration: BoxDecoration(
                  color: product.inStock ? Colors.green : Colors.grey,
                  shape: BoxShape.circle,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
