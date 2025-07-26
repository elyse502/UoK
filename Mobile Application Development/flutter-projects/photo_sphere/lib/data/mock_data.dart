import 'package:photo_sphere/models/post.dart';
import 'package:photo_sphere/models/user.dart';

List<User> mockUser = [
  User(
    userId: '1',
    username: 'DannyI',
    avatarUrl: 'https://picsum.photos/seed/chair/600/400',
  ),
  User(
    userId: '2',
    username: 'AlterBusy',
    avatarUrl: 'https://picsum.photos/seed/laptop/600/400',
  ),
  User(
    userId: '3',
    username: 'DoneLay',
    avatarUrl: 'https://picsum.photos/seed/giant/600/400',
  ),
];
List<Post> mockPost = [
  Post(
    postId: '1',
    userId: '1',
    imageUrl: 'https://picsum.photos/seed/car/600/400',
    caption: 'caption1',
    likes: '3 likes',
  ),
  Post(
    postId: '2',
    userId: '2',
    imageUrl: 'https://picsum.photos/seed/screen/600/400',
    caption: 'caption1',
    likes: '3 likes',
  ),
  Post(
    postId: '3',
    userId: '2',
    imageUrl: 'https://picsum.photos/seed/lake/600/400',
    caption: 'caption1',
    likes: '3 likes',
  ),
  Post(
    postId: '4',
    userId: '3',
    imageUrl: 'https://picsum.photos/seed/chair/600/400',
    caption: 'caption1',
    likes: '3 likes',
  ),
];
