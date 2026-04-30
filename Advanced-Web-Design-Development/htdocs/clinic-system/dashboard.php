<?php
include("connection.php");

$select = "SELECT * FROM doctor";
$runSelect = mysqli_query($connection, $select);

if (!$runSelect) {
    die("Query failed: " . mysqli_error($connection));
}

$numberOfDoctors = mysqli_num_rows($runSelect);
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>

    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./styles/dashboard.css">
</head>

<body>

    <div class="header">
        <h3>CLINIC MANAGEMENT SYSTEM</h3>
    </div>

    <div class="dashcontainer">

        <div class="dashgroup">
            <h2>Total Doctors</h2>
            <p id="paragraph"><?php echo $numberOfDoctors; ?></p>
            <a href="viewdoctors.php">Check</a>
        </div>

        <div class="dashgroup">
            <h2>Add Doctors</h2>
            <p class="paragraph">Doctors Data Entry</p>
            <a href="adddoctor.php">Add</a>
        </div>

        <div class="dashgroup">
            <h2>Update Doctors</h2>
            <p>Click to Change</p>
            <a href="updatedoctor.php?id=1">Update</a>
        </div>

    </div>

    <div class="footer">
        <h3>@copyright == UNIVERSITY OF KIGALI <?php echo date("Y"); ?> </h3>
    </div>

</body>

</html>