import 'package:flutter/material.dart';
import 'package:shop_sphere/models/product.dart';
import 'package:go_router/go_router.dart';

class ProductCard extends StatelessWidget {
  final Product prod;
  const ProductCard({super.key, required this.prod});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        context.go('/product/${prod.id}');
      },
      child: Card(
        elevation: 2.0,
        margin: const EdgeInsets.symmetric(horizontal: 12.0, vertical: 8.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            ClipRRect(
              borderRadius: const BorderRadius.only(
                topLeft: Radius.circular(8.0),
                topRight: Radius.circular(8.0),
              ),
              child: Image.network(
                prod.imageUrl,
                height: 150.0,
                width: double.infinity,
                fit: BoxFit.cover,
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    prod.name,
                    style: TextStyle(
                      fontSize: 20.0,
                      fontWeight: FontWeight.bold,
                    ),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 4.0),
                  Text(
                    prod.description,
                    style: TextStyle(fontSize: 14.0, color: Colors.grey[600]),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 5.0),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            'Shipping: \$${prod.shippingCost.toStringAsFixed(2)}',
                            style: TextStyle(
                              fontSize: 12.0,
                              fontStyle: FontStyle.italic,
                              color: Colors.green[700],
                            ),
                          ),
                          const SizedBox(height: 5.0),
                          Text(
                            'Price: \$${prod.price.toStringAsFixed(2)}',
                            style: TextStyle(
                              fontSize: 14.0,
                              fontStyle: FontStyle.italic,
                              color: Colors.red[700],
                            ),
                          ),
                        ],
                      ),
                      Text(
                        'Return Policy: ${prod.returnPolicy}',
                        style: TextStyle(
                          fontSize: 12.0,
                          fontStyle: FontStyle.italic,
                          color: Colors.grey[500],
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 6.0),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        '\$${prod.totalPrice.toStringAsFixed(2)}',
                        style: TextStyle(
                          fontSize: 18.0,
                          fontWeight: FontWeight.w600,
                          color: Theme.of(context).colorScheme.primary,
                        ),
                      ),
                      ElevatedButton(
                        onPressed: () {},
                        child: Icon(Icons.add_shopping_cart),
                      ),
                    ],
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
