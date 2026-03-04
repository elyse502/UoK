import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, r2_score
from sklearn.svm import SVC
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split

# Create the dataset
np.random.seed(42)
n = 1000

data = {
    'Task_Completion_Rate': np.random.normal(7, 2, n),
    'Customer_Satisfaction': np.random.uniform(1, 10, n),
    'Hours_Worked_Per_Week': np.random.normal(40, 5, n),
    'Performance': np.random.choice([0, 1], n, p=[0.5, 0.5])
}

df_svm = pd.DataFrame(data)

# Features and target
X = df_svm[['Task_Completion_Rate', 'Customer_Satisfaction', 'Hours_Worked_Per_Week']]
y = df_svm['Performance']

# Split data
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Descriptive statistics before scaling
print("Descriptive Statistics BEFORE Scaling:")
print(X_train.describe())

# Scale the features
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# Convert back to DataFrame for display
X_train_scaled_df = pd.DataFrame(X_train_scaled, columns=X.columns)

print("\n" + "="*50)
print("Descriptive Statistics AFTER Scaling:")
print(X_train_scaled_df.describe())

# Train SVM classifier
svm_model = SVC(kernel='rbf', C=1.0, gamma='scale', random_state=42)
svm_model.fit(X_train_scaled, y_train)

print("\n✅ SVM Model trained successfully!")
print(f"Parameters used: kernel='rbf', C=1.0, gamma='scale'")

# Make predictions
y_pred = svm_model.predict(X_test_scaled)

# Convert predictions to DataFrame for descriptive statistics
y_pred_df = pd.DataFrame(y_pred, columns=['Predicted_Performance'])

print("Predicted Performance Distribution:")
print(y_pred_df.describe())

print(f"\nPrediction Summary:")
print(f"Total predictions: {len(y_pred)}")
print(f"Predicted Class 0 (Low Performance): {sum(y_pred == 0)} ({sum(y_pred == 0)/len(y_pred)*100:.1f}%)")
print(f"Predicted Class 1 (High Performance): {sum(y_pred == 1)} ({sum(y_pred == 1)/len(y_pred)*100:.1f}%)")

# Compare with actual
print(f"\nActual Test Set Distribution:")
print(f"Actual Class 0: {sum(y_test == 0)} ({sum(y_test == 0)/len(y_test)*100:.1f}%)")
print(f"Actual Class 1: {sum(y_test == 1)} ({sum(y_test == 1)/len(y_test)*100:.1f}%)")

from sklearn.metrics import accuracy_score, confusion_matrix, classification_report

# Calculate accuracy
accuracy = accuracy_score(y_test, y_pred)
print(f"Model Accuracy: {accuracy:.4f} ({accuracy*100:.2f}%)")

# Generate confusion matrix
cm = confusion_matrix(y_test, y_pred)
print("\nConfusion Matrix:")
print("                 Predicted")
print("                 Negative  Positive")
print(f"Actual Negative    {cm[0,0]:5d}    {cm[0,1]:5d}")
print(f"Actual Positive    {cm[1,0]:5d}    {cm[1,1]:5d}")

# Calculate metrics from confusion matrix
tn, fp, fn, tp = cm.ravel()

print("\nDetailed Metrics:")
print(f"True Negatives (Correctly predicted Low): {tn}")
print(f"False Positives (Incorrectly predicted High): {fp}")
print(f"False Negatives (Missed High performers): {fn}")
print(f"True Positives (Correctly predicted High): {tp}")

print(f"\nPrecision (High Performance): {tp/(tp+fp):.4f}")
print(f"Recall (High Performance): {tp/(tp+fn):.4f}")
print(f"Specificity (Low Performance): {tn/(tn+fp):.4f}")
print(f"F1-Score: {2*tp/(2*tp+fp+fn):.4f}")

# Classification report
print("\nClassification Report:")
print(classification_report(y_test, y_pred, target_names=['Low Performance', 'High Performance']))

# Visualize confusion matrix
plt.figure(figsize=(8, 6))
sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', 
            xticklabels=['Predicted Low', 'Predicted High'],
            yticklabels=['Actual Low', 'Actual High'])
plt.title('Confusion Matrix - SVM Classifier')
plt.ylabel('Actual')
plt.xlabel('Predicted')
plt.show()