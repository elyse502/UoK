<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Insert Page page</title>
</head>
<body>
    <center>
        <?php
            $link = mysqli_connect("localhost", "root", "", "staff");
            if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
            }
            $sql = "SELECT * FROM college";
            if ($res = mysqli_query($link, $sql)) {
            if (mysqli_num_rows($res) > 0) {
            echo "<table border=1>";
            echo "<tr>";
            echo "<th>Firstname</th>";
            echo "<th>Lastname</th>";
            echo "<th>Gender</th>";
            echo "<th>Address</th>";
            echo "<th>Email</th>";
            echo "</tr>";
            while ($row = mysqli_fetch_array($res)) {
            echo "<tr>";
            echo "<td>".$row['first_name']."</td>";
            echo "<td>".$row['last_name']."</td>";
            echo "<td>".$row['gender']."</td>";
            echo "<td>".$row['address']."</td>";
            echo "<td>".$row['email']."</td>";
            echo "</tr>";
            }
            echo "</table>";
            mysqli_free_result($res);
            }
            else {
            echo "No matching records are found.";
            }
            }
            else {
            echo "ERROR: Could not able to execute $sql. "
            .mysqli_error($link);
            }
            mysqli_close($link);
        ?>
    </center>
</body>
</html>


