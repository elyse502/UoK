import 'package:flutter/widgets.dart';
import 'package:photo_sphere/models/post.dart';

class ProfileCard extends StatefulWidget {
  final Post post;
  const ProfileCard({super.key, required this.post});

  @override
  State<ProfileCard> createState() => _ProfileCardState();
}

class _ProfileCardState extends State<ProfileCard> {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(2.0),
      child: Image.network(widget.post.imageUrl),
    );
  }
}
