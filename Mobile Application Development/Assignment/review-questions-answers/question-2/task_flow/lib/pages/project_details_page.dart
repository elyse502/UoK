import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../data/mock_data.dart';
import '../models/project.dart';
import '../models/task.dart';
import '../widgets/task_tile.dart';

class ProjectDetailsPage extends StatelessWidget {
  final String projectId;

  const ProjectDetailsPage({Key? key, required this.projectId})
    : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Add a debug print to confirm this widget's build method is called
    debugPrint('Building ProjectDetailsPage for projectId: $projectId');

    // Find the project based on the projectId
    final Project? project = mockProjects.firstWhere(
      (p) => p.id == projectId,
      orElse: () {
        debugPrint('Project not found for ID: $projectId');
        throw Exception(
          'Project not found',
        ); // Handle case where project ID is invalid
      },
    );

    // If project is null (should be caught by orElse, but as a safeguard)
    if (project == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Error')),
        body: const Center(child: Text('Project not found!')),
      );
    }

    // Filter tasks associated with the current project
    final List<Task> projectTasks = mockTasks
        .where((task) => task.projectId == projectId)
        .toList();

    return Scaffold(
      appBar: AppBar(
        title: Text('${project.name} Tasks'),
        backgroundColor: project.color,
        // Explicitly add a leading button for back navigation
        leading: IconButton(
          icon: const Icon(
            Icons.arrow_back,
            color: Colors
                .white, // Ensures the icon is visible against the AppBar color
          ),
          onPressed: () {
            // Add a debug print to confirm the button's onPressed is triggered
            debugPrint(
              'Back button pressed. context.canPop(): ${context.canPop()}',
            );

            // Check if there's a route to pop from the navigation stack.
            // This prevents the "There is nothing to pop" error.
            if (context.canPop()) {
              context
                  .pop(); // Go back to the previous screen (HomePage in this case)
            } else {
              // If there's no previous route (e.g., if this page was deep-linked as the first screen),
              // navigate to the home page to provide a consistent entry point.
              debugPrint('No route to pop, navigating to /');
              context.go('/'); // Navigate to the root (HomePage)
            }
          },
        ),
      ),
      body: projectTasks.isEmpty
          ? const Center(
              child: Text(
                'No tasks yet for this project. Add one!',
                style: TextStyle(fontSize: 16.0, color: Colors.grey),
              ),
            )
          : ListView.builder(
              padding: const EdgeInsets.symmetric(vertical: 12.0),
              itemCount: projectTasks.length,
              itemBuilder: (context, index) {
                return TaskTile(task: projectTasks[index]);
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // Navigate to the AddTaskPage, passing the current projectId
          debugPrint(
            'Add Task button pressed, navigating to /project/$projectId/add-task',
          );
          context.go('/project/$projectId/add-task');
        },
        backgroundColor: project.color,
        child: const Icon(Icons.add, color: Colors.white),
        tooltip: 'Add New Task',
      ),
    );
  }
}
