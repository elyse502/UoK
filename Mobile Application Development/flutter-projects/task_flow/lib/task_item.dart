import 'package:flutter/material.dart';

class TaskItem extends StatefulWidget {
  final String title;
  const TaskItem({super.key, required this.title});

  @override
  State<TaskItem> createState() => _TaskItemState();
}

class _TaskItemState extends State<TaskItem> {
  bool _isChecked = false;

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 4.0),
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
          widget.title,
          style: TextStyle(
            decoration: _isChecked
                ? TextDecoration.lineThrough
                : TextDecoration.none,
            color: _isChecked ? Colors.grey : null,
          ),
        ),
        subtitle: const Text('Due: Tomorrow'),
        trailing: IconButton(
          icon: const Icon(Icons.delete_outline, color: Colors.redAccent),
          onPressed: () {
            print('Delete button pressed for ${widget.title}');
          },
        ),
        onTap: () {
          setState(() {
            _isChecked = !_isChecked;
          });
        },
      ),
    );
  }
}
