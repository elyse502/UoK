import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:shop_sphere/data/mock_data.dart';
import 'package:shop_sphere/widget/product_card.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 2,
        actions: [
          IconButton(
            icon: Icon(Icons.shopping_cart_outlined),
            onPressed: () {},
          ),
          PopupMenuButton<String>(
            onSelected: (value) {
              if (value == 'Register') {
                context.push('/register');
              } else {
                context.push('/product-upload');
              }
            },
            itemBuilder: (context) => <PopupMenuEntry<String>>[
              const PopupMenuItem<String>(
                value: 'Register',
                child: Text('Register'),
              ),
              const PopupMenuItem<String>(
                value: 'Upload',
                child: Text('Upload Product'),
              ),
            ],
          ),
        ],
        leading: Icon(Icons.menu),
        title: Text("ShopSphere", style: TextStyle(color: Colors.black)),
        // backgroundColor: Colors.blue,
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              "Featured Products",
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: mockData.length,
              itemBuilder: (context, int index) {
                return ProductCard(prod: mockData[index]);
              },
            ),
          ),
        ],
      ),
      // body: Center(child: Text("Welcome to ShopSphere App!")),
    );
  }
}
