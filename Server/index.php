<?php
$connection = mysqli_connect("127.0.0.1", "root", "", "tan");
$query = "SELECT * FROM papers";
$result = mysqli_query($connection, $query);
while ($row = mysqli_fetch_assoc($result)) {
    $array[] = $row;
}
header('Content-Type:Application/json');
echo json_encode($array);
?>

