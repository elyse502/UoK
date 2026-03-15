import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../models/project.dart'; //

class ProjectCard extends StatelessWidget {
  final Project project; //

  const ProjectCard({Key? key, required this.project}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      //
      margin: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
      elevation: 4.0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12.0)),
      child: ListTile(
        //
        contentPadding: const EdgeInsets.all(16.0),
        leading: Container(
          //
          width: 50,
          height: 50,
          decoration: BoxDecoration(
            color: project.color, //
            shape: BoxShape.circle,
          ),
        ),
        title: Text(
          //
          project.name, //
          style: const TextStyle(fontSize: 18.0, fontWeight: FontWeight.bold),
        ),
        trailing: const Icon(Icons.arrow_forward_ios, color: Colors.grey),
        onTap: () {
          // Navigate to ProjectDetailsPage //
          context.go('/project/${project.id}'); //
        },
      ),
    );
  }
}
