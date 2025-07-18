import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart'; //
import 'package:google_fonts/google_fonts.dart'; // For a custom font (optional but good practice)

import 'pages/home_page.dart';
import 'pages/project_details_page.dart';
import 'pages/add_task_page.dart';

void main() {
  runApp(const TaskFlowApp());
}

final GoRouter _router = GoRouter(
  //
  routes: [
    GoRoute(
      path: '/',
      builder: (context, state) => const HomePage(), //
    ),
    GoRoute(
      path: '/project/:projectId', //
      builder: (context, state) {
        final projectId = state.pathParameters['projectId']!; //
        return ProjectDetailsPage(projectId: projectId); //
      },
      routes: [
        GoRoute(
          path: 'add-task', // [cite: 40]
          builder: (context, state) {
            final projectId = state.pathParameters['projectId']!; // [cite: 40]
            return AddTaskPage(projectId: projectId); // [cite: 40]
          },
        ),
      ],
    ),
  ],
);

class TaskFlowApp extends StatelessWidget {
  const TaskFlowApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: 'TaskFlow',
      theme: ThemeData(
        primarySwatch: Colors.teal, // A nice primary color
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.teal,
          foregroundColor: Colors.white,
          elevation: 0,
        ),
        floatingActionButtonTheme: const FloatingActionButtonThemeData(
          backgroundColor: Colors.teal,
          foregroundColor: Colors.white,
        ),
        // Incorporate Google Fonts (optional but enhances UI)
        textTheme: GoogleFonts.robotoTextTheme(Theme.of(context).textTheme),
        inputDecorationTheme: InputDecorationTheme(
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.0),
            borderSide: BorderSide.none,
          ),
          filled: true,
          fillColor: Colors.grey[100],
          contentPadding: const EdgeInsets.symmetric(
            vertical: 16.0,
            horizontal: 12.0,
          ),
        ),
        cardTheme: CardThemeData(
          // Change CardTheme to CardThemeData
          elevation: 4.0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12.0),
          ),
        ),
        snackBarTheme: SnackBarThemeData(
          backgroundColor: Colors.teal,
          contentTextStyle: const TextStyle(color: Colors.white),
          behavior: SnackBarBehavior.floating,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8.0),
          ),
        ),
      ),
      routerConfig: _router,
    );
  }
}
