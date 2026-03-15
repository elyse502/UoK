void main() {
  Set<String> months = {'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'};
  
  for (var month in months) {
    print(month);
  }
  
  var numberSet = {1, 2, 3, 4, 5, 6};
  
  // Check if numberSet contains 5
  if (numberSet.contains(5) == true) {
    print('5 Exist in Set');
  }
}