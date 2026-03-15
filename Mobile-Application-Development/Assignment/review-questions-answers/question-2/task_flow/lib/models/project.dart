import 'package:flutter/material.dart';

class Project {
  final String id; //
  final String name; //
  final String colorHex; //

  Project({required this.id, required this.name, required this.colorHex});

  Color get color => Color(int.parse(colorHex.replaceAll('#', '0xFF')));
}
