<?php

     $con = mysqli_connect('localhost', 'scv0319', 'jessica0319!', 'scv0319');

     $userid = $_POST["userid"];


     $statement = mysqli_prepare($con, "SELECT userid FROM hipas_member WHERE userid = ?");

     //������ *�� �ϸ� mysqli_stmt_bind_result���� ������ ���� ������


     mysqli_stmt_bind_param($statement, "s", $userid);

     mysqli_stmt_execute($statement);

     mysqli_stmt_store_result($statement);//����� Ŭ���̾�Ʈ�� ������

     mysqli_stmt_bind_result($statement, $userid);//����� $userID�� ���ε���


     $response = array();

     $response["success"] = true;


     while(mysqli_stmt_fetch($statement)){

       $response["success"] = false;//ȸ�����ԺҰ��� ��Ÿ��

       $response["userid"] = $userid;

     }


     //�����ͺ��̽� �۾��� ���� Ȥ�� �����Ѱ��� �˷���

     echo json_encode($response);

?>
