import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../models/post.dart';
import '../models/user.dart';
import '../data/mock_photos_data.dart';

class PostCard extends StatelessWidget {
  final Post post;

  const PostCard({Key? key, required this.post}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Find the user associated with this post.
    // The orElse clause ensures postUser will never be null.
    final User postUser = mockUsers.firstWhere(
      // Changed User? to User
      (user) => user.userId == post.userId,
      orElse: () => mockUsers.first, // Fallback to a default user if not found
    );

    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
      elevation: 4.0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12.0)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Header Row: Avatar, Username
          Padding(
            padding: const EdgeInsets.all(12.0),
            child: Row(
              children: [
                CircleAvatar(
                  radius: 20,
                  backgroundImage: NetworkImage(
                    postUser.avatarUrl,
                  ), // Now safely accessed
                  onBackgroundImageError: (exception, stackTrace) {
                    debugPrint('Error loading avatar: $exception');
                    // Fallback to a placeholder if image fails to load
                    // You might want a local asset or a more robust placeholder here
                  },
                ),
                const SizedBox(width: 12.0),
                Expanded(
                  child: GestureDetector(
                    onTap: () {
                      // Navigate to ProfilePage when username is tapped
                      context.push('/profile/${postUser.userId}');
                    },
                    child: Text(
                      postUser.username,
                      style: const TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 16.0,
                      ),
                    ),
                  ),
                ),
                // Optional: More options icon
                // IconButton(icon: const Icon(Icons.more_vert), onPressed: () {}),
              ],
            ),
          ),
          // Post Image
          Image.network(
            post.imageUrl,
            width: double.infinity,
            height: 250,
            fit: BoxFit.cover,
            errorBuilder: (context, error, stackTrace) {
              debugPrint('Error loading post image: $error');
              return Container(
                height: 250,
                color: Colors.grey[300],
                child: const Center(
                  child: Icon(Icons.broken_image, color: Colors.grey, size: 50),
                ),
              );
            },
          ),
          // Action Buttons Row: Like, Comment, Share
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 4.0),
            child: Row(
              children: [
                IconButton(
                  icon: Icon(Icons.favorite_border, color: Colors.grey[700]),
                  onPressed: () {
                    // Implement like functionality (local for now)
                    debugPrint('Liked post ${post.postId}');
                  },
                ),
                Text('${post.likes} likes'),
                const SizedBox(width: 8.0),
                IconButton(
                  icon: Icon(Icons.comment_outlined, color: Colors.grey[700]),
                  onPressed: () {
                    debugPrint('Commented on post ${post.postId}');
                  },
                ),
                IconButton(
                  icon: Icon(Icons.share_outlined, color: Colors.grey[700]),
                  onPressed: () {
                    debugPrint('Shared post ${post.postId}');
                  },
                ),
                // Spacer to push icons to left, if needed
                // const Spacer(),
              ],
            ),
          ),
          // Caption
          Padding(
            padding: const EdgeInsets.fromLTRB(12.0, 0, 12.0, 12.0),
            child: Text(post.caption, style: const TextStyle(fontSize: 14.0)),
          ),
        ],
      ),
    );
  }
}
