import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'app_router.dart'; 

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Firebase.initializeApp();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    // Use MaterialApp.router to integrate GoRouter.
    return MaterialApp.router(
      title: 'Flutter User Management',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.indigo,
        visualDensity: VisualDensity.adaptivePlatformDensity,
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.indigo,
          foregroundColor: Colors.white,
        ),
      ),
      // Set the router configuration.
      routerConfig: AppRouter.router,
    );
  }
}
