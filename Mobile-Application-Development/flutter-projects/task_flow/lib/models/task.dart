class Task {
  final String id;
  final String taskTitle;
  final String description;
  final String dueDate;
  final bool isCompleted;

  Task({
    required this.id,
    required this.taskTitle,
    required this.description,
    required this.dueDate,
    required this.isCompleted,
  });
}
