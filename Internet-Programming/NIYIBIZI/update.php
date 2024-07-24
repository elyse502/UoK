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
        $sql = "UPDATE college SET address='KK-806 St' WHERE first_name='Elysée' ";
        if(mysqli_query($conn, $sql)){
        echo "Record was updated successfully";
        } else{
        echo "ERROR: Could not able to execute $sql. " . mysqli_error($conn);
        }
        
        // Close connection
        mysqli_close($conn);
        ?>
    </center>
</body>
</html>


