<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    

    require_once('dbconnection.php'); // establish connection to database

    // get data from POST
    $username = $_POST['Username'];
    $password = $_POST['HashPass'];
    $salt = $_POST['Salt'];

    $sql = "SELECT ID FROM Users WHERE Username = '$username'";
    try{
        
        $sth = $conn->prepare($sql);
        $sth->execute(); // attempt to execute command
        $result = $sth->fetch(PDO::FETCH_ASSOC);
        if(strcmp($result,"Array") == 0){
            $conn = null;
            echo "Username already taken!";
        }else{

            $sql = "INSERT INTO Users (Username, HashPass, Salt) VALUES ('$username', '$password', '$salt')"; // sql to be inserted
            try{
                
            $conn->exec($sql); // attempt to execute command
            echo "Success";
        
            }catch(PDOException $e){
                echo "ERROR: " . $e->getMessage(); // output error message
            }
        
                $conn = null; // end connection

        }
    
        }catch(PDOException $e){
            echo "ERROR: " . $e->getMessage(); // output error message
        }
            

  

}



?>