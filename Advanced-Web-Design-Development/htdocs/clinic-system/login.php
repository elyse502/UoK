<?php
include("connection.php");
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LOGIN USER</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/login.css">
</head>

<body>
    <div class="container">
        <h1>LOGIN USER</h1>

        <form method="POST" action="login.php" autocomplete="off">
            <label for="username">USER NAME:</label>
            <input type="text" name="username" id="username" placeholder="Enter username" autocomplete="new-userame" required>

            <label for="password">PASSWORD:</label>
            <input type="password" name="password" id="password" placeholder="Enter password" required>

            <button type="submit" id="btn" name="login">SIGN IN</button>
        </form>
    </div>

    <?php
    if (isset($_POST['login'])) {
        $username = $_POST['username'];
        $password = $_POST['password'];

        $selectUser = "SELECT * FROM users WHERE USERNAME='$username' AND PASSWORD='$password'";
        $runSelectUser = mysqli_query($connection, $selectUser);

        if (mysqli_num_rows($runSelectUser) == 1) {
            echo "<script>alert('Login successful!');
                    // window.location.href = 'dashboard.php';
                    window.location.replace('dashboard.php');
                    </script>";
            // header("Location: dashboard.php");
        } else {
            echo "<script>alert('Invalid username or password!');</script>";
        }
    }
    ?>

</body>

</html>