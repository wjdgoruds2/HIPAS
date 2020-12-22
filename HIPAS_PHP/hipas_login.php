<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 
 $userid  = $_GET['userid'];
 $userpassword  = $_GET['userpassword'];
 
$user = "scv0319";
$pass = "jessica0319!";
$host= "localhost";
$dbname="scv0319";

$con = mysqli_connect($host,$user,$pass,$dbname);
 
 $sql = "SELECT * FROM hipas_member WHERE userid='".$userid."' and userpassword ='".$userpassword."'";
 

 $r = mysqli_query($con,$sql);
 
 $res = mysqli_fetch_array($r);
 
  $result = array();
 
 if(sizeof($res)>0){
	 
    array_push($result,array(	
 	"userid"=>$res['userid'],
 	"userpassword"=>$res['userpassword'],
 	"username"=>$res['username'],
 	"email"=>$res['email']
 ));
 
 echo json_encode(array("result"=>$result));
 
	 
 }else {
	 
	 array_push($result,array(
     "error"=>'error',
 
 )
 );
 
 echo json_encode(array("result"=>$result));
 }
 
 mysqli_close($con);
 
 }
 
?>