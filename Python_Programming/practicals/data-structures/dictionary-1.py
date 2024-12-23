"""dictionary is one of the four datatype to store multiple items in a variable.
    Two type of dictionaries : simple dictionary and nested dictionary
    Single dictionary. dictName={ key:value }"""

company = {
    "owner": "Gilbert",
    "Company_name": "TeyaHome Ldt",
    "StartingDate": 2024,
    "status": "working",
    "location": "Kigali city"
}

print(company)

# print one item from a dictionary
# you have to use key: value
print(company["Company_name"])

# change one item
company["location"] = "Western Province"
print(company["location"])

# When you want to change the key which not present, the item is added to the dictionary
company["numberOfWorkers"] = 30
print(company)

# keys(), values()
print(company.keys())  # print out all the keys in data dictionary
# values
print(company.values())  # access the all values of the dictionary

"""pop(), popitem(), del keyword and clear function
    pop() function is used to remove an item from the dictionary"""
print()
print(">> pop(), popitem(), del keyword and clear function")
print("___________________________________________________")
students_marks = {
    "student_name": "Bahati",
    "last_name": "Yves",
    "course_name": "Programming with python",
    "Total_marks": 78,
    "decision": "pass",
    "Other_courses": ["java", "Database", "AI", "IoT"]
}

print(students_marks["Other_courses"][1])

print(len(students_marks))
# Remove a specific item from the dictionary
print(students_marks.pop("decision"))
print(students_marks)
print(len(students_marks))

# popitem() is used to remove the last item
print()
students_marks.popitem()
print(students_marks)
print(len(students_marks))

# del is a key word used to remove the specified item
print()
del students_marks["student_name"]
print(len(students_marks))
print(students_marks)
# del keyword is also used to completely delete the dictionary
del students_marks

# clear() function which is used to empty the dictionary
print()
people = {
    "name": "Gilbert",
    "Title": "Lecturer",
    "course": "Programming with python"
}
print(people)
people.clear()
print(people)

# Nested data dictionary
print()
print(">> Nested data dictionary")
print("_________________________")
countryRepresentative = {
    "Rwanda": {
        "PresidentName": "HE.Paul KAGAME",
        "Role": "country leader",
    },
    "Uganda": {
        "PresidentName": "Museveni",
        "Role": "country leader",
        "number_of_ministries": 12
    },
    "Sudan": {
        "PresidentName": "Omar al-Bashir",
        "Role": "country leader",
        "number_of_ministries": 30
    }
}
print(countryRepresentative)
# print the inner data dictionary.
print(countryRepresentative["Rwanda"])
# print the inner data dictionary item.
print(f'> The president of RWANDA is {countryRepresentative["Rwanda"]["PresidentName"]}')

"""__________________________CHALLENGE1________________________________
Loop into the outer data dictionary and print all items using a loop"""
print()
for element in countryRepresentative.values():
    print(element)

print()
for country, info in countryRepresentative.items():
    print(f"Country: {country}")
    for key, value in info.items():
        print(f"{key}: {value}")
    print()

