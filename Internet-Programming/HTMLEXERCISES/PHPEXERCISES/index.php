<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <?php
    $d = date("D");
    if ($d=="Sat") {
        echo "Happy Saturday!";
    }
    else if ($d=="Sun") {
        echo "Happy Sunday!";
    }
    else {
        echo "Have a nice day";
    }
    print "<br /><br />";
    $cars=array("Saab", "Volvo", "BMW", "Toyota");
    echo "Car on index 0 is : " . $cars[0];
    print "<br /><br />";
    foreach ($cars as $value) {
        echo $value . "<br />";
    }
    print "<br /><br />";
    for($j = 5; $j >= 1; $j--)
        echo "The number is...".$j."<br />";
    print "<br /><br />";
    $i = 1;
    do{
        echo "The number is...".$i."<br />";
        $i++;
    } while ($i <= 5);
    print "<br /><br />";
    function writeName() {
        echo "Elysée NIYIBIZI";
    }
    echo "My name is ";
    writeName();
    ?>
</body>
</html>