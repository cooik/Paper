<?php
if (isset($_GET['name'])) {
    require_once 'DBOperations.php';
    $db = new DBOperations ();
    $result = $db->searchPapersbyName($_GET['name']);
    echo json_encode($result);
}
