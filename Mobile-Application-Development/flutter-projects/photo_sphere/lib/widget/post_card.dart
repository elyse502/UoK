import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:photo_sphere/data/mock_data.dart';
import 'package:photo_sphere/models/post.dart';
import 'package:photo_sphere/models/user.dart';

class PostCard extends StatelessWidget {
  final Post post;
  const PostCard({super.key, required this.post});

  @override
  Widget build(BuildContext context) {
    final user = mockUser.firstWhere(
      (u) => u.userId == post.userId,
      orElse: () {
        return User(userId: 'error', username: 'Unknown', avatarUrl: 'unknown');
      },
    );
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                CircleAvatar(
                  backgroundImage: NetworkImage(
                    post.userId == user.userId ? user.avatarUrl : 'Unknown',
                  ),
                  radius: 15.0,
                ),
                const SizedBox(width: 10.0),
                GestureDetector(
                  onTap: () {
                    context.push('/profile/${user.userId}');
                  },
                  child: Text(
                    post.userId == user.userId ? user.username : 'Default',
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12.0),
            ClipRRect(
              borderRadius: BorderRadiusGeometry.all(Radius.circular(10.0)),
              child: Image.network(
                post.imageUrl,
                width: double.infinity,
                height: 200,
                fit: BoxFit.cover,
              ),
            ),
            const SizedBox(height: 4.0),
            Row(
              children: [
                IconButton(
                  onPressed: () {},
                  icon: Icon(
                    Icons.thumb_up_alt_rounded,
                    size: 20.0,
                    color: Colors.blueAccent,
                  ),
                ),
                IconButton(
                  onPressed: () {},
                  icon: Icon(
                    Icons.comment_rounded,
                    size: 20.0,
                    color: Colors.blueGrey,
                  ),
                ),
                IconButton(
                  onPressed: () {},
                  icon: Icon(
                    Icons.share_outlined,
                    size: 20.0,
                    color: Colors.lightGreen,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
