import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../data/mock_photos_data.dart';
import '../widgets/post_card.dart';

class FeedPage extends StatelessWidget {
  const FeedPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('PhotoSphere Feed'),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.camera_alt), // Camera icon for upload
            onPressed: () {
              context.go('/upload'); // Navigate to UploadPage
            },
          ),
        ],
      ),
      body: ListView.builder(
        padding: const EdgeInsets.symmetric(vertical: 8.0),
        itemCount: mockPosts.length,
        itemBuilder: (context, index) {
          return PostCard(post: mockPosts[index]);
        },
      ),
    );
  }
}
