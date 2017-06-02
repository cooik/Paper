<?php
require_once 'DBOperations.php';
$data = json_decode(file_get_contents("php://input"));
if (isset($data->work_id) && !empty($data->password)) {
    $work_id = $data->work_id;
    $password = $data->password;
    $db = new DBOperations ();
    $db->checkLogin($work_id, $password);
} else {
    $response ["result"] = "failure";
    $response ["message"] = "Invalid Parameters";
    echo json_encode($response);
}
?>
