import 'package:cloud_firestore/cloud_firestore.dart';

class UserService {
  // Instance of Firestore, pointing to our 'users' collection.
  final CollectionReference _usersCollection = FirebaseFirestore.instance.collection('users');

  /// Adds a new user document to the 'users' collection in Firestore.
  Future<void> addUser({
    required String firstName,
    required String lastName,
    required String username,
    required String email,
  }) {
    return _usersCollection.add({
      'firstName': firstName,
      'lastName': lastName,
      'username': username,
      'email': email,
      'createdAt': Timestamp.now(), // Good for sorting by creation time.
    });
  }

  /// Retrieves a real-time stream of all users from the collection,
  /// ordered by their creation time in descending order (newest first).
  Stream<QuerySnapshot> getUsersStream() {
    return _usersCollection.orderBy('createdAt', descending: true).snapshots();
  }
}