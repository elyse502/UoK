import 'package:flutter/material.dart';
import 'package:task_flow/models/task.dart';
import 'package:go_router/go_router.dart';

class TaskItem extends StatefulWidget {
  final Task task;
  const TaskItem({super.key, required this.task});

  @override
  State<TaskItem> createState() => _TaskItemState();
}

class _TaskItemState extends State<TaskItem> {
  late bool _isChecked = false;

  @override
  void initState() {
    super.initState();
    _isChecked = widget.task.isCompleted;
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: ListTile(
        leading: Checkbox(
          value: _isChecked,
          onChanged: (bool? value) {
            setState(() {
              _isChecked = value ?? false;
            });
          },
        ),
        title: Text(
          widget.task.taskTitle,
          style: TextStyle(
            decoration: _isChecked
                ? TextDecoration.lineThrough
                : TextDecoration.none,
            color: _isChecked ? Colors.red : null,
          ),
        ),
        subtitle: Text('Due: ${widget.task.dueDate}'),
        trailing: IconButton(
          onPressed: () {
            print('Item ${widget.task.taskTitle} deleted!');
          },
          icon: const Icon(Icons.delete_outline, color: Colors.redAccent),
        ),
        // onTap: () {
        //   setState(() {
        //     _isChecked = !_isChecked;
        //   });
        onTap: () {
          context.push('/task/${widget.task.id}');
        },
      ),
    );
  }
}
