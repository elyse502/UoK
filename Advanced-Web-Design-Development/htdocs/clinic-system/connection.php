<?php
// $connection = mysqli_connect("localhost", "root", "", "clinic_evening_db");

$host = "localhost";
$username = "root";
$password = "";
$database = "clinic_evening_db";

$connection = mysqli_connect($host, $username, $password, $database);

if ($connection) {
    // echo "Connected successfully!";
    // echo "<script>alert('Connected successful!');</script>";
    echo "<script>console.log('Connected successful!');</script>";
} else {
    // echo "Connection failed: " . mysqli_connect_error();
    die("Connection failed: " . mysqli_connect_error());
}
