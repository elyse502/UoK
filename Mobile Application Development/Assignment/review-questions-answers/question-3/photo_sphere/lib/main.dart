import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';

// Import pages for PhotoSphere
import 'pages/feed_page.dart'; // PhotoSphere Feed
import 'pages/profile_page.dart'; // PhotoSphere Profile
import 'pages/upload_page.dart'; // PhotoSphere Upload

void main() {
  runApp(const PhotoSphereApp());
}

final GoRouter _router = GoRouter(
  // Initial location (start with PhotoSphere Feed)
  initialLocation: '/',
  routes: [
    // PhotoSphere Routes
    GoRoute(
      path: '/',
      builder: (context, state) => const FeedPage(), // PhotoSphere Feed Page
    ),
    GoRoute(
      path: '/profile/:userId',
      builder: (context, state) {
        final userId = state.pathParameters['userId']!;
        return ProfilePage(userId: userId);
      },
    ),
    GoRoute(path: '/upload', builder: (context, state) => const UploadPage()),
  ],
);

class PhotoSphereApp extends StatelessWidget {
  const PhotoSphereApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: 'PhotoSphere', // Updated title
      theme: ThemeData(
        primarySwatch:
            Colors.deepPurple, // A nice primary color for PhotoSphere
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.deepPurple,
          foregroundColor: Colors.white,
          elevation: 0,
        ),
        floatingActionButtonTheme: const FloatingActionButtonThemeData(
          backgroundColor: Colors.deepPurple,
          foregroundColor: Colors.white,
        ),
        textTheme: GoogleFonts.poppinsTextTheme(Theme.of(context).textTheme),
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
          elevation: 4.0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12.0),
          ),
        ),
        snackBarTheme: SnackBarThemeData(
          backgroundColor: Colors.deepPurple,
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
