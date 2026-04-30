<?php
include("connection.php");

// Super Global Variable ($_GET)
$id = $_GET['id'];
$delete = "DELETE FROM doctor WHERE DOCTORID = '$id'";
$result = mysqli_query($connection, $delete);

if ($result) {
    echo "<script>
                alert('Record deleted successfully!');
                window.location.href = 'viewdoctors.php';
              </script>";
    // header("Location: viewdoctors.php");
} else {
    // echo "<script>alert('Error deleting record!');</script>";
    die("Error deleting record: " . mysqli_error($connection));
}
