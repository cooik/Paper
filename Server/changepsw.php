<?php

require_once 'DBOperations.php';
$data = json_decode(file_get_contents("php://input"));
$work_id = $data->work_id;
$old_password = $data->old_password;
$new_password = $data->new_password;
if (!empty($work_id) && !empty($old_password) && !empty($new_password)) {

    if (!$db->checkpsw($work_id, $old_password)) {

        $response ["result"] = "failure";
        $response ["message"] = 'Invalid Old Password';
    } else {

        $result = $db->changePassword($work_id, $new_password);
        if ($result) {
            $response ["result"] = "success";
            $response ["message"] = "Password Changed Successfully";
        } else {
            $response ["result"] = "failure";
            $response ["message"] = 'Error Updating Password';
        }
    }
} else {
    $response ["result"] = "failure";
    $response ["message"] = "Parameters should not be empty !";
}
echo json_encode($response);


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

