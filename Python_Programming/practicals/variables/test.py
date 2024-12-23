"""Prints each character in a string"""
name = input("Name: ")

for c in name:
    if (c.lower() == "f"):
       print(c)
       break
else:
    print("You didn't find what you were looking for")

print("\n")
for i in range(10):
    print(i)
