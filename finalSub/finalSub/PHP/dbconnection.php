<?php
    $hostname = "localhost";
    $login = "kingb29";
    $pass = "ADAisshit12349";

    try{
        $conn = new PDO("mysql:host=$hostname;dbname=BestList", $login, $pass);

        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        
   
    }catch(PDOException $e){

    echo "Connection failed: " . $e->getMessage();
    }
    
?>