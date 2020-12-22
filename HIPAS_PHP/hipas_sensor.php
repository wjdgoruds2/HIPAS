<?php


$user = "scv0319";
$pass = "jessica0319!";
$host = "localhost";
$dbname = "scv0319";

$temp = $_GET['temp'];
$gas = $_GET['gas'];
$danger = $_GET['danger'];

$con = mysqli_connect($host,$user,$pass,$dbname);

$sql = "INSERT INTO hipas_scv0319Sensor(Temp, Gas, Danger) VALUES ($temp, $gas, $danger)";
$sql2 = "DELETE FROM hipas_scv0319Sensor WHERE Nowtime < DATE_SUB(NOW(), INTERVAL 1 MINUTE)";

mysqli_query($con, $sql);
mysqli_query($con, $sql2);
mysqli_close($con);
?>