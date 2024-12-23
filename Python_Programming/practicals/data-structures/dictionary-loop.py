company = {
    "owner": "Gilbert",
    "Company_name": "TeyaHome Ldt",
    "StartingDate": 2024,
    "status": "working",
    "location": "Kigali city"
}

# loop through the keys of dictionary, then print out all the dictionary keys
print("All keys inside our dictionary!")
for item in company:
    print(item)

# When you try to access none existing key in your dict it will be created
company["numberOfWorkers"] = 30

# loop through the keys of dictionary, then print out all the dictionary values
print("\nAll values inside our dictionary!")
for item in company:
    print(company[item])  # values are accessed using the key
# Other way we can use is by using the value() function and check
# are in the dictionary.
print("Other way to access values:")
for value in company.values():
    print(value)
