import 'package:go_router/go_router.dart';

import 'pages/home_page.dart';
import 'pages/product_details_page.dart';
import 'pages/checkout_page.dart';

final GoRouter router = GoRouter(
  routes: [
    GoRoute(path: '/', builder: (context, state) => HomePage()),
    GoRoute(
      path: '/product/:id',
      builder: (context, state) {
        final id = state.pathParameters['id']!;
        return ProductDetailsPage(id: id);
      },
    ),
    GoRoute(path: '/checkout', builder: (context, state) => CheckoutPage()),
  ],
);
