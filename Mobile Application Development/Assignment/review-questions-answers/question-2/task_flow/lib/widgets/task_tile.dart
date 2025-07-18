import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../models/task.dart'; //

class TaskTile extends StatefulWidget {
  final Task task; //

  const TaskTile({Key? key, required this.task}) : super(key: key);

  @override
  State<TaskTile> createState() => _TaskTileState();
}

class _TaskTileState extends State<TaskTile> {
  late bool _isCompleted;

  @override
  void initState() {
    super.initState();
    _isCompleted = widget.task.isCompleted; //
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
      elevation: 2.0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8.0)),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Row(
          children: [
            Checkbox(
              //
              value: _isCompleted, //
              onChanged: (bool? newValue) {
                setState(() {
                  _isCompleted = newValue!; //
                  widget.task.isCompleted =
                      newValue; // Update model (local state change) //
                });
              },
              activeColor: widget.task.isCompleted ? Colors.green : Colors.grey,
            ),
            const SizedBox(width: 12.0),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    widget.task.title, // [cite: 38]
                    style: TextStyle(
                      fontSize: 16.0,
                      fontWeight: FontWeight.bold,
                      decoration: _isCompleted
                          ? TextDecoration.lineThrough
                          : null,
                    ),
                  ),
                  const SizedBox(height: 4.0),
                  Text(
                    'Due: ${DateFormat('MMM dd, yyyy').format(widget.task.dueDate)}', // [cite: 38]
                    style: TextStyle(
                      fontSize: 12.0,
                      color: Colors.grey[600],
                      decoration: _isCompleted
                          ? TextDecoration.lineThrough
                          : null,
                    ),
                  ),
                  if (widget.task.description.isNotEmpty) ...[
                    const SizedBox(height: 4.0),
                    Text(
                      widget.task.description,
                      style: TextStyle(
                        fontSize: 12.0,
                        color: Colors.grey[700],
                        fontStyle: FontStyle.italic,
                      ),
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
