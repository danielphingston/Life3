<?php
    $con = mysqli_connect("localhost", "id1114817_life", "password", "id1114817_life");
    
    $username = $_POST["username"];
	  $password = $_POST["password"];
   

    $statement = mysqli_prepare($con, "DELETE FROM login WHERE username=? ");
    mysqli_stmt_bind_param($statement,"s",$username);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    print_r(json_encode($response));

?>