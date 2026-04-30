<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Name & Age</title>
    <link rel="stylesheet" href="styles/message.css">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/message.css">
</head>
<body>
    <form action="" method="POST">
        <div class="formcontainer">

            <div class="formgroup">
                <label>ENTER YOUR NAME:</label>
                <input type="text" name="username" required>
            </div>

            <div class="formgroup">
                <label>ENTER YOUR AGE:</label>
                <input type="number" name="age" required>
            </div>

            <div class="formgroup">
                <input type="submit" name="submit" value="Submit">
            </div>

            <div class="message">
                <?php
                    if (isset($_POST['submit'])) {
                        $name = $_POST['username'];
                        $age = $_POST['age'];

                        echo "Hello $name, you are $age years old!";
                    }
                ?>
            </div>

        </div>
    </form>
</body>
</html>