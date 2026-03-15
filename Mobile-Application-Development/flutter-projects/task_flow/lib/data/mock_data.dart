import 'package:task_flow/models/task.dart';
import 'package:task_flow/models/project.dart';

List<Task> mockTask = [
  Task(
    id: '1',
    taskTitle: 'Finished Module of Flutter Course',
    description: 'description',
    dueDate: 'Today',
    isCompleted: false,
  ),
  Task(
    id: '2',
    taskTitle: 'Submit the Weekly report',
    description: 'description',
    dueDate: 'Tomorrow',
    isCompleted: false,
  ),
  Task(
    id: '3',
    taskTitle: 'Call the client Back',
    description: 'description',
    dueDate: 'Oct 25',
    isCompleted: false,
  ),
  Task(
    id: '4',
    taskTitle: 'Call the client Back',
    description: 'description',
    dueDate: 'Nov 28',
    isCompleted: false,
  ),
  Task(
    id: '5',
    taskTitle: 'Plan the weekend trip',
    description: 'description',
    dueDate: 'Today',
    isCompleted: false,
  ),
  Task(
    id: '6',
    taskTitle: 'Unplanned Adventures',
    description: 'description',
    dueDate: 'Jan 29',
    isCompleted: false,
  ),
];

List<Project> mockProject = [
  Project(id: '1', name: 'name1', colorHex: 'colorHex'),
  Project(id: '2', name: 'name2', colorHex: 'colorHex'),
  Project(id: '3', name: 'name3', colorHex: 'colorHex'),
  Project(id: '4', name: 'name4', colorHex: 'colorHex'),
  Project(id: '5', name: 'name5', colorHex: 'colorHex'),
];
