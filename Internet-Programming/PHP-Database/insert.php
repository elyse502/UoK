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
        
        // Taking all 5 values from the form data(input)
        $first_name = $_REQUEST['first_name'];
        $last_name = $_REQUEST['last_name'];
        $gender = $_REQUEST['gender'];
        $address = $_REQUEST['address'];
        $email = $_REQUEST['email'];
        
        // Performing insert query execution
        // here our table name is college
        $sql = "INSERT INTO college VALUES ('$first_name',
        '$last_name','$gender','$address','$email')";
        
        if(mysqli_query($conn, $sql)){
        echo "<h3>Data stored in a database successfully."
        . " Please browse your localhost php my admin"
        . " to view the updated data</h3>";
        echo nl2br("\n$first_name\n $last_name\n "
        . "$gender\n $address\n $email");
        } else{
        echo "ERROR: Hush! Sorry $sql. "
        . mysqli_error($conn);
        }
        
        // Close connection
        mysqli_close($conn);
        ?>
    </center>
</body>
</html>


