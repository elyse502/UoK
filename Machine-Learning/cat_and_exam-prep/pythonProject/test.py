import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, r2_score


# Global variables
separator = "_"*20
divider = "\n" + "*"*90 + "\n"

data = {
    "YearsExperience": [1.1, 2.0, 3.2, 4.5, 5.1, 6.8, 7.5, 8.3, 9.0, 10.5],
    "Salary": [39343, 46205, 54445, 60000, 65200, 72500, 78000, 83000, 88000, 95000]
}

# Step 2 - Load and Prepare data
df = pd.DataFrame(data)

print(f"{separator} DataFrame: {separator}")
print(df)
print(divider)
print(f"{separator} DataFrame Summary: {separator}")
print(df.info())
print()
print(df.head())
print(divider)
print(f"{separator} Missing values: {separator}")
print(df.isnull().sum())
print(divider)

X = df[["YearsExperience"]]
y = df["Salary"]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

lr_model = LinearRegression()
lr_model.fit(X_train, y_train)

y_pred = lr_model.predict(X_test)
print(f"=> Predicted Values: {y_pred}\n=> Actual Values: {y_test.values}")
print(divider)

mse = mean_squared_error(y_test, y_pred)
rmse = np.sqrt(mse)
r2 = r2_score(y_test, y_pred)

print(f"\n>> The Mean Squared Error: {mse} \n>> The RMSE: {rmse} \n>> The R² Score: {r2}\n")
print(divider)

plt.scatter(X_test, y_test, color="green", label="Actual Salary")
plt.plot(X_test, y_pred, label="Predicted Salary", color="red", linestyle="-", linewidth=2)
plt.title("Salary vs Experience", fontsize=16, fontweight="bold", pad=20)
plt.xlabel("Years of Experience", fontsize=12, fontstyle="italic")
plt.ylabel("Salary", fontsize=12, fontstyle="italic")
plt.grid(True)
plt.legend()
plt.show()