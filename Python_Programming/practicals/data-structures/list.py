# A list is an ordered, changeable (Mutable)
list_of_universities = ["University of Kigali", "RP", "UR", "Rwanda Polytechnic", "ULK"]
print(type(list_of_universities))
print(list_of_universities)

# List items are indexed, index start from 0, 1,....
print(list_of_universities[0])
print(list_of_universities[2])

# Negative index means start from the end of items
print(list_of_universities[-1])

# len() function to print the length of a list
print(len(list_of_universities))

list1 = [1, 2, 3, 6]
list2 = [25, 90, 89]
# Concatinate lists
newList = list1 + list2
print(newList)

# Re-assign an element in a list
list_of_universities[0] = "Havard University"
print(list_of_universities)

# remove() function, I can remove items
list_of_universities.remove("RP")
print(list_of_universities)

# Add item in the list, append() function
list_of_universities.append("Jomo Kenyatta")
print(list_of_universities)

# Check if one item is in the list, use in keyword
if "Makelele University" in list_of_universities:
    print("The university is in the list")
else:
    print("The name is not mentioned in the list")

"""_____________________________CHALLENGE1__________________________________
    Check if the name Mburabuturo is in the list, if it is in print its name
    if not the name be added in the list and it include the updated one!"""

if "Mburabuturo" in list_of_universities:
    print("The name is in the list of Universities")
else:
    list_of_universities.append("Mburabuturo")
    print(list_of_universities)

# Print the specific range, list[start, end], start will be included, the end will not...
marks = [49, 80, 30, 70, 78, 56, 85, 79, 100, 60]
print(marks[2:6])  # Item of index 2 is in the response, the index 6 item is excluded...

"""_____________________________CHALLENGE2__________________________________
    Write the newList whose items are a copy of the marks list between index 2:7,
    and after print that list"""
newMarks = marks[2:7]
print(newMarks) # Copy a list

# extend(), pop() and clear() functions
print(marks[0])
marks.remove(marks[0])
print(marks)
print(marks.pop())
studentsNames = ["Edmond", "Hirwa", "Patrick", "Kimenyi"]
eveningStudents = ["David", "Uwimana"]
# extend() function will copy the items from one list into another
studentsNames.extend(eveningStudents)
print(studentsNames)

# Removing every element in a list
for i in range(len(studentsNames) - 1, -1, -1):
    studentsNames.pop(i)
print(studentsNames)  # Output: []

# Using a while loop is another option u can use ...
studentsNames1 = ["Edmond", "Hirwa", "Patrick", "Kimenyi", "Byukusenge"]
print(studentsNames1)
while studentsNames1:
    studentsNames1.pop(0)
print(studentsNames1)  # Output: []
print("\n\n")

# Other option is to use clear function
number1 = [12, 34, 90, 80, 100]
print(number1)
number1.clear()  # empty the list
print(number1)

# sorting
number2 = [12, 34, 90, 80, 100]
number2.sort()
print(number2)

# sorting in reverse!
number3 = [12, 34, 90, 80, 100]
number3.sort(reverse=True)  # Gives same results as the below method (reverse())!
# number3.reverse()
print(number3)
