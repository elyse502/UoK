tuple = (12, 90, 30, 12, 9, 7)
print(type(tuple))
# Loop through a tuple
print(tuple[0])
print(tuple[2:4])

countryNames = ("Rwanda")  # It will be considered as string not a tuple because of comma
print(countryNames)
print(type(countryNames))
print("\n")

countryNames1 = ("Rwanda",)  # Now this is a tuple because we added a comma this time
print(countryNames1)
print(type(countryNames1))

# Concatenating tuples
tuple1 = (12, 34, 56)
tuple2 = (30, 100, 230)
newTuple = tuple1+tuple2
print(newTuple)
