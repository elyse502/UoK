import 'package:go_router/go_router.dart';
import 'home_screen.dart';
import 'registration_screen.dart';
import 'view_users_screen.dart';

class AppRouter {
  // Create a static GoRouter instance.
  static final GoRouter router = GoRouter(
    // The initial location of the app.
    initialLocation: '/',
    
    // Define the application routes.
    routes: [
      GoRoute(
        path: '/',
        builder: (context, state) => const HomeScreen(),
      ),
      GoRoute(
        path: '/register',
        builder: (context, state) => const RegistrationScreen(),
      ),
      GoRoute(
        path: '/view-users',
        builder: (context, state) => const ViewUsersScreen(),
      ),
    ],
  );
}