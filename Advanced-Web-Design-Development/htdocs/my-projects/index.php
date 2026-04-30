<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <?php
        // echo "Hello World! </br>" . "My name is Elysee" . "</br>";
        // print() is somehow slower than echo() but it can be used in expressions while echo() cannot be used in expressions.
        // print("Hello World! </br>" . "My name is Elysee" . "</br>");

        $myName = "Elysee NIYIBIZI";
        $address = "KK 806 St";
        $phone = "+250 788 123 456";
        $age = 20;
        // echo $myName;

        $x = 10;
        echo $x++, "</br>"; // Outputs 10, then increments to 11
        echo ++$x; // Increments to 12, then outputs 12

        echo "<h1>MY DETAILS ARE: </h1></br>";
        echo "NAMES: " . $myName . "</br>";
        echo "ADDRESS: " . $address . "</br>";
        echo "PHONE: " . $phone . "</br>";
        echo "AGE: " . $age . "</br>";

        $name = "John";
        printf("Hello %s</br>", $name);
        // 1. Integers
        printf("You have %d messages</br>", 5);
        // 2. Floats (precision)
        printf("Price: %.2f</br>", 12.3456);
        // 3. Padding and width
        printf("%05d</br>", 42);
        // → "Hi !" (left aligned)
        printf("%-10s!</br>", "Hi");
        // 4. Multiple values
        printf("%s is %d years old</br>", "Alice", 25);
        
        // 5. Using sprintf() (returns string instead of printing)
        $text = sprintf("Total: €%.2f", 19.99);
        echo $text, "</br>";
        // Positional specifiers (very useful!)
        printf('%2$s is %1$d years old', 25, 'Alice');
        echo "</br>";

        // Cross-platform newline (best practice)
        echo "Line 1" . PHP_EOL . "Line 2", "</br>";

       echo "<br/> <hr/> <br/>";

       // Date and time
        echo date("Y-m-d H:i:s") . "</br>"; // Outputs current date and time

        echo "@Copyright &copy; " . date("Y");
        echo "<br/> <br/> <hr/> <br/>";

        $firstName = "<i>John</i>";
        $age = 30;
        $day = date("l");
        echo "Hello" . " " . $firstName . " you have " . $age . " years old and remember today is " . $day . ".";
        echo "<br/> <hr/> <br/>";

        $marks = 85;
        $gradeMessage = "Your grade is: ";

        if ($marks >= 80) {
            $grade = "A";
        } elseif ($marks >= 70) {
            $grade = "B";
        } elseif ($marks >= 60) {
            $grade = "C";
        } elseif ($marks >= 50) {
            $grade = "D";
        } else {
            $grade = "F";
        }

        echo $gradeMessage . $grade;
        echo "<br/> <hr/> <br/>";

        // Switch statement
        $dayOfWeek = date("l");

        switch ($dayOfWeek) {
            case "Monday":
                echo "Have a nice Monday!";
                break;
            case "Tuesday":
                echo "Have a nice Tuesday!";
                break;
            case "Wednesday":
                echo "Have a nice Wednesday!";
                break;
            case "Thursday":
                echo "Have a nice Thursday!";
                break;
            case "Friday":
                echo "Have a nice Friday!";
                break;
            default:
                echo "Have a nice day!";
        }
        echo "<br/> <br /> <hr/> <br/>";

        // Arrays
        $products = ["Laptop", "Smartphone", "Tablet", "Headphones", "Smartwatch"];
        // Other way to declare an array
        // $products = array("Laptop", "Smartphone", "Tablet", "Headphones", "Smartwatch");
        echo "Product on position 2 is: " . $products[2]; // Outputs "Tablet"
        echo "<br/> <br /> <hr/> <br/>";

        // Associative arrays
        $person = [
            "name" => "Alice",
            "age" => 30,
            "city" => "New York"
        ];
        // Other way to declare an associative array
        // $person = array("name" => "Alice", "age" => 30, "city" => "New York");
        echo "Name: " . $person["name"], "<br/>"; // Outputs "Alice"
        $book = [
            "title" => "The Great Gatsby",
            "author" => "F. Scott Fitzgerald",
            "year" => 1925
        ];
        echo "Title: " . $book["title"], "<br/>"; // Outputs "The Great Gatsby"

        // Multi-dimensional arrays
        $matrix = [
            [1, 2, 3],
            [4, 5, 6],
            [7, 8, 9]
        ];
        echo "Element at row 1, column 2: " . $matrix[1][2], "<br/>"; // Outputs 6
        $threeBooks = [
            ["To Kill a Mockingbird", "Harper Lee", 1960],
            ["1984", "George Orwell", 1949],
            ["The Great Gatsby", "F. Scott Fitzgerald", 1925]
        ];

        // Printing each book's publisher
        echo "Books and their publishers: <br/>";
        echo ">> Publisher of " . $threeBooks[0][0] . " is " . $threeBooks[0][1] . "<br/>";
        echo ">> Publisher of " . $threeBooks[1][0] . " is " . $threeBooks[1][1] . "<br/>";
        echo ">> Publisher of " . $threeBooks[2][0] . " is " . $threeBooks[2][1] . "<br/>";
        echo "<br/> <br /> <hr/> <br/>";

        // Loops
        // For loop
        echo "For loop: <br/>";
        for ($i = 0; $i < 5; $i++) {
            echo "Iteration: " . $i . "<br/>";
        }
        // While loop
        echo "While loop: <br/>";
        $j = 0;
        while ($j < 5) {
            echo "Iteration: " . $j . "<br/>";
            $j++;
        }
        // Do-while loop
        echo "Do-while loop: <br/>";
        $k = 0;
        do {
            echo "Iteration: " . $k . "<br/>";
            $k++;
        } while ($k < 5);
        echo "<br/> <br /> <hr/> <br/>";

        // Foreach loop (for arrays)
        echo "Foreach loop: <br/>";
        $colors = ["Red", "Green", "Blue"];
        echo ">> Foreach loop with indexed array: <br/>";
        foreach ($colors as $color) {
            echo "Color: " . $color . "<br/>";
        }
        echo ">> length of colors array is: " . count($colors) . "<br/>";
        for ($a = 0; $a < count($colors); $a++) {
            echo "Color: " . $colors[$a] . "<br/>";
        }
        // Foreach loop with associative array
        $person = [
            "name" => "Alice",
            "age" => 30,
            "city" => "New York"
        ];
        echo ">> Foreach loop with associative array: <br/>";
        foreach ($person as $key => $value) {
            echo $key . ": " . $value . "<br/>";
        }
        $studentInfo = [
            "name" => "Bob",
            "age" => 22,
            "major" => "Computer Science"
        ];
        echo ">> Foreach loop with associative array for student info: <br/>";
        foreach ($studentInfo as $key => $value) {
            echo $key . ": " . $value . "<br/>";
        }
        echo "<br/> <hr/> <br/>";

        // Functions
        function greet($name) {
            return "Hello, " . $name . "!";
        }
        echo greet("Alice"), "<br/>";

        function add($num1, $num2) {
            return $num1 + $num2;
        }
        echo "Sum is: " . add(5, 10), "<br/>";

        // Area of a rectangle
        function areaOfRectangle($length, $width) {
            return $length * $width;
        }
        echo "Area of rectangle is: " . areaOfRectangle(5, 3), "<br/>";
         function greetUser($name) {
            return "Hello " . $name . ", we are in " . date("Y") . "!";
        }
        echo greetUser("John"), "<br/>";
        echo "<br/> <hr/> <br/>";
    ?>
</body>
</html>