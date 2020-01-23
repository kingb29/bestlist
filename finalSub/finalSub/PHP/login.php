<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    
    // get data from POST
    $username = $_POST['Username'];
    $password = $_POST['HashPass'];

    require_once('dbconnection.php'); // establish connection to database

    $sql = "SELECT Username,HashPass FROM Users where Username like %{$username}%"; // sql to be inserted
    try{
        
    $conn->exec($sql); // attempt to execute command
    if($row['Username'] == $username && $row['HashPass'] == $password){
        echo "Success";
    }
    

    }catch(PDOException $e){
        echo "ERROR: " . $e->getMessage(); // output error message
    }

        $conn = null; // end connection

}




?>