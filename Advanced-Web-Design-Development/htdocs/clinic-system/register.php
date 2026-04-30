<?php
include("connection.php");
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>REGISTER USER</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/register.css">
</head>

<body>
    <div class="container">
        <h1>REGISTER USER</h1>

        <form method="POST" action="register.php">
            <label for="username">USER NAME:</label>
            <input type="text" name="username" id="username" placeholder="Enter username" required>

            <label for="password">PASSWORD:</label>
            <input type="password" name="password" id="password" placeholder="Enter password" required>

            <button type="submit" id="btn" name="register">REGISTER</button>
        </form>
    </div>

    <?php
    if (isset($_POST['register'])) {
        $username = $_POST['username'];
        $password = $_POST['password'];

        // Hash the password
        // $hashed_password = password_hash($password, PASSWORD_DEFAULT);

        // Insert user into database
        // $insert = "INSERT INTO users (username, password) VALUES ('$username', '$hashed_password')";
        $insert = "INSERT INTO users (username, password) VALUES ('$username', '$password')";
        $runInsert = mysqli_query($connection, $insert);

        if ($runInsert) {
            echo "<script>alert('User registered successfully!');
                    // window.location.replace('login.php');
                    window.location.href = 'login.php';</script>";
            // header("Location: login.php");
        } else {
            die("Error registering user: " . mysqli_error($connection));
        }
    }
    ?>
</body>

</html>