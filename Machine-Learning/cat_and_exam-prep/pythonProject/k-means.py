import warnings
warnings.filterwarnings("ignore")

# Step 1: import libraries
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans


# Global variables
separtor = "_"*30
divider = f"\n{"*" * 90}\n"

# Step 2: Prepare the data set
data = {
    "PointID": [x for x in range(1, 5)],
    "Feature1(X)": [1.0, np.nan, 5.0, 8.0],
    "Feature2(Y)": [2.0, 1.8, np.nan, 8.0]
}

df = pd.DataFrame(data)
print(f"{separtor} Original DataFrame with Missing Values: {separtor}\n{df}")

# Treating missing values
df["Feature1(X)"] = df["Feature1(X)"].fillna(df["Feature1(X)"].mean())
df["Feature2(Y)"] = df["Feature2(Y)"].fillna(df["Feature2(Y)"].mean())
print(f"{divider}\n{separtor} DataFrame after treating missing values: {separtor}\n{df}")

# Step 3: Initialize the K-Means Model
kmeans = KMeans(n_clusters=2, random_state=0)

# Step 4: Fit the Model to the Data
kmeans.fit(df)

# Step 5: Obtain Cluster Centroids and Labels
centroids = kmeans.cluster_centers_
labels = kmeans.labels_
print("\n{divider}=> Centroids:\n", centroids)
print(f"{divider}\n=> Labels:", labels)

"""
    The model effectively separated the data into two meaningful clusters, 
    with one data point showing significant variation from the others.
"""

# Step 6: Visualize Clusters and Centroids
colors = ["red" if label == 0 else "blue" for label in labels]

plt.figure(figsize=(10, 6))

df = df.values

plt.scatter(df[:, 0], df[:, 1], c=colors)
plt.scatter(centroids[:, 0], centroids[:, 1], s=300, c="yellow", marker='*')
plt.title("K-Means Clustering Example", fontsize=16, fontweight="bold", pad=26)
plt.show()

# Final Evaluation
title = " Final Evaluation "
print(f"""\n\n{"-"*110}\n\n{"\t"*3}{title:*^59}\n
            The K-Means model successfully grouped the data into two distinct clusters. 
            Most observations belong to Cluster 0, while one observation forms a separate cluster, 
            indicating a noticeable difference in feature values. The centroids clearly represent the 
            average position of each cluster in the feature space.
""")