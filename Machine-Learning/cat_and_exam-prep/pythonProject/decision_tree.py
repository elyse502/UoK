import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier, plot_tree
from sklearn.metrics import accuracy_score, confusion_matrix, classification_report

# Global variables
separator = "_"*30
divider = "\n" + "*" * 90 + "\n"

data = {
    "Feature_1": [5, 7, 8, 5, 6, 2, 1, 3, 4, 3],
    "Feature_2": [2, 3, 4, 1, 3, 7, 8, 6, 5, 7],
    "Target": [0, 0, 0, 0, 0, 1, 1, 1, 1, 1]
}

df = pd.DataFrame(data)
print(f"{separator} DataFrame: {separator}\n{df}")

print(f"{separator} Missing Data Info: {separator}\n{df.isnull().sum()}")
print(f"{divider}\n{separator} Dataset Statistcal Summary: {separator}\n{df.describe()}")
print(f"{divider}\n{separator} Dataset Shape: {separator}\n{df.shape}")
print(f"{divider}\n{separator} DataFrame Summary: {separator}")
print(df.info())
print(divider)

# Select features and target
X = df[["Feature_1", "Feature_2"]]
y = df["Target"]

# Split data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

dt_model = DecisionTreeClassifier(random_state=42)
dt_model.fit(X_train, y_train)
print("\n✅ Decision Tree Model trained successfully!")
print(divider)

y_pred = dt_model.predict(X_test)
print(f"=> Predicted Values: {y_pred}\n=> Actual Values: {y_test.values}")

# Evaluate the model
accuracy = accuracy_score(y_test, y_pred)
conf_matrix = confusion_matrix(y_test, y_pred)
class_report = classification_report(y_test, y_pred)

print(f"\n=> Accuracy: {accuracy}\n{divider}\n=> Confusion Matrix:\n{conf_matrix}\n{divider}\n=> Classification Report:\n{class_report}")
print(divider)

# Visualize the Decision Tree
plt.figure(figsize=(12, 8))
plot_tree(dt_model, feature_names=["Feature_1", "Feature_2"], class_names=["0", "1"], filled=True, rounded=True, fontsize=12)

plt.title("Decision Tree Visualization", fontsize=16, fontweight="bold", fontstyle="italic", pad=20)

plt.show()