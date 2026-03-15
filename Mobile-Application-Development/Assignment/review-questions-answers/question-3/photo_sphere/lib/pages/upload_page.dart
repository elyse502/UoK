import 'dart:io'; // Required for File
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:image_picker/image_picker.dart';
import 'dart:ui'; // Import dart:ui for PathMetric

// CustomPainter for drawing a dashed border
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

    // Create the rounded rectangle path
    final RRect rRect = RRect.fromRectAndRadius(
      Rect.fromLTWH(0, 0, size.width, size.height),
      Radius.circular(radius),
    );

    // Add the rounded rectangle to the path
    path.addRRect(rRect);

    // Iterate over the path segments to draw dashes
    for (PathMetric metric in path.computeMetrics()) {
      double distance = 0.0;
      while (distance < metric.length) {
        // Draw a dash segment
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
    // Only repaint if the properties have changed
    return oldDelegate is! DashedBorderPainter ||
        oldDelegate.radius != radius ||
        oldDelegate.color != color ||
        oldDelegate.strokeWidth != strokeWidth;
  }
}

class UploadPage extends StatefulWidget {
  const UploadPage({Key? key}) : super(key: key);

  @override
  State<UploadPage> createState() => _UploadPageState();
}

class _UploadPageState extends State<UploadPage> {
  final _formKey = GlobalKey<FormState>();
  final _captionController = TextEditingController();
  final _locationController = TextEditingController();

  XFile? _selectedImage; // Stores the selected image file

  @override
  void dispose() {
    _captionController.dispose();
    _locationController.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final ImagePicker picker = ImagePicker();
    final XFile? image = await picker.pickImage(source: ImageSource.gallery);

    if (image != null) {
      setState(() {
        _selectedImage = image; // Update the state with the selected image
      });
    }
  }

  void _sharePost() {
    if (_selectedImage == null) {
      // Show SnackBar if no image is selected
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select an image to share.')),
      );
      return; // Stop execution
    }

    if (_formKey.currentState!.validate()) {
      // Form is valid and image is selected
      debugPrint('Sharing Post:');
      debugPrint('  Caption: ${_captionController.text}');
      debugPrint(
        '  Location: ${_locationController.text.isEmpty ? 'N/A' : _locationController.text}',
      );
      debugPrint('  Image Path: ${_selectedImage!.path}');

      // Show success SnackBar
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Post shared successfully!')),
      );

      // Navigate back to the FeedPage
      context.go(
        '/',
      ); // Or context.pop() if you want to go back to the previous route
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('New Post'),
        centerTitle: true,
        leading: IconButton(
          // Explicit back button for UploadPage
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () {
            if (context.canPop()) {
              context.pop(); // Go back to previous route (FeedPage)
            } else {
              context.go('/'); // Fallback to FeedPage if no route to pop
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
              // Image selection area
              GestureDetector(
                onTap: _pickImage, // Tap to pick an image
                child: CustomPaint(
                  // <--- Wrap with CustomPaint for the dashed border
                  painter: DashedBorderPainter(
                    color: Colors.grey[400]!,
                    radius: 12.0,
                    strokeWidth: 2.0,
                  ),
                  child: Container(
                    height: 200,
                    decoration: BoxDecoration(
                      color: Colors.grey[200],
                      borderRadius: BorderRadius.circular(12.0),
                      // No border property here, CustomPaint handles it
                    ),
                    child: _selectedImage != null
                        ? ClipRRect(
                            borderRadius: BorderRadius.circular(10.0),
                            child: Image.file(
                              File(
                                _selectedImage!.path,
                              ), // Display selected image
                              fit: BoxFit.cover,
                              width: double.infinity,
                              height: double.infinity,
                            ),
                          )
                        : Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(
                                Icons.add_a_photo,
                                size: 50,
                                color: Colors.grey[600],
                              ),
                              const SizedBox(height: 8.0),
                              Text(
                                'Tap to select image',
                                style: TextStyle(color: Colors.grey[700]),
                              ),
                            ],
                          ),
                  ),
                ),
              ),
              const SizedBox(height: 24.0),
              // Caption TextFormField
              TextFormField(
                controller: _captionController,
                decoration: const InputDecoration(
                  labelText: 'Caption',
                  hintText: 'What\'s on your mind?',
                  prefixIcon: Icon(Icons.text_fields),
                ),
                maxLines: 3,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Caption cannot be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              // Location TextFormField (Optional)
              TextFormField(
                controller: _locationController,
                decoration: const InputDecoration(
                  labelText: 'Location (Optional)',
                  hintText: 'Where was this photo taken?',
                  prefixIcon: Icon(Icons.location_on_outlined),
                ),
              ),
              const SizedBox(height: 24.0),
              // Share Button
              ElevatedButton.icon(
                onPressed: _sharePost,
                icon: const Icon(Icons.send),
                label: const Text(
                  'Share Post',
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
