# Arithmetic operartors +, -, /, %
x = 2000
y = 85
sum = x + y
print(sum)

'''Kamali has a saving of 2500000.
    Then on Monday he removed from the balance 50000 for beer
    on next day his employer credited him with 400000
    then the evening his wife paid from balance 85000 a school.
    After getting the new balance, Kamali distributed to his 3 
    children calculate them without floating'''
amount = 2500000
withdraw = 50000
credit = 400000
fees = 85000
balance = (amount + credit) - (withdraw + fees)
print(f"> At first Kamali had the Balance of : {amount} RWF\n>> Then after all spendings and credits he remained with : {balance} RWF")
print("The new balance is: ", balance)
rest_amount = balance % 3
print(f"Kamali Remained with only {rest_amount} RWF after giving some bucks to his 3 children!")
