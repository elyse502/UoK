import 'package:go_router/go_router.dart';
import 'package:task_flow/homepage.dart';
import 'package:task_flow/page/edit_task_page.dart';

final appRoute = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(path: '/', builder: (context, state) => const HomePage()),
    GoRoute(
      path: '/task/:id',
      builder: (context, state) {
        final tId = state.pathParameters['id']!;
        return EditTaskPage(taskId: tId);
      },
    ),
  ],
);
