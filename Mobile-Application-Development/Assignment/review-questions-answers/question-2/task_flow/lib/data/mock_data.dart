import '../models/project.dart';
import '../models/task.dart';

final List<Project> mockProjects = [
  Project(id: 'p1', name: 'User Management System', colorHex: '#4CAF50'),
  Project(id: 'p2', name: 'RESTful API Platform', colorHex: '#2196F3'),
  Project(id: 'p3', name: 'CI/CD Pipeline Automation', colorHex: '#FFC107'),
  Project(
    id: 'p4',
    name: 'Monitoring & Logging Dashboard',
    colorHex: '#9C27B0',
  ),
  Project(id: 'p5', name: 'Payment Gateway Integration', colorHex: '#F44336'),
  Project(id: 'p6', name: 'DevOps Infrastructure Setup', colorHex: '#00BCD4'),
  Project(id: 'p7', name: 'AI Recommendation Engine', colorHex: '#FF5722'),
  Project(id: 'p8', name: 'Mobile App Performance Tuning', colorHex: '#673AB7'),
  Project(id: 'p9', name: 'Secure File Storage System', colorHex: '#3F51B5'),
  Project(id: 'p10', name: 'Cross-Platform Testing Suite', colorHex: '#E91E63'),
];

List<Task> mockTasks = [
  Task(
    id: 't1',
    projectId: 'p1',
    title: 'Implement Role-Based Access Control',
    description: 'Develop RBAC system for admin, editor, and viewer roles.',
    dueDate: DateTime.now().add(const Duration(days: 5)),
  ),
  Task(
    id: 't2',
    projectId: 'p1',
    title: 'Write Unit Tests for Auth Module',
    description: 'Ensure login and signup functionalities are properly tested.',
    dueDate: DateTime.now().add(const Duration(days: 8)),
    isCompleted: true,
  ),
  Task(
    id: 't3',
    projectId: 'p2',
    title: 'Design REST API Routes',
    description: 'Define structure for user, project, and task endpoints.',
    dueDate: DateTime.now().add(const Duration(days: 6)),
  ),
  Task(
    id: 't4',
    projectId: 'p2',
    title: 'Implement JWT Authentication',
    description: 'Secure API routes using JWT token validation.',
    dueDate: DateTime.now().add(const Duration(days: 10)),
  ),
  Task(
    id: 't5',
    projectId: 'p3',
    title: 'Set Up GitHub Actions Workflow',
    description: 'Automate testing and deployment on every push to main.',
    dueDate: DateTime.now().add(const Duration(days: 3)),
  ),
  Task(
    id: 't6',
    projectId: 'p4',
    title: 'Integrate Prometheus & Grafana',
    description: 'Enable real-time monitoring for backend services.',
    dueDate: DateTime.now().add(const Duration(days: 7)),
  ),
];

// Function to add a new task (for demonstration purposes, in a real app this would interact with a database)
void addTask(Task newTask) {
  mockTasks.add(newTask);
}
