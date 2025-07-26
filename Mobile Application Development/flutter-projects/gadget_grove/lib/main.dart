import 'package:flutter/material.dart';
import 'package:gadget_grove/app_router.dart';
// import 'package:gadget_grove/homepage.dart';

void main() => runApp(const GadgetGrove());

class GadgetGrove extends StatelessWidget {
  const GadgetGrove({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: appRoute,
      debugShowCheckedModeBanner: false,
      title: 'Gadget Grove App',
      // home: HomePage(),
    );
  }
}
