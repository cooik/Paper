<?php

require_once 'DBOperations.php';
$data = json_decode(file_get_contents("php://input"));
if (isset($data->paper) && !empty($data->paper) && isset($data->paper->title) && isset($data->paper->author) && isset($data->paper->description) && isset($data->paper->file_name) && isset($data->paper->categories) && isset($data->paper->journal_title)) {
    $db = new DBOperations ();
    $paper = $data->paper;
    $author = $paper->author;
    $title = $paper->title;
    $categories = $paper->categories;
    $journal_title = $paper->journal_title;
    $description = $paper->description;
    $file_name = $paper->file_name;
    $result = $db->insertPaper($author, $title, $categories, $journal_title, $description, $file_name);
    if ($result) {
        $response ["result"] = "success";
        $response ["message"] = "Paper Upload Successfully !";
        echo json_encode($response);
    } else {
        $response ["result"] = "failure";
        $response ["message"] = "Paper UploadPaper Upload Failure";
        echo json_encode($response);
    }
}