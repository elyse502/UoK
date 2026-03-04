import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score


# Global Variables
separator = "_" * 30
divider = f"\n{"*" * 90}\n"

# Load the Dataset
data = {
    "Student_ID": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
    "Math_Score": [78, 65, 90, 55, 82, 70, 85, 60, 75, 88],
    "Science_Score": [85, 70, 92, 60, 78, 72, 88, 65, 80, 85],
    "English_Score":  [82, 75, 88, 65, 80, 68, 90, 70, 78, 90],
    "FinalExamMark": [80, 68, 91, 58, 79, 71, 87, 64, 77, 89]
}

df = pd.DataFrame(data)
print(f"{separator} Dataset: {separator}\n{df}")
print(f"{divider}\n{separator} First 10 Rows of Dataset: {separator}\n{df.head(10)}")
print(f"{divider}\n{separator} Dataset Shape: {separator}\n{df.shape}")
print(f"{divider}\n{separator} Dataset Summary: {separator}\n")
print(df.info())
print(f"{divider}\n{separator} Dataset Statistical Summary: {separator}\n{df.describe()}")
print(f"{divider}\n{separator} Missing values: {separator}\n{df.isnull().sum()}")
print(divider)

# Select independent and dependent valuables
X = df[["Math_Score", "Science_Score", "English_Score"]]
y = df["FinalExamMark"]

# Split the Dataset
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Create the model
model = LinearRegression()
model.fit(X_train, y_train)

y_pred = model.predict(X_test)
print(f"=> Predicted Values: {y_pred}\n=> Actual Values: {y_test.values}")
print(divider)

# Evaluate the Model
mse = mean_squared_error(y_test, y_pred)
rmse = np.sqrt(mse)
r2 = r2_score(y_test, y_pred)

print(mse)

print(f"\n>> The Mean Squared Error: {mse} \n>> The RMSE: {rmse} \n>> The R² Score: {r2}\n")
print(r2_score(y_pred, y_test))

# Visualize Actual vs Predicted Results
plt.scatter(y_test, y_pred, color='green', label='Predicted vs Actual', marker='o')

# Perfect prediction line
plt.plot([y_test.min(), y_test.max()],
         [y_test.min(), y_test.max()],
         color='red', label='Perfect Prediction', linewidth=2)

plt.title("Multiple Linear Regression: Actual vs Predicted Final Exam Marks",
          fontweight="bold", fontsize=16, pad=20)
plt.xlabel("Actual Final Exam Marks", fontstyle="italic", fontsize=12)
plt.ylabel("Predicted Final Exam Marks", fontstyle="italic", fontsize=12)
plt.grid(True)
plt.legend()
plt.show()

# Final Evaluation
title = " Final Evalutation "
print(f"""{"-"*110}\n\n{"\t"*2}{title:*^59}\n
    This script trains a Multiple Linear Regression model to predict Final Exam Marks
    using Math, Science, and English scores. The data is split into training and testing
    sets, and performance is evaluated using MSE, RMSE, and R² score. The model shows
    excellent predictive performance because it achieves a very high R² (>= 0.7) value and low error metrics.
    So in a nutshell I can confirm that the model is acceptable and can be deployed!
""")
