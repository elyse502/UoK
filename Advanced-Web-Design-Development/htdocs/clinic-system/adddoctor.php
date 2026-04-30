<?php
include("connection.php");

// Super Global Variable ($_POST - to get attribute `name`)
if (isset($_POST['submit'])) {
    $names = $_POST['names'];
    $address = $_POST['address'];
    $phone = $_POST['phone'];
    $speciality = $_POST['speciality'];

    // Validate the input data (Validate data...)
    if (empty($names) || empty($address) || empty($phone) || empty($speciality)) {
        die("All fields are required!");
    }

    // Make a query to insert the doctor data into the database (Make a query...)
    $insert = "INSERT INTO doctor (names, address, phone, speciality)
                VALUES ('$names', '$address', '$phone', '$speciality')";

    // Execute the query (Run a query...)
    $result = mysqli_query($connection, $insert);

    // Check if the query was successful (Test a query...)
    if ($result) {
        echo "<script>alert('Doctor added successfully!');
                window.location.href = 'viewdoctors.php';</script>";
        // header("Location: viewdoctors.php");
    } else {
        // echo "<script>alert('Error adding doctor!');</script>";
        die("Doctor insertion failed: " . mysqli_error($connection));
    }
}
?>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctor Insertion</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/adddoctor.css">
</head>

<body>

    <div class="container">
        <h1>+ ADD DOCTOR</h1>

        <form method="POST" action="adddoctor.php">
            <label for="names">Names</label>
            <input type="text" name="names" id="names" placeholder="Enter names" required>

            <label for="address">Address</label>
            <textarea name="address" id="address" placeholder="Enter address" required></textarea>

            <label for="phone">Phone Number</label>
            <input type="text" name="phone" id="phone" placeholder="Enter phone number">

            <label for="speciality">Speciality</label>
            <input type="text" name="speciality" id="speciality" placeholder="Enter speciality" required>

            <button type="submit" name="submit">Save Doctor</button>
        </form>
    </div>

</body>

</html>