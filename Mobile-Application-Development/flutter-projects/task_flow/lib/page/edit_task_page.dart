import 'package:flutter/material.dart';

class EditTaskPage extends StatefulWidget {
  final String taskId;
  const EditTaskPage({super.key, required this.taskId});

  @override
  State<EditTaskPage> createState() => _EditTaskPageState();
}

class _EditTaskPageState extends State<EditTaskPage> {
  late final TextEditingController _titleController;
  bool get _isCreatingNew => widget.taskId == 'new';

  @override
  void initState() {
    super.initState();
    _titleController = TextEditingController();
    if (!_isCreatingNew) {
      _titleController.text =
          'Editing Task ID: ${widget.taskId}'; // Placeholder for loaded title
    }
  }

  @override
  void dispose() {
    _titleController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_isCreatingNew ? 'Add New Task' : 'Edit Task'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _titleController,
              decoration: InputDecoration(
                labelText: 'Task Title',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {},
              child: Text(_isCreatingNew ? 'Create Task' : 'Save Changes'),
            ),
          ],
        ),
      ),
    );
  }
}
