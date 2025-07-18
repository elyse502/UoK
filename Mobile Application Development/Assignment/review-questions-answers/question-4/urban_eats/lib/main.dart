import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'app_router.dart'; // Import the new app_router.dart

void main() {
  runApp(const UrbanEatsApp());
}

class UrbanEatsApp extends StatelessWidget {
  const UrbanEatsApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: 'UrbanEats',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.teal,
        ), // Seed color as required
        appBarTheme: AppBarTheme(
          backgroundColor: Colors.teal, // Matches theme's primary color
          foregroundColor: Colors.white, // White text
          elevation: 0,
        ),
        fontFamily:
            GoogleFonts.poppins().fontFamily, // Set Poppins as default font
        inputDecorationTheme: InputDecorationTheme(
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12.0),
            borderSide: BorderSide.none,
          ),
          filled: true,
          fillColor: Colors.grey[100],
          contentPadding: const EdgeInsets.symmetric(
            vertical: 12.0,
            horizontal: 16.0,
          ),
        ),
        cardTheme: CardThemeData(
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
        floatingActionButtonTheme: FloatingActionButtonThemeData(
          backgroundColor: Colors.teal,
          foregroundColor: Colors.white,
        ),
      ),
      routerConfig: appRouter, // Use the appRouter from app_router.dart
    );
  }
}
