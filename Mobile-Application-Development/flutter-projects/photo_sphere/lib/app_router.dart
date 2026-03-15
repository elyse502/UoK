import 'package:go_router/go_router.dart';
import 'package:photo_sphere/homepage.dart';
import 'package:photo_sphere/page/profile_page.dart';

final appRoute = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(path: '/', builder: (context, state) => const HomePage()),
    GoRoute(
      path: '/profile/:userid',
      builder: (context, state) {
        final id = state.pathParameters['userid']!;
        return ProfilePage(userid: id);
      },
    ),
  ],
);
