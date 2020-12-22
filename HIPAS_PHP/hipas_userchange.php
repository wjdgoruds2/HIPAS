<?php

$userid = $_POST['userid'];
$email= $_POST['email'];
$userpassword = $_POST['userpassword'];

$user = "scv0319";
$pass = "jessica0319!";
$host= "localhost";
$dbname="scv0319";

$con = mysqli_connect($host,$user,$pass,$dbname);

$sql = "update hipas_member set userpassword='".$userpassword."',email='".$email."' where userid = '$userid';";

echo $sql;

if(mysqli_query($con,$sql)){
	echo  "success";
	
}else{
	echo "failed";
}

?>