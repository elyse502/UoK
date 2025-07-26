void main() {
  List<String> names = ['Danny', 'Straton', 'I', 'Noheri'];

  // Using for-in loop  
  for (var name in names) {
    print(name);
  }
  
  // Using traditional for loop  
  for (int i = 0; i < names.length; i++) {
    print('Name ${i} is ${names[i]}');
  }
}