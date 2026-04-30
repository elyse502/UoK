<?php
function calculateFine($daysLate)
{
    if ($daysLate >= 0 && $daysLate <= 3) {
        $fine = 0;
    } elseif ($daysLate >= 4 && $daysLate <= 7) {
        $fine = 500;
    } elseif ($daysLate >= 8 && $daysLate <= 14) {
        $fine = 1000;
    } else {
        $fine = 2000;
    }

    return "Book is $daysLate days late. Fine: $fine RWF";
}
?>

<!DOCTYPE html>
<html>

<body>

    <form method="POST">
        <label>Enter days late:</label><br>
        <input type="number" name="daysLate" required>
        <button type="submit">Calculate Fine</button>
    </form>

    <?php
    if (isset($_POST['daysLate'])) {
        $daysLate = $_POST['daysLate'];

        // Validation
        if (!is_numeric($daysLate) || $daysLate < 0) {
            echo "<p style='color:red;'>Please enter a valid number of days.</p>";
        } else {
            echo "<p>" . calculateFine($daysLate) . "</p>";
        }
    }
    ?>

</body>

</html>