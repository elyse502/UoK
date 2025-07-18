import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:file_picker/file_picker.dart'; // [cite: 45]
import 'package:intl/intl.dart'; // [cite: 44]

import '../data/mock_data.dart';
import '../models/task.dart'; //

class AddTaskPage extends StatefulWidget {
  final String projectId; // [cite: 40]

  const AddTaskPage({Key? key, required this.projectId}) : super(key: key);

  @override
  State<AddTaskPage> createState() => _AddTaskPageState();
}

class _AddTaskPageState extends State<AddTaskPage> {
  final _formKey = GlobalKey<FormState>();
  final _titleController = TextEditingController(); //
  final _descriptionController = TextEditingController(); //
  final _dueDateController = TextEditingController(); //

  DateTime? _selectedDueDate; // [cite: 44]
  String? _attachedFileName; //
  String? _attachedFilePath; // [cite: 47]

  @override
  void dispose() {
    _titleController.dispose();
    _descriptionController.dispose();
    _dueDateController.dispose();
    super.dispose();
  }

  Future<void> _selectDate(BuildContext context) async {
    //
    final DateTime? picked = await showDatePicker(
      //
      context: context,
      initialDate: _selectedDueDate ?? DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2100),
      builder: (context, child) {
        return Theme(
          data: ThemeData.light().copyWith(
            colorScheme: ColorScheme.light(
              primary: Theme.of(
                context,
              ).primaryColor, // Use app's primary color
              onPrimary: Colors.white,
              onSurface: Colors.black,
            ),
            textButtonTheme: TextButtonThemeData(
              style: TextButton.styleFrom(
                foregroundColor: Theme.of(
                  context,
                ).primaryColor, // Color for buttons
              ),
            ),
          ),
          child: child!,
        );
      },
    );
    if (picked != null && picked != _selectedDueDate) {
      setState(() {
        _selectedDueDate = picked; // [cite: 44]
        _dueDateController.text = DateFormat(
          'MMM dd, yyyy',
        ).format(picked); // [cite: 44]
      });
    }
  }

  Future<void> _attachFile() async {
    // [cite: 45]
    FilePickerResult? result = await FilePicker.platform
        .pickFiles(); // [cite: 45]

    if (result != null) {
      setState(() {
        _attachedFileName = result.files.first.name; //
        _attachedFilePath = result.files.first.path; // [cite: 47]
      });
    } else {
      // User canceled the picker
      setState(() {
        _attachedFileName = null;
        _attachedFilePath = null;
      });
    }
  }

  void _saveTask() {
    // [cite: 47]
    if (_formKey.currentState!.validate()) {
      // [cite: 47]
      if (_selectedDueDate == null) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Please select a deadline.')),
        );
        return;
      }

      final newTask = Task(
        id: DateTime.now().millisecondsSinceEpoch
            .toString(), // Simple unique ID
        projectId: widget.projectId, // [cite: 40]
        title: _titleController.text, // [cite: 47]
        description: _descriptionController.text, // [cite: 47]
        dueDate: _selectedDueDate!, // [cite: 47]
        isCompleted: false,
      );

      addTask(newTask); // Add to mock data

      print('Task Saved:'); // [cite: 47]
      print('  Project ID: ${newTask.projectId}'); // [cite: 47]
      print('  Title: ${newTask.title}'); // [cite: 47]
      print('  Description: ${newTask.description}'); // [cite: 47]
      print(
        '  Due Date: ${DateFormat('yyyy-MM-dd').format(newTask.dueDate)}',
      ); // [cite: 47]
      print('  Attached File: ${_attachedFileName ?? 'None'}'); // [cite: 47]
      if (_attachedFilePath != null) {
        print('  File Path: $_attachedFilePath'); // [cite: 47]
      }

      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Task added successfully!')));

      context.pop(); // Go back to ProjectDetailsPage
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Add New Task'),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          //
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              TextFormField(
                //
                controller: _titleController, //
                decoration: const InputDecoration(
                  labelText: 'Title',
                  hintText: 'e.g., Complete report',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.title),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter a title';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              TextFormField(
                //
                controller: _descriptionController, //
                decoration: const InputDecoration(
                  labelText: 'Description',
                  hintText: 'e.g., Finalize Q3 sales report for presentation',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.description),
                ),
                maxLines: 3,
              ),
              const SizedBox(height: 16.0),
              TextFormField(
                //
                controller: _dueDateController, //
                readOnly: true, //
                onTap: () => _selectDate(context), //
                decoration: const InputDecoration(
                  labelText: 'Deadline', //
                  hintText: 'Select a due date',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.calendar_today),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please select a deadline';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              ElevatedButton.icon(
                // [cite: 45]
                onPressed: _attachFile, // [cite: 45]
                icon: const Icon(Icons.attach_file),
                label: const Text('Attach File'), // [cite: 45]
                style: ElevatedButton.styleFrom(
                  minimumSize: const Size.fromHeight(50),
                  backgroundColor: Theme.of(
                    context,
                  ).primaryColor.withOpacity(0.1),
                  foregroundColor: Theme.of(context).primaryColor,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8.0),
                  ),
                ),
              ),
              if (_attachedFileName != null) //
                Padding(
                  padding: const EdgeInsets.only(top: 8.0),
                  child: Row(
                    children: [
                      const Icon(
                        Icons.insert_drive_file,
                        size: 20,
                        color: Colors.grey,
                      ),
                      const SizedBox(width: 8.0),
                      Expanded(
                        child: Text(
                          _attachedFileName!, //
                          style: const TextStyle(
                            fontStyle: FontStyle.italic,
                            color: Colors.grey,
                          ),
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                      IconButton(
                        icon: const Icon(
                          Icons.close,
                          size: 18,
                          color: Colors.red,
                        ),
                        onPressed: () {
                          setState(() {
                            _attachedFileName = null;
                            _attachedFilePath = null;
                          });
                        },
                      ),
                    ],
                  ),
                ),
              const SizedBox(height: 24.0),
              ElevatedButton(
                onPressed: _saveTask, // [cite: 47]
                style: ElevatedButton.styleFrom(
                  backgroundColor: Theme.of(context).primaryColor,
                  foregroundColor: Colors.white,
                  minimumSize: const Size.fromHeight(
                    50,
                  ), // Make button full width
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                ),
                child: const Text(
                  'Save Task', // [cite: 47]
                  style: TextStyle(fontSize: 18.0),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
