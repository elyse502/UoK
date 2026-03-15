import '../models/user.dart';
import '../models/post.dart';

final List<User> mockUsers = [
  User(
    userId: 'u1',
    username: 'Alice_Wonders',
    avatarUrl:
        'https://images.unsplash.com/photo-1715259135257-bc460070ba65?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  ),
  User(
    userId: 'u2',
    username: 'Bob_Explorer',
    avatarUrl:
        'https://images.unsplash.com/photo-1705467495579-6b1eb83add5c?q=80&w=1175&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  ),
  User(
    userId: 'u3',
    username: 'Charlie_Pixel',
    avatarUrl:
        'https://images.unsplash.com/photo-1570369336897-57984b6cf5dc?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  ),
  User(
    userId: 'u4',
    username: 'Diana_Lens',
    avatarUrl:
        'https://plus.unsplash.com/premium_photo-1669704098876-2a38eb10e56a?q=80&w=688&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  ),
];

final List<Post> mockPosts = [
  Post(
    postId: 'p1',
    userId: 'u1',
    imageUrl:
        'https://images.unsplash.com/photo-1605663585104-02b44080a242?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    caption:
        'Majestic mountain views! Feeling on top of the world. #nature #adventure',
    likes: 125,
  ),
  Post(
    postId: 'p2',
    userId: 'u2',
    imageUrl:
        'https://plus.unsplash.com/premium_photo-1661908853318-893732a14e42?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    caption: 'City lights at night. So vibrant! ✨ #citylife #nightphotography',
    likes: 230,
  ),
  Post(
    postId: 'p3',
    userId: 'u1',
    imageUrl:
        'https://images.unsplash.com/photo-1610467029422-61be7e00ec77?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    caption: 'Quiet moments in the garden. Pure bliss. 🌿 #garden #peaceful',
    likes: 88,
  ),
  Post(
    postId: 'p4',
    userId: 'u3',
    imageUrl:
        'https://images.unsplash.com/photo-1660482136157-9a2dc99208a6?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    caption: 'Delicious brunch spread! Who\'s hungry? 🥞 #foodie #brunch',
    likes: 190,
  ),
  Post(
    postId: 'p5',
    userId: 'u2',
    imageUrl:
        'https://plus.unsplash.com/premium_photo-1661962790754-9a1a5b5e334a?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    caption:
        'Breathtaking sunset by the ocean. Nature\'s masterpiece. 🌅 #sunset #beach',
    likes: 310,
  ),
  Post(
    postId: 'p6',
    userId: 'u4',
    imageUrl:
        'https://plus.unsplash.com/premium_photo-1669769591349-41181bed9d6a?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    caption: 'My furry friend enjoying the sun! 🐾 #petlove #dogsofinstagram',
    likes: 155,
  ),
];
