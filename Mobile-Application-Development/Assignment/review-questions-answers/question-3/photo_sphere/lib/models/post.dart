class Post {
  final String postId;
  final String userId; // Links to User
  final String imageUrl;
  final String caption;
  final int likes;

  Post({
    required this.postId,
    required this.userId,
    required this.imageUrl,
    required this.caption,
    this.likes = 0,
  });
}
