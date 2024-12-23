guestName = "Muhizi"

def globalFunction():
    print(guestName)
globalFunction()

def localVariable():
    age = 20
    print(age)
localVariable()

def globalVariable():
    global x # Now is global variable declared inside function
    x = 100
    print(x)
globalVariable()
x = 34.5
print(x)
