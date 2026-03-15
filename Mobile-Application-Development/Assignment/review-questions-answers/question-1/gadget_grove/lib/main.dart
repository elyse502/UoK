import 'package:google_fonts/google_fonts.dart';
import 'package:flutter/material.dart';
import 'app_router.dart';

void main() {
  runApp(const GadgetGroveApp());
}

class GadgetGroveApp extends StatelessWidget {
  const GadgetGroveApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: router,
      debugShowCheckedModeBanner: false,
      title: 'Gadget Grove',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
        useMaterial3: true,
        fontFamily:
            GoogleFonts.poppins().fontFamily, // Set Poppins as default font
      ),
    );
  }
}
