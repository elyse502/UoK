import 'package:flutter/material.dart';
import '../data/mock_data.dart'; //
import '../widgets/project_card.dart'; //

class HomePage extends StatelessWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('TaskFlow - Projects 📑'),
        centerTitle: true,
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: ListView.builder(
        //
        padding: const EdgeInsets.symmetric(vertical: 12.0),
        itemCount: mockProjects.length, //
        itemBuilder: (context, index) {
          final project = mockProjects[index]; //
          return ProjectCard(project: project); //
        },
      ),
    );
  }
}
