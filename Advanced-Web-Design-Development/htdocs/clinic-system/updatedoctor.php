<?php
include("connection.php");

// Super Global Variable ($_GET)
$id = $_GET['id'];

$select = "SELECT * FROM doctor WHERE DOCTORID = '$id'";
$result = mysqli_query($connection, $select);

if ($result) {
    echo "<script>
                alert('Selected successfully!');
              </script>";
} else {
    // echo "<script>alert('Error deleting record!');</script>";
    die("Error selecting record: " . mysqli_error($connection));
}

// $doctor = mysqli_fetch_assoc($result);
// $row = mysqli_fetch_assoc($result);
$row = mysqli_fetch_array($result);
$docId = $row['DOCTORID'];
$names = $row['NAMES'];
$address = $row['ADDRESS'];
$phone = $row['PHONE'];
$speciality = $row['SPECIALITY'];
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
        <h1>UPDATE DOCTOR</h1>

        <form method="POST" action="">
            <input type="hidden" name="docId" id="docId" placeholder="Enter names" required value="<?php echo $docId; ?>">

            <label for="names">Names</label>
            <input type="text" name="names" id="names" placeholder="Enter names" required value="<?php echo $names; ?>">

            <label for="address">Address</label>
            <textarea name="address" id="address" placeholder="Enter address" required><?php echo $address; ?></textarea>

            <label for="phone">Phone Number</label>
            <input type="text" name="phone" id="phone" placeholder="Enter phone number" required value="<?php echo $phone; ?>">

            <label for="speciality">Speciality</label>
            <input type="text" name="speciality" id="speciality" placeholder="Enter speciality" required value="<?php echo $speciality; ?>">

            <button type="submit" name="update">Update Doctor</button>
        </form>
    </div>

    <?php
    if (isset($_POST['update'])) {
        $id = $_POST['docId'];
        $names = $_POST['names'];
        $address = $_POST['address'];
        $phone = $_POST['phone'];
        $speciality = $_POST['speciality'];

        $update = "UPDATE doctor SET NAMES = '$names', ADDRESS = '$address', PHONE = '$phone', SPECIALITY = '$speciality' WHERE DOCTORID = '$id'";
        $result = mysqli_query($connection, $update);

        if ($result) {
            echo "<script>
                        alert('Record updated successfully!');
                        window.location.href = 'viewdoctors.php';
                      </script>";
            exit();
            // header("Location: viewdoctors.php");
        } else {
            // echo "<script>alert('Error deleting record!');</script>";
            die("Error updating record: " . mysqli_error($connection));
        }
    }
    ?>

</body>

</html>