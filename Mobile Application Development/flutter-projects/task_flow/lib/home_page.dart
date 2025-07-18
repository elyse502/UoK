import 'package:flutter/material.dart';
import 'package:task_flow/task_item.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("My Tasks")),
      body: ListView(
        children: const [
          SizedBox(height: 8),
          TaskItem(title: 'Finish Module 2 of Flutter Course'),
          TaskItem(title: 'Submit the weekly report'),
          TaskItem(title: 'Call the client back'),
          TaskItem(title: 'Grocery shopping for the week'),
          TaskItem(title: 'Plan the weekend trip'),
          TaskItem(title: 'Read one chapter of "Atomic Habits"'),
          TaskItem(title: 'Fix bug #734 on the main project'),
        ],
      ),
    );
  }
}
