<details>
  <summary>CAT PREP</summary>

Here are **simple, exam-ready answers**, using your **mysqli pattern + clean HTML + validation**.

---

# QUESTION 1

## a) Form + Insert with validation

```php
<?php
$host = "localhost";
$username = "root";
$password = "";
$database = "company_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['save'])){
    $id = $_POST['id'];
    $name = $_POST['name'];
    $position = $_POST['position'];
    $salary = $_POST['salary'];

    // Validation
    if($id == "" || $name == "" || $position == "" || $salary == ""){
        echo "<script>alert('All fields are required');</script>";
    }
    elseif(!is_numeric($salary)){
        echo "<script>alert('Salary must be numeric');</script>";
    }
    else{
        // Check duplicate ID
        $check = mysqli_query($connection, "SELECT * FROM employees WHERE emp_id='$id'");
        if(mysqli_num_rows($check) > 0){
            echo "<script>alert('Employee ID already exists');</script>";
        } else {
            $sql = "INSERT INTO employees(emp_id, emp_name, position, salary)
                    VALUES('$id','$name','$position','$salary')";
            if(mysqli_query($connection, $sql)){
                echo "<script>alert('Inserted successfully');</script>";
            } else {
                echo "<script>alert('Error inserting');</script>";
            }
        }
    }
}
?>

<!DOCTYPE html>
<html>
<head>
<style>
body { font-family: Arial; }
form { width: 300px; margin: auto; }
input { width: 100%; padding: 8px; margin: 5px 0; }
button { width: 100%; padding: 10px; background: blue; color: white; border: none; }
</style>
</head>
<body>

<form method="POST">
<h3>Add Employee</h3>
<input type="text" name="id" placeholder="Employee ID">
<input type="text" name="name" placeholder="Employee Name">
<input type="text" name="position" placeholder="Position">
<input type="text" name="salary" placeholder="Salary">
<button type="submit" name="save">Save</button>
</form>

</body>
</html>
```

---

## b) JavaScript alert (already included above)

Used like this:

```php
echo "<script>alert('Inserted successfully');</script>";
```

---

## c) Display records in styled table

```php
<?php
$result = mysqli_query($connection, "SELECT * FROM employees");
?>

<table border="1" cellpadding="10" style="margin:auto; margin-top:20px;">
<tr>
<th>ID</th>
<th>Name</th>
<th>Position</th>
<th>Salary</th>
</tr>

<?php
while($row = mysqli_fetch_assoc($result)){
echo "<tr>
<td>{$row['emp_id']}</td>
<td>{$row['emp_name']}</td>
<td>{$row['position']}</td>
<td>{$row['salary']}</td>
</tr>";
}
?>
</table>
```

---

# QUESTION 2

## A & B JavaScript solution

```html
<script>
  let days = prompt("Enter number of absent days:");

  if (days >= 0 && days <= 2) {
    alert("Status: Normal");
  } else if (days >= 3 && days <= 5) {
    alert("Status: Warning");
  } else if (days >= 6 && days <= 10) {
    alert("Status: Manager Review");
  } else if (days > 10) {
    alert("Status: HR Action Required");
  } else {
    alert("Invalid input");
  }
</script>
```

---

# QUESTION 3

## a) PHP function calculateFine()

```php
<?php
function calculateFine($daysLate){
    if($daysLate >= 0 && $daysLate <= 3){
        return 0;
    }
    elseif($daysLate >= 4 && $daysLate <= 7){
        return 500;
    }
    elseif($daysLate >= 8 && $daysLate <= 14){
        return 1000;
    }
    else{
        return 2000;
    }
}
?>
```

---

## b) Return message with fine

```php
<?php
function calculateFine($daysLate){
    if($daysLate >= 0 && $daysLate <= 3){
        $fine = 0;
    }
    elseif($daysLate >= 4 && $daysLate <= 7){
        $fine = 500;
    }
    elseif($daysLate >= 8 && $daysLate <= 14){
        $fine = 1000;
    }
    else{
        $fine = 2000;
    }

    return "Book is $daysLate days late. Fine: $fine RWF";
}

// Example
echo calculateFine(5);
?>
```

</details>

<br/><hr/><br/>

<details>
  <summary>Exam Prep</summary>

# Advanced Web Development - Exam Preparation Guide

# SECTION A

## QUESTION 1 (Compulsory) - Student Registration System

### a) PHP Code to Establish Database Connection (3 Marks)

```php
<?php
// Database connection using MySQLi (procedural style)

$host = "localhost";
$username = "root";
$password = "";
$database = "student_db";

$connection = mysqli_connect($host, $username, $password, $database);

if ($connection) {
    echo "<script>alert('Connection successful!');</script>";
} else {
    die("Connection failed: " . mysqli_connect_error());
}
?>
```

### b) Query to Retrieve Records by Joining Tables (4 Marks)

```php
<?php
// Join students and programmes tables to display results

$query = "SELECT students.student_id, students.full_name, students.reg_no,
                students.email, programmes.programme_name
          FROM students
          INNER JOIN programmes ON students.programme_id = programmes.programme_id";

$result = mysqli_query($connection, $query);

if (mysqli_num_rows($result) > 0) {
    echo "<table border='1' cellpadding='10' cellspacing='0'>";
    echo "<tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Registration No</th>
            <th>Email</th>
            <th>Programme</th>
          </tr>";

    while ($row = mysqli_fetch_assoc($result)) {
        echo "<tr>";
        echo "<td>" . $row['student_id'] . "</td>";
        echo "<td>" . $row['full_name'] . "</td>";
        echo "<td>" . $row['reg_no'] . "</td>";
        echo "<td>" . $row['email'] . "</td>";
        echo "<td>" . $row['programme_name'] . "</td>";
        echo "</tr>";
    }
    echo "</table>";
} else {
    echo "No records found.";
}

mysqli_close($connection);
?>
```

### c) PHP Code to Redirect After 5 Seconds (3 Marks)

```php
<?php
// After performing an action (e.g., inserting data)
// Redirect to view.php after 5 seconds

echo "Data inserted successfully! You will be redirected in 5 seconds.";

// Method 1: Using header with refresh
header("refresh:5; url=view.php");

// Method 2: Using meta tag in HTML
// echo "<meta http-equiv='refresh' content='5; url=view.php'>";

// Method 3: Using JavaScript
// echo "<script>
//         setTimeout(function() {
//             window.location.href = 'view.php';
//         }, 5000);
//       </script>";
?>
```

---

## QUESTION 2 - Employee Records Management

### a) Fetch Employee Data by ID and Display in Form (3 Marks)

```php
<?php
$host = "localhost";
$username = "root";
$password = "";
$database = "employee_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

// Get employee ID from URL parameter
$emp_id = $_GET['id'];

$query = "SELECT * FROM employees WHERE emp_id = '$emp_id'";
$result = mysqli_query($connection, $query);
$employee = mysqli_fetch_assoc($result);
?>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Employee</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        .form-container { width: 400px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input[type="text"], input[type="number"] { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc; border-radius: 4px; }
        button { margin-top: 20px; padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; }
        button:hover { background-color: #45a049; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>Edit Employee Details</h2>
        <form action="update_employee.php" method="POST">
            <input type="hidden" name="emp_id" value="<?php echo $employee['emp_id']; ?>">

            <label>Employee Name:</label>
            <input type="text" name="emp_name" value="<?php echo $employee['emp_name']; ?>" required>

            <label>Department:</label>
            <input type="text" name="department" value="<?php echo $employee['department']; ?>" required>

            <label>Salary:</label>
            <input type="number" name="salary" value="<?php echo $employee['salary']; ?>" required>

            <label>Phone Number:</label>
            <input type="text" name="phone" value="<?php echo $employee['phone']; ?>" required>

            <button type="submit">Update Employee</button>
        </form>
    </div>
</body>
</html>

<?php mysqli_close($connection); ?>
```

### b) PHP Code to Update Employee Details (4 Marks)

```php
<?php
$host = "localhost";
$username = "root";
$password = "";
$database = "employee_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $emp_id = $_POST['emp_id'];
    $emp_name = $_POST['emp_name'];
    $department = $_POST['department'];
    $salary = $_POST['salary'];
    $phone = $_POST['phone'];

    $query = "UPDATE employees
              SET emp_name = '$emp_name',
                  department = '$department',
                  salary = '$salary',
                  phone = '$phone'
              WHERE emp_id = '$emp_id'";

    if (mysqli_query($connection, $query)) {
        echo "<script>
                alert('Employee updated successfully!');
                window.location.href = 'view_employees.php';
              </script>";
    } else {
        echo "Error updating record: " . mysqli_error($connection);
    }
}

mysqli_close($connection);
?>
```

### c) JavaScript to Print Employee Identification (3 Marks)

```javascript
// JavaScript function to print employee identification

function printEmployeeID(emp_id, emp_name, department) {
  // Create a new window for printing
  var printWindow = window.open("", "_blank", "width=600,height=400");

  // HTML content for the print window
  printWindow.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Employee ID Card</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                    margin: 0;
                }
                .id-card {
                    width: 350px;
                    padding: 20px;
                    border: 2px solid #333;
                    border-radius: 10px;
                    text-align: center;
                    box-shadow: 5px 5px 15px rgba(0,0,0,0.2);
                }
                .id-card h2 {
                    color: #2c3e50;
                    margin-bottom: 20px;
                }
                .id-card p {
                    font-size: 16px;
                    margin: 10px 0;
                    text-align: left;
                }
                .id-card .label {
                    font-weight: bold;
                    display: inline-block;
                    width: 120px;
                }
                .company {
                    background-color: #3498db;
                    color: white;
                    padding: 10px;
                    border-radius: 5px;
                    margin-bottom: 20px;
                }
            </style>
        </head>
        <body>
            <div class="id-card">
                <div class="company">
                    <h3>EMPLOYEE IDENTIFICATION CARD</h3>
                </div>
                <h2>${emp_name}</h2>
                <p><span class="label">Employee ID:</span> ${emp_id}</p>
                <p><span class="label">Department:</span> ${department}</p>
                <hr>
                <p style="font-size: 12px; text-align: center;">Authorized Signature</p>
                <p style="font-size: 10px; text-align: center;">This card is property of the company</p>
            </div>
            <script>
                window.print();
                setTimeout(function() { window.close(); }, 500);
            </script>
        </body>
        </html>
    `);

  printWindow.document.close();
}

// Usage example - call this function when a button is clicked
// <button onclick="printEmployeeID('EMP001', 'John Doe', 'IT')">Print ID</button>
```

---

## QUESTION 3 - Invoice Generation System (No Database)

### a) HTML Form to Capture Client and Product Details (2 Marks)

```html
<!DOCTYPE html>
<html>
  <head>
    <title>BERWA Shop - Invoice Generator</title>
    <style>
      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }

      body {
        font-family: "Segoe UI", Arial, sans-serif;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        min-height: 100vh;
        padding: 40px 20px;
      }

      .container {
        max-width: 600px;
        margin: 0 auto;
        background: white;
        border-radius: 15px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        overflow: hidden;
      }

      .header {
        background: linear-gradient(135deg, #2c3e50, #3498db);
        color: white;
        padding: 30px;
        text-align: center;
      }

      .header h1 {
        font-size: 28px;
        margin-bottom: 10px;
      }

      .header p {
        opacity: 0.9;
      }

      .form-section {
        padding: 30px;
      }

      .form-group {
        margin-bottom: 20px;
      }

      label {
        display: block;
        font-weight: 600;
        margin-bottom: 8px;
        color: #333;
      }

      input[type="text"],
      input[type="number"],
      select {
        width: 100%;
        padding: 12px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 14px;
        transition: border-color 0.3s;
      }

      input:focus {
        outline: none;
        border-color: #3498db;
      }

      .row {
        display: flex;
        gap: 20px;
      }

      .row .form-group {
        flex: 1;
      }

      button {
        width: 100%;
        padding: 14px;
        background: linear-gradient(135deg, #2c3e50, #3498db);
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 16px;
        font-weight: bold;
        cursor: pointer;
        transition: transform 0.2s;
      }

      button:hover {
        transform: translateY(-2px);
      }

      .error {
        color: #e74c3c;
        font-size: 12px;
        margin-top: 5px;
        display: none;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="header">
        <h1>🏪 BERWA Shop</h1>
        <p>Invoice Generation System</p>
      </div>

      <div class="form-section">
        <form
          id="invoiceForm"
          action="process_invoice.php"
          method="POST"
          onsubmit="return validateForm()"
        >
          <div class="form-group">
            <label>Client Name:</label>
            <input
              type="text"
              name="client_name"
              id="client_name"
              placeholder="Enter client full name"
            />
            <div class="error" id="client_name_error">
              Client name is required
            </div>
          </div>

          <div class="form-group">
            <label>Product Name:</label>
            <input
              type="text"
              name="product_name"
              id="product_name"
              placeholder="Enter product name"
            />
            <div class="error" id="product_name_error">
              Product name is required
            </div>
          </div>

          <div class="row">
            <div class="form-group">
              <label>Quantity:</label>
              <input
                type="number"
                name="quantity"
                id="quantity"
                placeholder="0"
                oninput="calculateTotals()"
              />
              <div class="error" id="quantity_error">
                Quantity must be a positive number
              </div>
            </div>

            <div class="form-group">
              <label>Unit Price (RWF):</label>
              <input
                type="number"
                name="unit_price"
                id="unit_price"
                placeholder="0"
                oninput="calculateTotals()"
              />
              <div class="error" id="price_error">
                Unit price must be a positive number
              </div>
            </div>
          </div>

          <div class="form-group">
            <label>Tax Rate (%):</label>
            <select name="tax_rate" id="tax_rate" onchange="calculateTotals()">
              <option value="0">0% - No Tax</option>
              <option value="18">18% - Standard VAT</option>
              <option value="10">10% - Reduced Rate</option>
              <option value="5">5% - Special Rate</option>
            </select>
          </div>

          <button type="submit">Generate Invoice</button>
        </form>
      </div>
    </div>
  </body>
</html>
```

### b) JavaScript to Validate Form (2 Marks)

```javascript
function validateForm() {
  let isValid = true;

  // Get form values
  let clientName = document.getElementById("client_name").value.trim();
  let productName = document.getElementById("product_name").value.trim();
  let quantity = document.getElementById("quantity").value;
  let unitPrice = document.getElementById("unit_price").value;

  // Validate Client Name
  if (clientName === "") {
    document.getElementById("client_name_error").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("client_name_error").style.display = "none";
  }

  // Validate Product Name
  if (productName === "") {
    document.getElementById("product_name_error").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("product_name_error").style.display = "none";
  }

  // Validate Quantity
  if (quantity === "" || parseFloat(quantity) <= 0) {
    document.getElementById("quantity_error").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("quantity_error").style.display = "none";
  }

  // Validate Unit Price
  if (unitPrice === "" || parseFloat(unitPrice) <= 0) {
    document.getElementById("price_error").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("price_error").style.display = "none";
  }

  return isValid;
}
```

### c) JavaScript to Calculate Subtotal, Tax, and Total (3 Marks)

```javascript
function calculateTotals() {
  // Get values
  let quantity = parseFloat(document.getElementById("quantity").value) || 0;
  let unitPrice = parseFloat(document.getElementById("unit_price").value) || 0;
  let taxRate = parseFloat(document.getElementById("tax_rate").value) || 0;

  // Calculate subtotal
  let subtotal = quantity * unitPrice;

  // Calculate tax amount
  let taxAmount = subtotal * (taxRate / 100);

  // Calculate total
  let total = subtotal + taxAmount;

  // Display or update calculation fields (add to form if needed)
  // You can add hidden fields or display these values

  // Example: Display in console for debugging
  console.log("Subtotal: " + subtotal);
  console.log("Tax Amount: " + taxAmount);
  console.log("Total: " + total);

  // Optionally create or update a summary section
  let summaryDiv = document.getElementById("calculation_summary");
  if (!summaryDiv) {
    summaryDiv = document.createElement("div");
    summaryDiv.id = "calculation_summary";
    summaryDiv.style.marginTop = "20px";
    summaryDiv.style.padding = "15px";
    summaryDiv.style.backgroundColor = "#f8f9fa";
    summaryDiv.style.borderRadius = "8px";
    document.querySelector(".form-section").appendChild(summaryDiv);
  }

  summaryDiv.innerHTML = `
        <h3 style="margin-bottom: 15px; color: #333;">Invoice Summary</h3>
        <p><strong>Subtotal:</strong> ${subtotal.toLocaleString()} RWF</p>
        <p><strong>Tax (${taxRate}%):</strong> ${taxAmount.toLocaleString()} RWF</p>
        <p><strong>Total Amount:</strong> ${total.toLocaleString()} RWF</p>
    `;
}
```

### d) PHP Code to Process and Display Invoice (2 Marks)

```php
<?php
// process_invoice.php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $client_name = $_POST['client_name'];
    $product_name = $_POST['product_name'];
    $quantity = $_POST['quantity'];
    $unit_price = $_POST['unit_price'];
    $tax_rate = $_POST['tax_rate'];

    // Calculate amounts
    $subtotal = $quantity * $unit_price;
    $tax_amount = $subtotal * ($tax_rate / 100);
    $total = $subtotal + $tax_amount;
    $invoice_no = 'INV-' . date('Ymd') . '-' . rand(1000, 9999);
    $date = date('F j, Y');
?>
<!DOCTYPE html>
<html>
<head>
    <title>Invoice - BERWA Shop</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            padding: 40px 20px;
        }
        .invoice {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .invoice-header {
            background: linear-gradient(135deg, #2c3e50, #3498db);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .invoice-header h1 {
            font-size: 32px;
            margin-bottom: 10px;
        }
        .invoice-body {
            padding: 30px;
        }
        .invoice-info {
            display: flex;
            justify-content: space-between;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #eee;
        }
        .invoice-details {
            margin-bottom: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        .totals {
            text-align: right;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 8px;
        }
        .totals p {
            margin: 10px 0;
        }
        .button-group {
            text-align: center;
            padding: 20px;
            border-top: 1px solid #eee;
        }
        button {
            padding: 10px 20px;
            margin: 0 10px;
            background: #3498db;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background: #2980b9;
        }
        @media print {
            .button-group {
                display: none;
            }
            body {
                background: white;
                padding: 0;
            }
        }
    </style>
</head>
<body>
    <div class="invoice" id="invoice">
        <div class="invoice-header">
            <h1>🏪 BERWA Shop</h1>
            <p>Official Tax Invoice</p>
        </div>

        <div class="invoice-body">
            <div class="invoice-info">
                <div>
                    <strong>Invoice No:</strong> <?php echo $invoice_no; ?><br>
                    <strong>Date:</strong> <?php echo $date; ?>
                </div>
                <div>
                    <strong>Client:</strong> <?php echo htmlspecialchars($client_name); ?>
                </div>
            </div>

            <div class="invoice-details">
                <table>
                    <thead>
                        <tr><th>Description</th><th>Quantity</th><th>Unit Price (RWF)</th><th>Total (RWF)</th></tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><?php echo htmlspecialchars($product_name); ?></td>
                            <td><?php echo $quantity; ?></td>
                            <td><?php echo number_format($unit_price, 2); ?></td>
                            <td><?php echo number_format($subtotal, 2); ?></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="totals">
                <p><strong>Subtotal:</strong> <?php echo number_format($subtotal, 2); ?> RWF</p>
                <p><strong>Tax (<?php echo $tax_rate; ?>%):</strong> <?php echo number_format($tax_amount, 2); ?> RWF</p>
                <p style="font-size: 20px;"><strong>Total:</strong> <?php echo number_format($total, 2); ?> RWF</p>
            </div>
        </div>
    </div>

    <div class="button-group">
        <button onclick="window.print()">🖨️ Print Invoice</button>
        <button onclick="downloadInvoice()">📥 Download Invoice</button>
    </div>

    <script>
        function downloadInvoice() {
            var invoiceContent = document.getElementById('invoice').innerHTML;
            var originalContent = document.body.innerHTML;
            document.body.innerHTML = invoiceContent;
            window.print();
            document.body.innerHTML = originalContent;
            location.reload();
        }
    </script>
</body>
</html>
<?php } ?>
```

### e) JavaScript for Print/Download Button (1 Mark)

```javascript
// Add print functionality (already included in the PHP code above)

// Alternative: Simple print function
function printInvoice() {
  window.print();
}

// Download as PDF (using browser's print to PDF)
function downloadInvoice() {
  window.print(); // Users can select "Save as PDF" from print dialog
}
```

---

# SECTION B

## QUESTION 4 (Compulsory) - Product Inventory System

### a) PHP Code to Insert a Product (2 Marks)

```php
<?php
// insert_product.php

$host = "localhost";
$username = "root";
$password = "";
$database = "stock_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $product_name = $_POST['product_name'];
    $price = $_POST['price'];
    $quantity = $_POST['quantity'];

    $query = "INSERT INTO products (product_name, price, quantity)
              VALUES ('$product_name', '$price', '$quantity')";

    if (mysqli_query($connection, $query)) {
        echo "<script>
                alert('Product added successfully!');
                window.location.href = 'view_products.php';
              </script>";
    } else {
        echo "Error: " . mysqli_error($connection);
    }
}

mysqli_close($connection);
?>
```

### b) PHP Code to Display All Products and Search (4 Marks)

```php
<?php
// view_products.php

$host = "localhost";
$username = "root";
$password = "";
$database = "stock_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

$search = "";
if (isset($_GET['search'])) {
    $search = $_GET['search'];
    $query = "SELECT * FROM products WHERE product_name LIKE '%$search%'";
} else {
    $query = "SELECT * FROM products";
}

$result = mysqli_query($connection, $query);
?>

<!DOCTYPE html>
<html>
<head>
    <title>Product Inventory</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #4CAF50; color: white; }
        tr:hover { background-color: #f5f5f5; }
        .search-box { margin-bottom: 20px; }
        .search-box input { padding: 10px; width: 300px; border: 1px solid #ccc; border-radius: 4px; }
        .search-box button { padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .add-button { display: inline-block; margin-bottom: 20px; padding: 10px 20px; background-color: #008CBA; color: white; text-decoration: none; border-radius: 4px; }
        .edit { color: blue; text-decoration: none; margin-right: 10px; }
        .delete { color: red; text-decoration: none; }
    </style>
</head>
<body>
    <h2>Product Inventory Management</h2>

    <a href="add_product.html" class="add-button">+ Add New Product</a>

    <div class="search-box">
        <form method="GET" action="">
            <input type="text" name="search" placeholder="Search by product name..." value="<?php echo $search; ?>">
            <button type="submit">Search</button>
            <?php if ($search): ?>
                <a href="view_products.php">Clear</a>
            <?php endif; ?>
        </form>
    </div>

    <table>
        <thead>
            <tr><th>Product ID</th><th>Product Name</th><th>Price (RWF)</th><th>Quantity</th><th>Stock Value</th><th>Actions</th></tr>
        </thead>
        <tbody>
            <?php
            if (mysqli_num_rows($result) > 0) {
                while ($row = mysqli_fetch_assoc($result)) {
                    $stock_value = $row['price'] * $row['quantity'];
                    $low_stock = ($row['quantity'] < 10) ? 'style="color: red; font-weight: bold;"' : '';
                    echo "<tr>";
                    echo "<td>" . $row['product_id'] . "</td>";
                    echo "<td>" . $row['product_name'] . "</td>";
                    echo "<td>" . number_format($row['price'], 2) . "</td>";
                    echo "<td $low_stock>" . $row['quantity'] . "</td>";
                    echo "<td>" . number_format($stock_value, 2) . "</td>";
                    echo "<td>
                            <a href='edit_product.php?id=" . $row['product_id'] . "' class='edit'>Edit</a>
                            <a href='delete_product.php?id=" . $row['product_id'] . "' class='delete' onclick='return confirm(\"Are you sure?\")'>Delete</a>
                          </td>";
                    echo "</tr>";
                }
            } else {
                echo "<tr><td colspan='6' style='text-align: center;'>No products found</td></tr>";
            }
            ?>
        </tbody>
    </table>

    <?php mysqli_close($connection); ?>
</body>
</html>
```

### c) PHP Code to Update and Delete a Product (4 Marks)

```php
<?php
// edit_product.php

$host = "localhost";
$username = "root";
$password = "";
$database = "stock_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

// Handle Update
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['update'])) {
    $product_id = $_POST['product_id'];
    $product_name = $_POST['product_name'];
    $price = $_POST['price'];
    $quantity = $_POST['quantity'];

    $query = "UPDATE products
              SET product_name = '$product_name',
                  price = '$price',
                  quantity = '$quantity'
              WHERE product_id = '$product_id'";

    if (mysqli_query($connection, $query)) {
        echo "<script>
                alert('Product updated successfully!');
                window.location.href = 'view_products.php';
              </script>";
    } else {
        echo "Error: " . mysqli_error($connection);
    }
}

// Get product data for editing
$product_id = $_GET['id'];
$query = "SELECT * FROM products WHERE product_id = '$product_id'";
$result = mysqli_query($connection, $query);
$product = mysqli_fetch_assoc($result);
?>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Product</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        .form-container { width: 400px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input[type="text"], input[type="number"] { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc; border-radius: 4px; }
        button { margin-top: 20px; padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>Edit Product</h2>
        <form method="POST" action="">
            <input type="hidden" name="product_id" value="<?php echo $product['product_id']; ?>">

            <label>Product Name:</label>
            <input type="text" name="product_name" value="<?php echo $product['product_name']; ?>" required>

            <label>Price (RWF):</label>
            <input type="number" name="price" value="<?php echo $product['price']; ?>" step="0.01" required>

            <label>Quantity:</label>
            <input type="number" name="quantity" value="<?php echo $product['quantity']; ?>" required>

            <button type="submit" name="update">Update Product</button>
            <a href="view_products.php" style="margin-left: 10px;">Cancel</a>
        </form>
    </div>
</body>
</html>

<?php
// delete_product.php
// Handle deletion (separate file)

if (isset($_GET['id'])) {
    $product_id = $_GET['id'];
    $query = "DELETE FROM products WHERE product_id = '$product_id'";

    if (mysqli_query($connection, $query)) {
        echo "<script>
                alert('Product deleted successfully!');
                window.location.href = 'view_products.php';
              </script>";
    } else {
        echo "Error: " . mysqli_error($connection);
    }
}

mysqli_close($connection);
?>
```

---

## QUESTION 5 - Marks Management System

### a) HTML Form for Student Details and Marks (3 Marks)

```html
<!DOCTYPE html>
<html>
  <head>
    <title>Marks Management System</title>
    <style>
      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }
      body {
        font-family: "Segoe UI", Arial, sans-serif;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        min-height: 100vh;
        padding: 40px 20px;
      }
      .container {
        max-width: 600px;
        margin: 0 auto;
        background: white;
        border-radius: 15px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        overflow: hidden;
      }
      .header {
        background: linear-gradient(135deg, #2c3e50, #3498db);
        color: white;
        padding: 30px;
        text-align: center;
      }
      .form-section {
        padding: 30px;
      }
      .form-group {
        margin-bottom: 20px;
      }
      label {
        display: block;
        font-weight: 600;
        margin-bottom: 8px;
        color: #333;
      }
      input[type="text"],
      input[type="number"],
      select {
        width: 100%;
        padding: 12px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 14px;
      }
      input:focus {
        outline: none;
        border-color: #3498db;
      }
      .row {
        display: flex;
        gap: 20px;
      }
      .row .form-group {
        flex: 1;
      }
      button {
        width: 100%;
        padding: 14px;
        background: linear-gradient(135deg, #2c3e50, #3498db);
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 16px;
        font-weight: bold;
        cursor: pointer;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="header">
        <h1>📚 Marks Management System</h1>
        <p>Academic Performance Tracker</p>
      </div>

      <div class="form-section">
        <form action="save_marks.php" method="POST">
          <div class="form-group">
            <label>Select Module:</label>
            <select name="module_id" required>
              <option value="">-- Select Module --</option>
              <option value="1">CS101 - Programming Fundamentals</option>
              <option value="2">CS201 - Database Systems</option>
              <option value="3">CS301 - Web Development</option>
            </select>
          </div>

          <div class="form-group">
            <label>Student Name:</label>
            <input
              type="text"
              name="student_name"
              placeholder="Enter full name"
              required
            />
          </div>

          <div class="form-group">
            <label>Registration Number:</label>
            <input
              type="text"
              name="reg_no"
              placeholder="e.g., 2305000921"
              required
            />
          </div>

          <div class="row">
            <div class="form-group">
              <label>CAT Marks (60%):</label>
              <input
                type="number"
                name="cat_60"
                placeholder="0-60"
                min="0"
                max="60"
                required
              />
            </div>
            <div class="form-group">
              <label>Exam Marks (40%):</label>
              <input
                type="number"
                name="exam_40"
                placeholder="0-40"
                min="0"
                max="40"
                required
              />
            </div>
          </div>

          <button type="submit">Save Results</button>
        </form>
      </div>
    </div>
  </body>
</html>
```

### b) PHP Code to Calculate Total and Assign Grade (3 Marks)

```php
<?php
// Function to calculate total and assign grade

function calculateTotalAndGrade($cat_60, $exam_40) {
    $total = $cat_60 + $exam_40;

    // Assign grade based on total marks
    if ($total >= 80) {
        $grade = 'A';
        $remark = 'Excellent';
    } elseif ($total >= 70) {
        $grade = 'B';
        $remark = 'Very Good';
    } elseif ($total >= 60) {
        $grade = 'C';
        $remark = 'Good';
    } elseif ($total >= 50) {
        $grade = 'D';
        $remark = 'Pass';
    } else {
        $grade = 'F';
        $remark = 'Fail';
    }

    return [
        'total' => $total,
        'grade' => $grade,
        'remark' => $remark
    ];
}

// Usage example
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $cat_60 = $_POST['cat_60'];
    $exam_40 = $_POST['exam_40'];

    $result = calculateTotalAndGrade($cat_60, $exam_40);

    echo "Total Marks: " . $result['total'] . "<br>";
    echo "Grade: " . $result['grade'] . "<br>";
    echo "Remark: " . $result['remark'] . "<br>";
}
?>
```

### c) PHP Code to Insert and Display Student Results (4 Marks)

```php
<?php
// save_marks.php

$host = "localhost";
$username = "root";
$password = "";
$database = "academic_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

// Function to calculate total and grade
function calculateTotalAndGrade($cat_60, $exam_40) {
    $total = $cat_60 + $exam_40;

    if ($total >= 80) {
        $grade = 'A';
    } elseif ($total >= 70) {
        $grade = 'B';
    } elseif ($total >= 60) {
        $grade = 'C';
    } elseif ($total >= 50) {
        $grade = 'D';
    } else {
        $grade = 'F';
    }

    return ['total' => $total, 'grade' => $grade];
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $student_name = $_POST['student_name'];
    $reg_no = $_POST['reg_no'];
    $cat_60 = $_POST['cat_60'];
    $exam_40 = $_POST['exam_40'];
    $module_id = $_POST['module_id'];

    $result = calculateTotalAndGrade($cat_60, $exam_40);
    $total = $result['total'];
    $grade = $result['grade'];

    // Insert into database
    $query = "INSERT INTO marks (student_name, reg_no, cat_60, exam_40, total, grade, module_id)
              VALUES ('$student_name', '$reg_no', '$cat_60', '$exam_40', '$total', '$grade', '$module_id')";

    if (mysqli_query($connection, $query)) {
        echo "<script>alert('Results saved successfully!');</script>";
    } else {
        echo "Error: " . mysqli_error($connection);
    }
}

// Display all results
$query = "SELECT marks.*, modules.module_name
          FROM marks
          JOIN modules ON marks.module_id = modules.module_id
          ORDER BY marks.marks_id DESC";

$result = mysqli_query($connection, $query);
?>

<!DOCTYPE html>
<html>
<head>
    <title>Student Results</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #4CAF50; color: white; }
        .grade-A { background-color: #d4edda; }
        .grade-F { background-color: #f8d7da; }
    </style>
</head>
<body>
    <h2>Student Results Report</h2>

    <table>
        <thead>
            <tr><th>ID</th><th>Student Name</th><th>Reg No</th><th>Module</th><th>CAT(60)</th><th>Exam(40)</th><th>Total</th><th>Grade</th></tr>
        </thead>
        <tbody>
            <?php
            if (mysqli_num_rows($result) > 0) {
                while ($row = mysqli_fetch_assoc($result)) {
                    $grade_class = ($row['grade'] == 'A') ? 'grade-A' : (($row['grade'] == 'F') ? 'grade-F' : '');
                    echo "<tr class='$grade_class'>";
                    echo "<td>" . $row['marks_id'] . "</td>";
                    echo "<td>" . $row['student_name'] . "</td>";
                    echo "<td>" . $row['reg_no'] . "</td>";
                    echo "<td>" . $row['module_name'] . "</td>";
                    echo "<td>" . $row['cat_60'] . "</td>";
                    echo "<td>" . $row['exam_40'] . "</td>";
                    echo "<td>" . $row['total'] . "</td>";
                    echo "<td>" . $row['grade'] . "</td>";
                    echo "</tr>";
                }
            } else {
                echo "<tr><td colspan='8' style='text-align: center;'>No records found</td></tr>";
            }
            ?>
        </tbody>
    </table>

    <br>
    <a href="add_marks.html">Add More Results</a>
</body>
</html>

<?php mysqli_close($connection); ?>
```

---

## QUESTION 6 - User Authentication System

### a) HTML Form with Appropriate Input Types (2 Marks)

```html
<!DOCTYPE html>
<html>
  <head>
    <title>User Registration</title>
    <style>
      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }
      body {
        font-family: "Segoe UI", Arial, sans-serif;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 20px;
      }
      .container {
        max-width: 500px;
        width: 100%;
        background: white;
        border-radius: 15px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        overflow: hidden;
      }
      .header {
        background: linear-gradient(135deg, #2c3e50, #3498db);
        color: white;
        padding: 30px;
        text-align: center;
      }
      .form-section {
        padding: 30px;
      }
      .form-group {
        margin-bottom: 20px;
      }
      label {
        display: block;
        font-weight: 600;
        margin-bottom: 8px;
        color: #333;
      }
      input[type="text"],
      input[type="email"],
      input[type="password"],
      select {
        width: 100%;
        padding: 12px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 14px;
      }
      .radio-group {
        display: flex;
        gap: 20px;
        margin-top: 8px;
      }
      .radio-group label {
        display: inline;
        font-weight: normal;
        margin-left: 5px;
      }
      button {
        width: 100%;
        padding: 14px;
        background: linear-gradient(135deg, #2c3e50, #3498db);
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 16px;
        font-weight: bold;
        cursor: pointer;
      }
      .login-link {
        text-align: center;
        margin-top: 20px;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="header">
        <h1>🔐 User Registration</h1>
        <p>Create your account</p>
      </div>

      <div class="form-section">
        <form action="register.php" method="POST">
          <div class="form-group">
            <label>Full Name:</label>
            <input
              type="text"
              name="full_name"
              placeholder="Enter your full name"
              required
            />
          </div>

          <div class="form-group">
            <label>Email Address:</label>
            <input
              type="email"
              name="email"
              placeholder="your@email.com"
              required
            />
          </div>

          <div class="form-group">
            <label>Country:</label>
            <select name="country" required>
              <option value="">-- Select Country --</option>
              <option value="Rwanda">Rwanda</option>
              <option value="Kenya">Kenya</option>
              <option value="Uganda">Uganda</option>
              <option value="Tanzania">Tanzania</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div class="form-group">
            <label>Gender:</label>
            <div class="radio-group">
              <input type="radio" name="gender" value="Male" required />
              <label>Male</label>
              <input type="radio" name="gender" value="Female" />
              <label>Female</label>
              <input type="radio" name="gender" value="Other" />
              <label>Other</label>
            </div>
          </div>

          <div class="form-group">
            <label>Password:</label>
            <input
              type="password"
              name="password"
              placeholder="Create a strong password"
              required
            />
          </div>

          <button type="submit">Register</button>
        </form>

        <div class="login-link">
          Already have an account? <a href="login.html">Login here</a>
        </div>
      </div>
    </div>
  </body>
</html>
```

### b) PHP Code to Encrypt Password (2 Marks)

```php
<?php
// Password encryption using password_hash()

$plain_password = $_POST['password'];

// Method 1: Using password_hash() (recommended)
$encrypted_password = password_hash($plain_password, PASSWORD_DEFAULT);

// Method 2: Using md5() (less secure, but sometimes used)
$md5_password = md5($plain_password);

// Method 3: Using sha256
$sha256_password = hash('sha256', $plain_password);

// For the exam, you can use any. password_hash() is modern and secure.
echo "Original: " . $plain_password . "<br>";
echo "Encrypted: " . $encrypted_password . "<br>";
?>
```

### c) PHP Code to Insert User Data (2 Marks)

```php
<?php
// register.php

$host = "localhost";
$username = "root";
$password = "";
$database = "user_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $full_name = $_POST['full_name'];
    $email = $_POST['email'];
    $country = $_POST['country'];
    $gender = $_POST['gender'];
    $plain_password = $_POST['password'];

    // Encrypt the password
    $encrypted_password = password_hash($plain_password, PASSWORD_DEFAULT);

    // Insert into database
    $query = "INSERT INTO users (full_name, email, country, gender, password)
              VALUES ('$full_name', '$email', '$country', '$gender', '$encrypted_password')";

    if (mysqli_query($connection, $query)) {
        echo "<script>
                alert('Registration successful! Please login.');
                window.location.href = 'login.html';
              </script>";
    } else {
        echo "Error: " . mysqli_error($connection);
    }
}

mysqli_close($connection);
?>
```

### d) PHP Code for User Login (2 Marks)

```php
<?php
// login.php

session_start();

$host = "localhost";
$username = "root";
$password = "";
$database = "user_db";

$connection = mysqli_connect($host, $username, $password, $database);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $plain_password = $_POST['password'];

    // Query to get user by email
    $query = "SELECT * FROM users WHERE email = '$email'";
    $result = mysqli_query($connection, $query);

    if (mysqli_num_rows($result) == 1) {
        $user = mysqli_fetch_assoc($result);

        // Verify password
        if (password_verify($plain_password, $user['password'])) {
            $_SESSION['user_id'] = $user['user_id'];
            $_SESSION['full_name'] = $user['full_name'];
            $_SESSION['email'] = $user['email'];

            echo "<script>
                    alert('Login successful! Welcome " . $user['full_name'] . "');
                    window.location.href = 'dashboard.php';
                  </script>";
        } else {
            echo "<script>alert('Invalid password!'); window.location.href = 'login.html';</script>";
        }
    } else {
        echo "<script>alert('Email not found!'); window.location.href = 'login.html';</script>";
    }
}

mysqli_close($connection);
?>
```

### e) PHP Code to Create and Destroy Session (Login & Logout) (2 Marks)

```php
<?php
// dashboard.php - Create session (already started in login)

session_start();

// Check if user is logged in
if (!isset($_SESSION['user_id'])) {
    header("Location: login.html");
    exit();
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; text-align: center; }
        .dashboard { max-width: 500px; margin: 0 auto; padding: 30px; border: 1px solid #ccc; border-radius: 10px; }
        .logout-btn { padding: 10px 20px; background-color: #e74c3c; color: white; border: none; border-radius: 5px; cursor: pointer; }
    </style>
</head>
<body>
    <div class="dashboard">
        <h2>Welcome, <?php echo $_SESSION['full_name']; ?>!</h2>
        <p>Email: <?php echo $_SESSION['email']; ?></p>
        <p>You have successfully logged in.</p>
        <a href="logout.php"><button class="logout-btn">Logout</button></a>
    </div>
</body>
</html>

<?php
// logout.php - Destroy session

session_start();

// Destroy all session data
session_unset();     // Remove all session variables
session_destroy();   // Destroy the session

// Redirect to login page
header("Location: login.html");
exit();
?>
```

<br/><hr/><br/>

  <details>
  <summary>Database and Table Creation</summary>

# Database and Table Creation Scripts

You're absolutely right! Here are all the **database and table creation scripts** for each question. Run these in phpMyAdmin or MySQL command line before testing your PHP code.

---

## QUESTION 1 - Student Registration System

### Database: `student_db`

### Tables: `students` and `programmes`

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- Create programmes table
CREATE TABLE programmes (
    programme_id INT AUTO_INCREMENT PRIMARY KEY,
    programme_name VARCHAR(100) NOT NULL
);

-- Create students table
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    reg_no VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    programme_id INT,
    FOREIGN KEY (programme_id) REFERENCES programmes(programme_id)
);

-- Insert sample data into programmes
INSERT INTO programmes (programme_name) VALUES
('Bachelor of Science in Computer Science'),
('Bachelor of Science in Information Technology'),
('Bachelor of Business Administration'),
('Bachelor of Accounting and Finance');

-- Insert sample data into students
INSERT INTO students (full_name, reg_no, email, programme_id) VALUES
('John Doe', '2305000001', 'john.doe@uok.ac.rw', 1),
('Jane Smith', '2305000002', 'jane.smith@uok.ac.rw', 1),
('Alice Munezero', '2305000003', 'alice.munezero@uok.ac.rw', 2),
('Bob Habimana', '2305000004', 'bob.habimana@uok.ac.rw', 3);
```

---

## QUESTION 2 - Employee Records Management

### Database: `employee_db`

### Table: `employees`

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS employee_db;
USE employee_db;

-- Create employees table
CREATE TABLE employees (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    salary DECIMAL(10, 2) NOT NULL,
    phone VARCHAR(20) NOT NULL
);

-- Insert sample data
INSERT INTO employees (emp_name, department, salary, phone) VALUES
('Jean Paul Niyonshuti', 'IT', 850000.00, '+250788123456'),
('Marie Claire Uwase', 'HR', 650000.00, '+250788123457'),
('David Ndayisaba', 'Finance', 750000.00, '+250788123458'),
('Grace Ingabire', 'Marketing', 600000.00, '+250788123459'),
('Eric Mugisha', 'IT', 900000.00, '+250788123460');
```

---

## QUESTION 3 - Invoice Generation System

### No Database Required

This system uses PHP session or form submission only. No database needed.

---

## QUESTION 4 - Product Inventory System

### Database: `stock_db`

### Table: `products`

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS stock_db;
USE stock_db;

-- Create products table
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0
);

-- Insert sample data
INSERT INTO products (product_name, price, quantity) VALUES
('Laptop Dell Inspiron', 850000.00, 15),
('HP LaserJet Printer', 350000.00, 8),
('Wireless Mouse', 15000.00, 50),
('Keyboard USB', 12000.00, 35),
('Monitor 24 inch', 180000.00, 12),
('External Hard Drive 1TB', 75000.00, 20);
```

---

## QUESTION 5 - Marks Management System

### Database: `academic_db`

### Tables: `marks` and `modules`

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS academic_db;
USE academic_db;

-- Create modules table
CREATE TABLE modules (
    module_id INT AUTO_INCREMENT PRIMARY KEY,
    module_name VARCHAR(100) NOT NULL,
    module_code VARCHAR(20) NOT NULL UNIQUE
);

-- Create marks table
CREATE TABLE marks (
    marks_id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    reg_no VARCHAR(50) NOT NULL,
    cat_60 DECIMAL(5, 2) CHECK (cat_60 >= 0 AND cat_60 <= 60),
    exam_40 DECIMAL(5, 2) CHECK (exam_40 >= 0 AND exam_40 <= 40),
    total DECIMAL(5, 2),
    grade CHAR(1),
    module_id INT,
    FOREIGN KEY (module_id) REFERENCES modules(module_id)
);

-- Insert sample modules
INSERT INTO modules (module_name, module_code) VALUES
('Programming Fundamentals', 'CS101'),
('Database Systems', 'CS201'),
('Web Development', 'CS301'),
('Data Structures and Algorithms', 'CS202'),
('Software Engineering', 'CS401');

-- Insert sample marks (optional)
INSERT INTO marks (student_name, reg_no, cat_60, exam_40, total, grade, module_id) VALUES
('Elysee NIYIBIZI', '2305000921', 55, 38, 93, 'A', 1),
('Alice Umutoni', '2305000001', 48, 35, 83, 'A', 1),
('John Mugisha', '2305000002', 42, 30, 72, 'B', 1),
('Sarah Uwase', '2305000003', 35, 28, 63, 'C', 2);
```

---

## QUESTION 6 - User Authentication System

### Database: `user_db`

### Table: `users`

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS user_db;
USE user_db;

-- Create users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    country VARCHAR(50) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    password VARCHAR(255) NOT NULL,  -- Store hashed password
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample user (password: password123)
-- Note: In real application, password should be hashed using password_hash()
INSERT INTO users (full_name, email, country, gender, password) VALUES
('Admin User', 'admin@uok.ac.rw', 'Rwanda', 'Male', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
('Test Student', 'student@uok.ac.rw', 'Rwanda', 'Female', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi');
```

---

## 📋 Quick Reference: All Databases Summary

| Question | Database Name | Tables                   | Purpose                     |
| -------- | ------------- | ------------------------ | --------------------------- |
| Q1       | `student_db`  | `students`, `programmes` | Student registration system |
| Q2       | `employee_db` | `employees`              | Employee records management |
| Q3       | None          | N/A                      | Invoice system (no DB)      |
| Q4       | `stock_db`    | `products`               | Product inventory system    |
| Q5       | `academic_db` | `marks`, `modules`       | Marks management system     |
| Q6       | `user_db`     | `users`                  | User authentication system  |

---

## 🔧 How to Run These Scripts

### Method 1: phpMyAdmin (Recommended)

1. Open phpMyAdmin (http://localhost/phpmyadmin)
2. Click on **SQL** tab
3. Copy and paste the SQL script for the desired question
4. Click **Go** to execute

### Method 2: MySQL Command Line

```bash
mysql -u root -p

# For each database, run:
source /path/to/script.sql;
```

### Method 3: Create a single setup file (setup.php)

```php
<?php
// setup.php - Run this once to create all databases and tables

$host = "localhost";
$username = "root";
$password = "";

$connection = mysqli_connect($host, $username, $password);

if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

// Array of SQL scripts
$sql_scripts = [
    // Question 1
    "CREATE DATABASE IF NOT EXISTS student_db;
    USE student_db;
    CREATE TABLE IF NOT EXISTS programmes (
        programme_id INT AUTO_INCREMENT PRIMARY KEY,
        programme_name VARCHAR(100) NOT NULL
    );
    CREATE TABLE IF NOT EXISTS students (
        student_id INT AUTO_INCREMENT PRIMARY KEY,
        full_name VARCHAR(100) NOT NULL,
        reg_no VARCHAR(50) UNIQUE NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        programme_id INT,
        FOREIGN KEY (programme_id) REFERENCES programmes(programme_id)
    );",

    // Question 2
    "CREATE DATABASE IF NOT EXISTS employee_db;
    USE employee_db;
    CREATE TABLE IF NOT EXISTS employees (
        emp_id INT AUTO_INCREMENT PRIMARY KEY,
        emp_name VARCHAR(100) NOT NULL,
        department VARCHAR(50) NOT NULL,
        salary DECIMAL(10, 2) NOT NULL,
        phone VARCHAR(20) NOT NULL
    );",

    // Question 4
    "CREATE DATABASE IF NOT EXISTS stock_db;
    USE stock_db;
    CREATE TABLE IF NOT EXISTS products (
        product_id INT AUTO_INCREMENT PRIMARY KEY,
        product_name VARCHAR(100) NOT NULL,
        price DECIMAL(10, 2) NOT NULL,
        quantity INT NOT NULL DEFAULT 0
    );",

    // Question 5
    "CREATE DATABASE IF NOT EXISTS academic_db;
    USE academic_db;
    CREATE TABLE IF NOT EXISTS modules (
        module_id INT AUTO_INCREMENT PRIMARY KEY,
        module_name VARCHAR(100) NOT NULL,
        module_code VARCHAR(20) NOT NULL UNIQUE
    );
    CREATE TABLE IF NOT EXISTS marks (
        marks_id INT AUTO_INCREMENT PRIMARY KEY,
        student_name VARCHAR(100) NOT NULL,
        reg_no VARCHAR(50) NOT NULL,
        cat_60 DECIMAL(5, 2),
        exam_40 DECIMAL(5, 2),
        total DECIMAL(5, 2),
        grade CHAR(1),
        module_id INT,
        FOREIGN KEY (module_id) REFERENCES modules(module_id)
    );",

    // Question 6
    "CREATE DATABASE IF NOT EXISTS user_db;
    USE user_db;
    CREATE TABLE IF NOT EXISTS users (
        user_id INT AUTO_INCREMENT PRIMARY KEY,
        full_name VARCHAR(100) NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        country VARCHAR(50) NOT NULL,
        gender ENUM('Male', 'Female', 'Other') NOT NULL,
        password VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );"
];

foreach ($sql_scripts as $sql) {
    if (mysqli_multi_query($connection, $sql)) {
        echo "✅ Script executed successfully!<br>";
        while (mysqli_next_result($connection)) {;}
    } else {
        echo "❌ Error: " . mysqli_error($connection) . "<br>";
    }
}

mysqli_close($connection);
echo "<br>🎉 All databases and tables created successfully!";
?>
```

  </details>
    
</details>
