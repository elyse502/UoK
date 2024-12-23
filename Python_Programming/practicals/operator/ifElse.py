a = 300
b = 300
if (a < b):
    print("a is less than b")
elif (a == b):
    print("a is equal to b")
else:
    print("a is greater than b")

x = 20
y = 10
if(x < y):
    print("a is greater than b")
print("Done by Gilbert")

"""When a student has between 50 and 59, this is pass
    when he has between 68 and 69 second class lower
    when he has between 78 and 79 is second class upper
    when he has above 80, he is a first class division"""

totalMarks = float(input("What's your total score over all: "))
if 50 <= totalMarks <= 59:
    print("\n>> Pass")
elif 60 <= totalMarks <= 69:
    print("\n>> Second class lower")
elif 70 <= totalMarks <= 79:
    print("\n>> Second class upper")
elif totalMarks >= 80:
    print("\n>> First class division")
else:
    print("\n>> Failed")
