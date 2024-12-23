import pandas as pd

# Create sample dataset
dataInfo = {
    'CustomerID': [1001, 1002, 1001, 1003, 1002],
    'TransactionDate': ['2023-01-15', '2023-03-22', '2023-04-10', '2023-12-05', '2023-05-18'],
    'Amount': [50.00, 200.00, 100.00, 50.00, 120.00],
    'Category': ['Electronics', 'Clothing', 'Electronics', 'Books', 'Clothing']
}
df = pd.DataFrame(dataInfo)
print(">> THE Data Frame:")
print(df)
print()

# Write the DataFrame to a CSV file
df.to_csv('transaction.csv', index=False)
print(f'>> CSV file "transaction.csv" has been created: \n {df.to_string(index=False)}')
print()


# a) Write a Python function to load the dataset into a Pandas DataFrame
def load_dataset(file_path):
    """
    Loads the dataset from the specified file path into a Pandas DataFrame.

    Args:
        file_path (str): The file path of the dataset.

    Returns:
        pd.DataFrame: The dataset loaded into a Pandas DataFrame.
    """
    return pd.read_csv(file_path)


# b) Write code to calculate the total transaction amount for each customer
def get_total_transaction_per_customer(df):
    """
    Calculates the total transaction amount for each customer.

    Args:
        df (pd.DataFrame): The DataFrame containing the transaction data.

    Returns:
        pd.Series: A Series with the total transaction amount for each customer.
    """
    return df.groupby('CustomerID')['Amount'].sum()


# c) Develop code to find the average transaction amount for each category
def get_average_transaction_per_category(df):
    """
    Calculates the average transaction amount for each category.

    Args:
        df (pd.DataFrame): The DataFrame containing the transaction data.

    Returns:
        pd.Series: A Series with the average transaction amount for each category.
    """
    return df.groupby('Category')['Amount'].mean()


"""d) Write code to filter the DataFrame to include only transactions that occurred in the year 2023. 
Create a new column called TransactionMonth that extracts the month from the TransactionDate."""
def filtered_2023_transactions(df):
    """
    Processes the transaction data for the year 2023.

    Args:
        df (pd.DataFrame): The DataFrame containing the transaction data.

    Returns:
        pd.DataFrame: The DataFrame containing the processed transactions for the year 2023.
    """
    # Convert TransactionDate to datetime
    df['TransactionDate'] = pd.to_datetime(df['TransactionDate'])

    # Filter for 2023 transactions
    df_2023 = df[df['TransactionDate'].dt.year == 2023]

    # Create TransactionMonth column
    df_2023['TransactionMonth'] = df_2023['TransactionDate'].dt.month

    return df_2023


# Test the functions
print(f'>> Total transaction amount for each customer: \n {get_total_transaction_per_customer(df)}')
print()
print(f'>> Average transaction amount for each category: \n {get_average_transaction_per_category(df)}')
print()
print(f">> Overall Processed transactions for 2023 year: \n {filtered_2023_transactions(df)}")
