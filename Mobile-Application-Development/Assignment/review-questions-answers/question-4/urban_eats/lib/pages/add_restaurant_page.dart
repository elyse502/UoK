import 'dart:io'; // Required for File
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:image_picker/image_picker.dart';
import 'dart:ui'; // For PathMetric in DashedBorderPainter

import '../data/mock_restaurants.dart';
import '../models/restaurant.dart';
import '../models/menu_item.dart'; // Import MenuItem for the mock menu

// CustomPainter for drawing a dashed border (reused from PhotoSphere's UploadPage)
class DashedBorderPainter extends CustomPainter {
  final double radius;
  final Color color;
  final double strokeWidth;

  DashedBorderPainter({
    this.radius = 12.0,
    this.color = Colors.grey,
    this.strokeWidth = 2.0,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final Paint paint = Paint()
      ..color = color
      ..strokeWidth = strokeWidth
      ..style = PaintingStyle.stroke;

    final Path path = Path();
    final double dashWidth = 10.0;
    final double dashSpace = 5.0;

    final RRect rRect = RRect.fromRectAndRadius(
      Rect.fromLTWH(0, 0, size.width, size.height),
      Radius.circular(radius),
    );

    path.addRRect(rRect);

    for (PathMetric metric in path.computeMetrics()) {
      double distance = 0.0;
      while (distance < metric.length) {
        canvas.drawPath(
          metric.extractPath(distance, distance + dashWidth),
          paint,
        );
        distance += dashWidth + dashSpace;
      }
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return oldDelegate is! DashedBorderPainter ||
        oldDelegate.radius != radius ||
        oldDelegate.color != color ||
        oldDelegate.strokeWidth != strokeWidth;
  }
}

class AddRestaurantPage extends StatefulWidget {
  const AddRestaurantPage({Key? key}) : super(key: key);

  @override
  State<AddRestaurantPage> createState() => _AddRestaurantPageState();
}

class _AddRestaurantPageState extends State<AddRestaurantPage> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _cuisineController = TextEditingController();
  final _addressController = TextEditingController();

  XFile? _selectedImage; // Stores the selected image file

  @override
  void dispose() {
    _nameController.dispose();
    _cuisineController.dispose();
    _addressController.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final ImagePicker picker = ImagePicker();
    final XFile? image = await picker.pickImage(source: ImageSource.gallery);

    if (image != null) {
      setState(() {
        _selectedImage = image;
      });
    }
  }

  void _submitForm() {
    // 1. Check if an image has been selected
    if (_selectedImage == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select a restaurant image.')),
      );
      return; // Stop if no image
    }

    // 2. Trigger form validation
    if (_formKey.currentState!.validate()) {
      // If image is present and form fields are valid
      final newRestaurant = Restaurant(
        id: 'res-${DateTime.now().millisecondsSinceEpoch}', // Simple unique ID
        name: _nameController.text,
        cuisine: _cuisineController.text,
        address: _addressController.text,
        rating: 0.0, // Default rating for new restaurants
        imageUrl: _selectedImage!.path, // Using local path for now
        menu: [
          // Mock menu items for new restaurant
          MenuItem(id: 'new-menu-1', name: 'New Dish 1', price: 10.00),
          MenuItem(id: 'new-menu-2', name: 'New Dish 2', price: 15.00),
        ],
      );

      addRestaurant(newRestaurant); // Add to mock data

      // Print all captured information to the console
      debugPrint('Restaurant Submitted:');
      debugPrint('  Name: ${newRestaurant.name}');
      debugPrint('  Cuisine: ${newRestaurant.cuisine}');
      debugPrint('  Address: ${newRestaurant.address}');
      debugPrint('  Image Path: ${newRestaurant.imageUrl}');

      // Show success SnackBar
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Restaurant submitted for review!')),
      );

      // Navigate the user back to the DiscoveryPage
      context.go('/'); // Navigate to the root (DiscoveryPage)
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Add New Restaurant'),
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
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // Image Picker UI
              GestureDetector(
                onTap: _pickImage,
                child: CustomPaint(
                  painter: DashedBorderPainter(
                    color: Colors.grey[400]!,
                    radius: 12.0,
                    strokeWidth: 2.0,
                  ),
                  child: Container(
                    height: 150, // Height as per requirement
                    decoration: BoxDecoration(
                      color: Colors.grey[200],
                      borderRadius: BorderRadius.circular(12.0),
                    ),
                    child: _selectedImage != null
                        ? ClipRRect(
                            borderRadius: BorderRadius.circular(10.0),
                            child: Image.file(
                              File(_selectedImage!.path),
                              fit: BoxFit.cover,
                              width: double.infinity,
                              height: double.infinity,
                            ),
                          )
                        : Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(
                                Icons.image,
                                size: 40,
                                color: Colors.grey[600],
                              ),
                              const SizedBox(height: 8.0),
                              Text(
                                'Tap to select restaurant image',
                                style: TextStyle(color: Colors.grey[700]),
                              ),
                            ],
                          ),
                  ),
                ),
              ),
              const SizedBox(height: 24.0),
              // Name TextFormField
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(
                  labelText: 'Restaurant Name',
                  hintText: 'e.g., The Golden Spoon',
                  prefixIcon: Icon(Icons.restaurant),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Name cannot be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              // Cuisine TextFormField
              TextFormField(
                controller: _cuisineController,
                decoration: const InputDecoration(
                  labelText: 'Cuisine',
                  hintText: 'e.g., Italian, Indian, Japanese',
                  prefixIcon: Icon(Icons.food_bank),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Cuisine cannot be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              // Address TextFormField
              TextFormField(
                controller: _addressController,
                decoration: const InputDecoration(
                  labelText: 'Address',
                  hintText: 'e.g., 123 Main St, Anytown',
                  prefixIcon: Icon(Icons.location_on),
                ),
                maxLines: 2,
                validator: (value) {
                  if (value == null || value.length < 10) {
                    return 'Address must be at least 10 characters long';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24.0),
              // Submit Restaurant Button
              ElevatedButton.icon(
                onPressed: _submitForm, // Single function for submission
                icon: const Icon(Icons.check),
                label: const Text(
                  'Submit Restaurant',
                  style: TextStyle(fontSize: 18.0),
                ),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Theme.of(context).primaryColor,
                  foregroundColor: Colors.white,
                  minimumSize: const Size.fromHeight(50),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
