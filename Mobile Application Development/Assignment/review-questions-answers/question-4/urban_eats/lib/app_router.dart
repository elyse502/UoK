import 'package:go_router/go_router.dart';

import 'pages/discovery_page.dart';
import 'pages/restaurant_details_page.dart';
import 'pages/add_restaurant_page.dart';

final GoRouter appRouter = GoRouter(
  initialLocation: '/', // Start at the DiscoveryPage
  routes: [
    GoRoute(path: '/', builder: (context, state) => const DiscoveryPage()),
    GoRoute(
      path: '/restaurant/:id',
      builder: (context, state) {
        final restaurantId = state.pathParameters['id']!;
        return RestaurantDetailsPage(restaurantId: restaurantId);
      },
    ),
    GoRoute(
      path: '/add-restaurant',
      builder: (context, state) => const AddRestaurantPage(),
    ),
  ],
);
