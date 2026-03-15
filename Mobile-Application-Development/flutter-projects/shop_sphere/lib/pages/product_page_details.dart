import 'package:flutter/material.dart';
import 'package:shop_sphere/models/product.dart';
import 'package:shop_sphere/data/mock_data.dart';

class ProductPageDetails extends StatelessWidget {
  final String productId;

  const ProductPageDetails({super.key, required this.productId});

  @override
  Widget build(BuildContext context) {
    final Product prod = mockData.firstWhere(
      (p) => p.id == productId,
      orElse: () {
        return const Product(
          id: 'error',
          name: 'Product Not Found',
          price: 0,
          imageUrl: '',
          description: '',
          returnPolicy: '',
          shippingCost: 0,
        );
      },
    );
    return Scaffold(
      appBar: AppBar(title: Text(prod.name)),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Image.network(
              prod.imageUrl,
              width: double.infinity,
              height: 300,
              fit: BoxFit.cover,
            ),
            Padding(
              padding: EdgeInsets.all(12.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Product Details for ID: $productId',
                    style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 10),
                  Text(prod.description, style: TextStyle(fontSize: 16)),
                  const SizedBox(height: 10),
                  Text(
                    'Price: \$${prod.price.toStringAsFixed(2)}',
                    style: TextStyle(fontSize: 20, color: Colors.green),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    'Shipping Cost: \$${prod.shippingCost.toStringAsFixed(2)}',
                    style: TextStyle(fontSize: 16, color: Colors.blueGrey),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    'Return Policy: ${prod.returnPolicy}',
                    style: TextStyle(fontSize: 16, color: Colors.redAccent),
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
