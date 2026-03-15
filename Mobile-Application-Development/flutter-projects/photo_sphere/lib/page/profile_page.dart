import 'package:flutter/material.dart';
import 'package:photo_sphere/data/mock_data.dart';
import 'package:photo_sphere/models/user.dart';
import 'package:photo_sphere/widget/profile_card.dart';

class ProfilePage extends StatelessWidget {
  final String userid;
  const ProfilePage({super.key, required this.userid});

  @override
  Widget build(BuildContext context) {
    final User user = mockUser.firstWhere(
      (u) => u.userId == userid,
      orElse: () {
        return User(
          userId: 'error',
          username: 'Unknown User',
          avatarUrl: 'Unknown',
        );
      },
    );

    int upostid = mockPost.indexWhere((p) => p.userId == userid);
    final postLength = mockPost.where((elt) => elt.userId == userid).length;

    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Profile',
          style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.bold),
        ),
      ),
      body: Column(
        children: [
          Padding(
            padding: EdgeInsetsGeometry.all(16.0),
            child: Center(
              child: Column(
                children: [
                  CircleAvatar(
                    backgroundImage: NetworkImage(user.avatarUrl),
                    radius: 80.0,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(user.username),
                      const SizedBox(height: 18.0),
                      IconButton(
                        onPressed: () {},
                        icon: Icon(Icons.verified),
                        color: Colors.blueAccent,
                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        '100+ Posts | 500+ Likes',
                        style: TextStyle(
                          fontSize: 15.0,
                          color: Colors.blueGrey,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: GridView.builder(
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 3,
                ),
                itemCount: postLength,
                itemBuilder: (context, index) {
                  index = upostid;
                  // if (mockPost[index].userId == userid) {
                  // print(mockPost[index].postId);
                  // if (mockPost[index].postId == (upostid + 1).toString()) {
                  // if (index == upostid) {
                  while (upostid <= postLength) {
                    upostid = upostid + 1;
                    return ProfileCard(post: mockPost[upostid]);
                  }
                  // }
                  // return null;
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}
