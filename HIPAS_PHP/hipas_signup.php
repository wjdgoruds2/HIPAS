<?php


$userid = $_POST['userid'];
$userpassword = $_POST['userpassword'];	
$username = $_POST['username'];				
$email = $_POST['email'];
$token = $_POST['token'];

$user = "scv0319";
$pass = "jessica0319!";
$host= "localhost";
$dbname="scv0319";

echo $userid;
echo $userpassword;
echo $username;
echo $email;
 
$con = mysqli_connect($host,$user,$pass,$dbname);
$sql="insert into hipas_member (userid, userpassword, username, email, token) values('".$userid."','".$userpassword."','".$username."','".$email."','".$token."');";
$sql2="CREATE TABLE hipas_"."$userid"."Sensor (Nowtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, Temp INT(5), Gas INT(5), Danger INT(5));";
if(mysqli_query($con,$sql) && mysqli_query($con, $sql2)){
	echo  "success";
	
}else{
	
	echo "failed";
}

?>