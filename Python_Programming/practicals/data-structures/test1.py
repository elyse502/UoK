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

for item in countryRepresentative.values():
    # print(item)
    for element in item.values():
        print(element)
    print()
