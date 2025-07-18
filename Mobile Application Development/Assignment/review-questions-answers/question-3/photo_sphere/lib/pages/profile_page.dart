import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../data/mock_photos_data.dart';
import '../models/user.dart';
import '../models/post.dart';

class ProfilePage extends StatelessWidget {
  final String userId;

  const ProfilePage({Key? key, required this.userId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final User? user = mockUsers.firstWhere(
      (u) => u.userId == userId,
      orElse: () {
        debugPrint('User not found for ID: $userId');
        throw Exception(
          'User not found',
        ); // Handle case where user ID is invalid
      },
    );

    if (user == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Error')),
        body: const Center(child: Text('User not found!')),
      );
    }

    final List<Post> userPosts = mockPosts
        .where((post) => post.userId == userId)
        .toList();

    return Scaffold(
      appBar: AppBar(
        title: Text('${user.username}\'s Profile'),
        centerTitle: true,
        leading: IconButton(
          // Explicit back button for ProfilePage
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
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Header Section: Avatar, Username, Metrics
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  CircleAvatar(
                    radius: 50,
                    backgroundImage: NetworkImage(user.avatarUrl),
                    onBackgroundImageError: (exception, stackTrace) {
                      debugPrint('Error loading profile avatar: $exception');
                    },
                  ),
                  const SizedBox(height: 16.0),
                  Text(
                    user.username,
                    style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 8.0),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      _buildMetricCounter(context, userPosts.length, 'Posts'),
                      const SizedBox(width: 24.0),
                      _buildMetricCounter(
                        context,
                        1200,
                        'Followers',
                      ), // Static for now
                      const SizedBox(width: 24.0),
                      _buildMetricCounter(
                        context,
                        50,
                        'Following',
                      ), // Static for now
                    ],
                  ),
                ],
              ),
            ),
            const Divider(),
            // Grid of User's Posts
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                'Posts',
                style: Theme.of(
                  context,
                ).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
              ),
            ),
            userPosts.isEmpty
                ? const Center(
                    child: Padding(
                      padding: EdgeInsets.all(16.0),
                      child: Text('No posts yet.'),
                    ),
                  )
                : GridView.builder(
                    shrinkWrap:
                        true, // Important for GridView inside SingleChildScrollView
                    physics:
                        const NeverScrollableScrollPhysics(), // Disable GridView's own scrolling
                    gridDelegate:
                        const SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: 3, // Three columns
                          crossAxisSpacing: 2.0,
                          mainAxisSpacing: 2.0,
                        ),
                    itemCount: userPosts.length,
                    itemBuilder: (context, index) {
                      return Image.network(
                        userPosts[index].imageUrl,
                        fit: BoxFit.cover,
                        errorBuilder: (context, error, stackTrace) {
                          return Container(
                            color: Colors.grey[200],
                            child: const Center(
                              child: Icon(
                                Icons.broken_image,
                                color: Colors.grey,
                              ),
                            ),
                          );
                        },
                      );
                    },
                  ),
          ],
        ),
      ),
    );
  }

  Widget _buildMetricCounter(BuildContext context, int count, String label) {
    return Column(
      children: [
        Text(
          count.toString(),
          style: Theme.of(
            context,
          ).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold),
        ),
        Text(
          label,
          style: Theme.of(
            context,
          ).textTheme.bodySmall?.copyWith(color: Colors.grey[600]),
        ),
      ],
    );
  }
}
