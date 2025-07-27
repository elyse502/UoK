import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'user_service.dart';

class ViewUsersScreen extends StatefulWidget {
  const ViewUsersScreen({super.key});

  @override
  State<ViewUsersScreen> createState() => _ViewUsersScreenState();
}

class _ViewUsersScreenState extends State<ViewUsersScreen> {
  // An instance of the UserService to fetch user data.
  final UserService _userService = UserService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Registered Users')),
      // StreamBuilder listens to the stream from Firestore and rebuilds the UI on new data.
      body: StreamBuilder<QuerySnapshot>(
        stream: _userService.getUsersStream(),
        builder: (context, snapshot) {
          // 1. Show a loading indicator while waiting for data.
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          // 2. Show an error message if something went wrong.
          if (snapshot.hasError) {
            return Center(
              child: Text('Something went wrong: ${snapshot.error}'),
            );
          }

          // 3. If there's no data, show a friendly message.
          if (!snapshot.hasData || snapshot.data!.docs.isEmpty) {
            return const Center(
              child: Text(
                'No users have been added yet.',
                style: TextStyle(fontSize: 18, color: Colors.grey),
              ),
            );
          }

          // 4. If we have data, get the list of documents.
          final users = snapshot.data!.docs;

          // 5. Build the ListView.
          return ListView.builder(
            itemCount: users.length,
            itemBuilder: (context, index) {
              // Get the data for the current user.
              // We cast it as a Map to access fields by name.
              final user = users[index].data() as Map<String, dynamic>;

              // For safety, provide default values if a field is null.
              final String username = user['username'] ?? 'N/A';
              final String email = user['email'] ?? 'No Email';
              final String fullName =
                  '${user['firstName'] ?? ''} ${user['lastName'] ?? ''}';

              return Card(
                margin: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                child: ListTile(
                  leading: const Icon(
                    Icons.person_outline,
                    color: Colors.indigo,
                  ),
                  title: Text(
                    username,
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  subtitle: Text('$fullName\n$email'),
                  isThreeLine: true, // Allows for more space in the subtitle.
                ),
              );
            },
          );
        },
      ),
    );
  }
}
