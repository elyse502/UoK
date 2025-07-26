import 'package:go_router/go_router.dart';
import 'package:shop_sphere/homepage.dart';
import 'package:shop_sphere/pages/product_page_details.dart';
import 'package:shop_sphere/pages/register_page.dart';
import 'package:shop_sphere/pages/product_upload_page.dart';

final appRouter = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(path: '/', builder: (context, state) => const HomePage()),
    GoRoute(
      path: '/product/:id',
      builder: (context, state) {
        final productId = state.pathParameters['id']!;
        return ProductPageDetails(productId: productId);
      },
    ),
    GoRoute(
      path: '/register',
      builder: (context, state) => const RegisterPage(),
    ),
    GoRoute(
      path: '/product-upload',
      builder: (context, state) => const ProductUploadPage(),
    ),
  ],
);
