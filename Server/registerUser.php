<?php

function getMsgParamNotEmpty() {
    $response ["result"] = "failure";
    $response ["message"] = "Parameters should not be empty !";
    return json_encode($response);
}

function getMsgInvalidParam() {
    $response ["result"] = "failure";
    $response ["message"] = "Invalid Parameters";
    return json_encode($response);
}

require_once 'DBOperations.php';
if ($_SERVER ['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    if (isset($data->user) && !empty($data->user) && isset($data->user->name) && isset($data->user->work_id) && isset($data->user->email) && isset($data->user->password)) {
        $db = new DBOperations ();
        $user = $data->user;
        $work_id = $user->work_id;
        $name = $user->name;
        $email = $user->email;
        $password = $user->password;
        if ($db->checkUserExist($work_id)) {
            $response ["result"] = "failure";
            $response ["message"] = "User exist !";
            echo json_encode($response);
        } else {
            $result = $db->insertUserData($work_id, $name, $email, $password);
            if ($result) {

                $response ["result"] = "success";
                $response ["message"] = "User Registered Successfully !";
                echo json_encode($response);
            } else {
                $response ["result"] = "failure";
                $response ["message"] = "Registration Failure";
                echo json_encode($response);
            }
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

