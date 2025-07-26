import 'package:flutter/material.dart';

class ProductUploadPage extends StatefulWidget {
  const ProductUploadPage({super.key});

  @override
  State<ProductUploadPage> createState() => _ProductUploadPageState();
}

class _ProductUploadPageState extends State<ProductUploadPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(elevation: 4.0, title: Text("Upload Product")),
    );
  }
}
