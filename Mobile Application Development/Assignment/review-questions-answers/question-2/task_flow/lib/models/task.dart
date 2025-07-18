class Task {
  final String id; //
  final String projectId; //
  final String title; //
  final String description; //
  final DateTime dueDate; //
  bool isCompleted; //

  Task({
    required this.id,
    required this.projectId, //
    required this.title, //
    required this.description, //
    required this.dueDate, //
    this.isCompleted = false, //
  });
}
