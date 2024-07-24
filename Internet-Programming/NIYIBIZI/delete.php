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
        // servername => localhost
        // username => root
        // password => empty
        // database name => staff
        $conn = mysqli_connect("localhost", "root", "", "staff");
        
        // Check connection
        if($conn === false){
            die("ERROR: Could not connect. "
            . mysqli_connect_error());
        }

        // Updating
        $link = mysqli_connect("localhost", "root", "", "staff");
        if($link === false){
        die("ERROR: Could not connect. " . mysqli_connect_error());
        }
        $sql = "DELETE FROM college WHERE first_name='Yves'";
        if(mysqli_query($link, $sql)){
        echo "Record was deleted successfully.";
        }
        else{
        echo "ERROR: Could not able to execute $sql. " . mysqli_error($link);
        }
        mysqli_close($link);
        ?>
    </center>
</body>
</html>

