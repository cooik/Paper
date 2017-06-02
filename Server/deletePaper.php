<?php
if (isset($_GET['id'])) {
    require_once 'DBOperations.php';
    $db = new DBOperations ();
    $db->deletePaper($_GET['id']);
   
}


