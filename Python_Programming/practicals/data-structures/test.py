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

"""_________CHALLENGE1___________
Loop into the outer data dictionary and print all items using a loop"""
'''for element in countryRepresentative.values():
    print(element)'''

# THE CORRECT ONE:
for country, info in countryRepresentative.items():
    print(f"Country: {country}")
    for key, value in info.items():
        print(f"{key}: {value}")
    print()

print()
age = 12
is_adult = "Yes" if age >= 18 else "No"
print(is_adult)  # Output: Yes
