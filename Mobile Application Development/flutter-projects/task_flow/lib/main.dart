import 'package:flutter/material.dart';
import 'package:task_flow/app_router.dart';

void main() => runApp(const TaskFlowApp());

class TaskFlowApp extends StatelessWidget {
  const TaskFlowApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: appRoute,
      debugShowCheckedModeBanner: false,
      title: "TaskFlow",
      // home: HomePage(),
    );
  }
}
