# data dictionary,
# Immutable
# doesn't allow duplicates
person = {
    "FirstName": "Gilbert",
    "LastName": "NDAYISENGA",
    "age": 35,
    "title": "Lecturer",
    "company": "UoK",
    "salary": 1000
}

print(type(person))
print(person)


# keys() and value()
# Keys() will be used to print out the keys of your dictonary
# The values() will be used to print out the values of the dictonary
print(person["FirstName"])
sal = person["salary"]
print(sal)
person["salary"] = 3000
print(person["salary"])

# when you want to print keys u use keys()
print(person.keys())

# values() function to print out values
print(person.values())

company = {
    "companyName": "BAHATI Ltd",
    "address": "Kigali",
    "sector": "Kacyiru",
    "district": "Gasabo",
    "TIN": 1128,
    "working": True,
    "products": ["Accesories", "Telephone", "Repairing", "Sauna"]
}

print(type(company))

"""_______________________CHALLENGE1_____________________________"""
print("\n")
print(company["companyName"] + " " + company["products"][2])
print(f"\n>> The company name is: {company["companyName"]} And the product at index 2 is: {company["products"][2]}")
# print(company["products"][2])
