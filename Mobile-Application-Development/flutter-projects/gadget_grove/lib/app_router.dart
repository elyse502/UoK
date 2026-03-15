import 'package:go_router/go_router.dart';
import 'package:gadget_grove/homepage.dart';
import 'package:gadget_grove/pages/product_details_page.dart';

final appRoute = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(path: '/', builder: (context, state) => const HomePage()),
    GoRoute(
      path: '/product/:id',
      builder: (context, state) {
        final prodId = state.pathParameters['id']!;
        return ProductDetailsPage(productId: prodId);
      },
    ),
  ],
);
