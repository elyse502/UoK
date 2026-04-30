<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Marks Grade Checker</title>
    <link rel="stylesheet" href="styles/grade.css">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles/super_global.css">
</head>

<body>
    <form action="" method="POST">
        <div class="formcontainer">

            <div class="formgroup">
                <label>ENTER YOUR MARKS (0–100):</label>
                <!-- <input type="number" name="marks" min="0" max="100" required> -->
                <input type="number" name="marks" required>
            </div>

            <div class="formgroup">
                <input type="submit" name="check" value="Check Grade">
            </div>

            <div class="message">
                <?php
                if (isset($_POST['check'])) {
                    $marks = $_POST['marks'];

                    if ($marks < 0 || $marks > 100) {
                        echo "Please enter valid marks (0–100).";
                    } elseif ($marks >= 90) {
                        echo "<span class='grade-a'>Your Grade is A</span>";
                    } elseif ($marks >= 75) {
                        echo "<span class='grade-b'>Your Grade is B</span>";
                    } elseif ($marks >= 60) {
                        echo "<span class='grade-c'>Your Grade is C</span>";
                    } elseif ($marks >= 50) {
                        echo "<span class='grade-d'>Your Grade is D</span>";
                    } else {
                        echo "<span class='grade-f'>Your Grade is F (Fail)</span>";
                    }
                }
                ?>
            </div>

        </div>
    </form>
</body>

</html>