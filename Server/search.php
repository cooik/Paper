<?php
if (isset($_GET['search'])) {
    require_once 'DBOperations.php';
    $db = new DBOperations ();
    $result = $db->searchPapers($_GET['search']);
    echo json_encode($result);
}


