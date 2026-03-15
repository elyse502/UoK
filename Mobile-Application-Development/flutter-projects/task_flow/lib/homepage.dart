import 'package:flutter/material.dart';
import 'package:task_flow/data/mock_data.dart';
import 'package:task_flow/widgets/task_item.dart';
import 'package:go_router/go_router.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(elevation: 2, title: Text('TaskFlow Home')),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          context.push('/task/new');
        },
        child: Icon(Icons.add),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              'Task List',
              style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.bold),
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: mockTask.length,
              itemBuilder: (context, int index) {
                return TaskItem(task: mockTask[index]);
              },
            ),
          ),
        ],
      ),
    );
  }
}
