import 'package:flutter/material.dart';
import 'package:photo_sphere/app_router.dart';

void main() => runApp(PhotoSphere());

class PhotoSphere extends StatelessWidget {
  const PhotoSphere({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: appRoute,
      debugShowCheckedModeBanner: false,
      title: 'Photo Sphere',
      // home: HomePage(),
    );
  }
}
