"""When a student has between 50 and 59, this is pass
    when he has between 68 and 69 second class lower
    when he has between 78 and 79 is second class upper
    when he has above 80, he is a first class division"""

totalMarks = float(input("What's your total score over all: "))
if totalMarks >= 50 and totalMarks <= 59:
    print("\n>> Pass")
elif totalMarks >= 60 and totalMarks <= 69:
    print("\n>> Second class lower")
elif totalMarks >= 70 and totalMarks <= 79:
    print("\n>> Second class upper")
elif totalMarks >= 80:
    print("\n>> First class division")
else:
    print("\n>> Failed")
