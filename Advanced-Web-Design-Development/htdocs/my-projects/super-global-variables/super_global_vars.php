<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Styled Form</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/super_global.css">
</head>

<body>
    <form action="super_global_vars.php" method="POST">
        <div class="formcontainer">
            <div class="formgroup">
                <label for="age">ENTER THE AGE:</label>
                <input type="number" name="age">
            </div>

            <div class="formgroup">
                <input type="submit" name="receive" value="Click To Receive">
            </div>
        </div>

        <?php
            if (isset($_POST['receive'])) {
                $age = $_POST['age'];
                if ($age >= 18) {
                    echo "You are an adult.";
                } else {
                    echo "You are a minor.";
                }
            }
        ?>
    </form>
</body>
</html>