import '../models/project.dart'; //
import '../models/task.dart'; //

final List<Project> mockProjects = [
  //
  Project(id: 'p1', name: 'Mobile App Redesign', colorHex: '#4CAF50'), //
  Project(id: 'p2', name: 'Backend API Development', colorHex: '#2196F3'), //
  Project(id: 'p3', name: 'Marketing Campaign Launch', colorHex: '#FFC107'), //
  Project(id: 'p4', name: 'Data Analytics Dashboard', colorHex: '#9C27B0'), //
  Project(id: 'p5', name: 'E-commerce Payment Gateway', colorHex: '#F44336'), //
  Project(id: 'p6', name: 'IoT Smart Home System', colorHex: '#00BCD4'), //
  Project(id: 'p7', name: 'AI Chatbot Implementation', colorHex: '#FF5722'), //
  Project(id: 'p8', name: 'Health & Fitness Tracker', colorHex: '#673AB7'), //
  Project(id: 'p9', name: 'Blockchain Wallet Prototype', colorHex: '#3F51B5'),
  Project(id: 'p10', name: 'AR Furniture App', colorHex: '#E91E63'), //
];

List<Task> mockTasks = [
  //
  Task(
    id: 't1',
    projectId: 'p1', //
    title: 'Design UI Wireframes', //
    description: 'Create initial wireframes for the new mobile app design.', //
    dueDate: DateTime.now().add(const Duration(days: 7)), //
  ),
  Task(
    id: 't2',
    projectId: 'p1', //
    title: 'Develop User Authentication', //
    description: 'Implement secure user login and registration flows.', //
    dueDate: DateTime.now().add(const Duration(days: 14)), //
    isCompleted: true, //
  ),
  Task(
    id: 't3',
    projectId: 'p2', //
    title: 'Define API Endpoints', //
    description: 'Document all necessary API endpoints for the mobile app.', //
    dueDate: DateTime.now().add(const Duration(days: 5)), //
  ),
  Task(
    id: 't4',
    projectId: 'p2', //
    title: 'Implement Database Schema', //
    description: 'Set up the database schema for user and task data.', //
    dueDate: DateTime.now().add(const Duration(days: 10)), //
  ),
  Task(
    id: 't5',
    projectId: 'p1', //
    title: 'Integrate Payment Gateway', //
    description:
        'Connect the app with a third-party payment processing service.', //
    dueDate: DateTime.now().add(const Duration(days: 20)), //
  ),
  Task(
    id: 't6',
    projectId: 'p3', //
    title: 'Create Social Media Content', //
    description:
        'Develop engaging content for various social media platforms.', //
    dueDate: DateTime.now().add(const Duration(days: 3)), //
  ),
];

// Function to add a new task (for demonstration purposes, in a real app this would interact with a database)
void addTask(Task newTask) {
  mockTasks.add(newTask);
}
