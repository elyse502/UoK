import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, confusion_matrix, classification_report
from sklearn.datasets import make_classification


# Global variables
separator = "_"*20
divider = "\n" + "*"*90 + "\n"

np.random.seed(42)

# Generate dataset
x, y = make_classification(n_samples=100, n_features=2, n_redundant=0, n_clusters_per_class=1, class_sep=2.0, random_state=42)

df = pd.DataFrame(x, columns=["Feature_1", "Feature_2"])

df["Target"] = y

# Introduce missing data explicitly
n_missing = 10 # 10% missing
missing_indices = np.random.choice(df.index, n_missing, replace=False)

df.loc[missing_indices, "Feature_1"] = np.nan
print(f"{separator} Dataset with missing values in Feature_1: {separator}")
print(df.loc[missing_indices]) # Show only rows with missing Feature_1
print(divider)
print(f"{separator} Count of missing values per column: {separator}")
print(df.isnull().sum())
print(divider)
# Treat missing values
df["Feature_1"] = df["Feature_1"].fillna(df["Feature_1"].mean())

print(f"{separator} Count of missing values per column after treatment: {separator}")
print(df.isnull().sum())
print(divider)
print(f"{separator} Shape: {separator}")
print(df.shape)
print(divider)

# Select Feature and Target
X = df[["Feature_1", "Feature_2"]]
y = df["Target"]

# Split the dataset
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

svc_model = SVC(kernel="linear", random_state=42)
svc_model.fit(X_train, y_train)
print("\n✅ SVC Model trained successfully!")
print(divider)

y_pred = svc_model.predict(X_test)
print(f"=> Predicted Values: {y_pred}\n=> Actual Values: {y_test.values}")

# Evaluate the model
accuracy = accuracy_score(y_test, y_pred)
conf_matrix = confusion_matrix(y_test, y_pred)
class_report = classification_report(y_test, y_pred)

print(f"\n=> Accuracy: {accuracy}\n=> Confusion Matrix:\n{conf_matrix}\n=> Classification Report:\n{class_report}")
print(divider)

# Visualize the decision boundary
# Plotting Scatter and Plot
plt.figure(figsize=(8, 6))

X_test = X_test.values # Convert to numpy array for plotting

# Scatter plot of test points colored by class
plt.scatter(X_test[:, 0], X_test[:, 1], c=y_test , cmap='bwr', edgecolors='k')

# Define grid boundaries based on test data
x_min, x_max = X_test[:, 0].min() - 1, X_test[:, 0].max() + 1
y_min, y_max = X_test[:, 1].min() - 1, X_test[:, 1].max() + 1

xx , yy = np.meshgrid(np.linspace(x_min, x_max, 500),
                      np.linspace(y_min, y_max, 500))

# Compute the decision function on the grid
# Z = svc_model.decision_function(np.c_[xx.ravel(), yy.ravel()]).reshape(xx.shape)
grid = np.c_[xx.ravel(), yy.ravel()]
grid_df = pd.DataFrame(grid, columns=X_train.columns)

Z = svc_model.decision_function(grid_df).reshape(xx.shape)

# Plot decision boundary and margins
plt.contour(xx, yy, Z, levels = [0], colors = 'k', linewidths=2) # Hyperplane
plt.contour(xx, yy, Z, levels = [-1, 1], colors = 'k', linestyles = "--") #  Margins (hard and soft margins)

plt.title("SVM Decision Boundary and Margins", fontsize=16, fontweight="bold", pad=20)
plt.xlabel("Feature_1", fontsize=12, fontstyle="italic")
plt.ylabel("Feature_2", fontsize=12, fontstyle="italic")
plt.show()