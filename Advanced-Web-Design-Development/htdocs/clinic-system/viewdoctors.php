<?php
include("connection.php");

$select = "SELECT * FROM doctor";
$result = mysqli_query($connection, $select);
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Doctors</title>

    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/viewdoctors.css">

</head>

<body>

    <h1>Doctor Records</h1>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Names</th>
                    <th>Address</th>
                    <th>Phone</th>
                    <th>Speciality</th>
                    <th>Edit</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>

                <?php
                if ($result && mysqli_num_rows($result) > 0) {
                    while ($row = mysqli_fetch_assoc($result)) {
                        $id = $row['DOCTORID'];
                        $names = $row['NAMES'];
                        $address = $row['ADDRESS'];
                        $phone = $row['PHONE'];
                        $speciality = $row['SPECIALITY'];

                        echo "<tr>
                                <td>" . $id . "</td>
                                <td>" . $names . "</td>
                                <td>" . $address . "</td>
                                <td>" . $phone . "</td>
                                <td>" . $speciality . "</td>
                                <td>
                                    <a class='btn edit-btn' href='updatedoctor.php?id=" . $id . "'>Edit</a>
                                </td>
                                <td>
                                    <a class='btn delete-btn' href='deletedoctor.php?id=" . $id . "' 
                                    onclick=\"return confirm('Are you sure you want to delete this record?');\">
                                    Delete
                                    </a>
                                </td>
                            </tr>";
                    }
                } else {
                    echo "<tr><td colspan='7' class='no-data'>No records found</td></tr>";
                }
                ?>

            </tbody>
        </table>
    </div>

</body>

</html>