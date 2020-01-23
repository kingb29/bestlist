<?php


if($_SERVER['REQUEST_METHOD']=='POST'){
    
    // get data from POST
    $username = $_POST['Username'];

    require_once('dbconnection.php'); // establish connection to database

    $sql = "SELECT Salt FROM Users WHERE Username = '$username'"; // sql to be inserted
    try{
        
        $sth = $conn->prepare($sql);
        $sth->execute(); // attempt to execute command
        $result = $sth->fetch(PDO::FETCH_ASSOC);
        echo $result['Salt'];
    
        }catch(PDOException $e){
            echo "ERROR: " . $e->getMessage(); // output error message
        }

        $conn = null; // end connection

}

?>