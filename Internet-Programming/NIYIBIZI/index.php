<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GFG- Store Data</title>
</head>
<body>
    <center>
        <h1>Storing Form data in Database</h1>
        <form action="insert.php" method="post">
            <p>
            <label for="firstName">First Name:</label>
            <input type="text" name="first_name" id="firstName">
            </p>
            
            <p>
            <label for="lastName">Last Name:</label>
            <input type="text" name="last_name" id="lastName">
            </p>
            
            <p>
            <label for="Gender">Gender:</label>
            <input type="text" name="gender" id="Gender">
            </p>
            
            <p>
            <label for="Address">Address:</label>
            <input type="text" name="address" id="Address">
            </p>
            
            <p>
            <label for="emailAddress">Email Address:</label>
            <input type="text" name="email" id="emailAddress">
            </p>
            <input type="submit" value="Submit">
        </form>
        <br><br>
        <a href="display.php" target="_blank">VIEW DATA FROM COLLEGE TABLE IN HTML TABLE</a>
        <br><br>
        <a href="update.php" target="_blank">MAKE AN UPDATE OF A RECORD</a>
        <br><br>
        <a href="delete.php" target="_blank">DELETE A RECORD</a>
    </center>
</body>
</html>







